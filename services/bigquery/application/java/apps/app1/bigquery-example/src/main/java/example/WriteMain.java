package example;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.threeten.bp.Duration;

import com.google.api.core.ApiFuture;
import com.google.api.gax.retrying.RetrySettings;
import com.google.cloud.bigquery.storage.v1.AppendRowsResponse;
import com.google.cloud.bigquery.storage.v1.BigQueryWriteClient;
import com.google.cloud.bigquery.storage.v1.CreateWriteStreamRequest;
import com.google.cloud.bigquery.storage.v1.FinalizeWriteStreamRequest;
import com.google.cloud.bigquery.storage.v1.FlushRowsRequest;
import com.google.cloud.bigquery.storage.v1.FlushRowsResponse;
import com.google.cloud.bigquery.storage.v1.JsonStreamWriter;
import com.google.cloud.bigquery.storage.v1.TableName;
import com.google.cloud.bigquery.storage.v1.WriteStream;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Int64Value;

public class WriteMain {

    public static void main(String[] args) throws Exception {
        writeBufferedStream("aspf-jp-test", "my_dataset", "test_table1");
    }

    public static void writeBufferedStream(String projectId, String datasetName, String tableName)
            throws DescriptorValidationException, InterruptedException, IOException {
        try (BigQueryWriteClient client = BigQueryWriteClient.create()) {
            // Initialize a write stream for the specified table.
            // For more information on WriteStream.Type, see:
            // https://googleapis.dev/java/google-cloud-bigquerystorage/latest/com/google/cloud/bigquery/storage/v1/WriteStream.Type.html
            WriteStream stream = WriteStream.newBuilder().setType(WriteStream.Type.BUFFERED).build();
            TableName parentTable = TableName.of(projectId, datasetName, tableName);
            CreateWriteStreamRequest createWriteStreamRequest = CreateWriteStreamRequest.newBuilder()
                    .setParent(parentTable.toString())
                    .setWriteStream(stream)
                    .build();
            WriteStream writeStream = client.createWriteStream(createWriteStreamRequest);

            // Configure in-stream automatic retry settings.
            // Error codes that are immediately retried:
            // * ABORTED, UNAVAILABLE, CANCELLED, INTERNAL, DEADLINE_EXCEEDED
            // Error codes that are retried with exponential backoff:
            // * RESOURCE_EXHAUSTED
            RetrySettings retrySettings = RetrySettings.newBuilder()
                    .setInitialRetryDelay(Duration.ofMillis(500))
                    .setRetryDelayMultiplier(1.1)
                    .setMaxAttempts(5)
                    .setMaxRetryDelay(Duration.ofMinutes(1))
                    .build();

            // Use the JSON stream writer to send records in JSON format.
            // For more information about JsonStreamWriter, see:
            // https://cloud.google.com/java/docs/reference/google-cloud-bigquerystorage/latest/com.google.cloud.bigquery.storage.v1.JsonStreamWriter
            try (JsonStreamWriter writer = JsonStreamWriter
                    .newBuilder(writeStream.getName(), writeStream.getTableSchema())
                    .setRetrySettings(retrySettings)
                    .build()) {
                // Write two batches to the stream, each with 10 JSON records.
                // for (int i = 0; i < 2; i++) {
                // JSONArray jsonArr = new JSONArray();
                // for (int j = 0; j < 10; j++) {
                // // Create a JSON object that is compatible with the table schema.
                // JSONObject record = new JSONObject();
                // record.put("col1", String.format("buffered-record %03d", i));
                // jsonArr.put(record);
                // }
                // ApiFuture<AppendRowsResponse> future = writer.append(jsonArr);
                // AppendRowsResponse response = future.get();
                // }
                JSONArray jsonArr = new JSONArray();
                JSONObject record = new JSONObject();
                record.put("record_num", 101);
                record.put("name", "Kevin");
                record.put("age", 21);
                record.put("address", "ENGLAND");
                jsonArr.put(record);
                ApiFuture<AppendRowsResponse> future = writer.append(jsonArr);
                AppendRowsResponse response = future.get();
                // Flush the buffer.
                FlushRowsRequest flushRowsRequest = FlushRowsRequest.newBuilder()
                        .setWriteStream(writeStream.getName())
                        .setOffset(Int64Value.of(1 * 1 - 1)) // Advance the cursor to the latest record.
                        .build();
                FlushRowsResponse flushRowsResponse = client.flushRows(flushRowsRequest);
                // You can continue to write to the stream after flushing the buffer.
            }
            // Finalize the stream after use.
            FinalizeWriteStreamRequest finalizeWriteStreamRequest = FinalizeWriteStreamRequest.newBuilder()
                    .setName(writeStream.getName()).build();
            client.finalizeWriteStream(finalizeWriteStreamRequest);
            System.out.println("Appended and committed records successfully.");
        } catch (ExecutionException e) {
            // If the wrapped exception is a StatusRuntimeException, check the state of the
            // operation.
            // If the state is INTERNAL, CANCELLED, or ABORTED, you can retry. For more
            // information, see:
            // https://grpc.github.io/grpc-java/javadoc/io/grpc/StatusRuntimeException.html
            System.out.println(e);
        }
    }
}
