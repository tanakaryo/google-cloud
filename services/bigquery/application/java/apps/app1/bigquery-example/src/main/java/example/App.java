package example;

import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigquery.storage.v1.ArrowRecordBatch;
import com.google.cloud.bigquery.storage.v1.ArrowSchema;
import com.google.cloud.bigquery.storage.v1.BigQueryReadClient;
import com.google.cloud.bigquery.storage.v1.CreateReadSessionRequest;
import com.google.cloud.bigquery.storage.v1.DataFormat;
import com.google.cloud.bigquery.storage.v1.ReadRowsRequest;
import com.google.cloud.bigquery.storage.v1.ReadRowsResponse;
import com.google.cloud.bigquery.storage.v1.ReadSession;
import com.google.cloud.bigquery.storage.v1.ReadSession.TableModifiers;
import com.google.cloud.bigquery.storage.v1.ReadSession.TableReadOptions;
import com.google.common.base.Preconditions;
import com.google.protobuf.Timestamp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.VectorLoader;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ReadChannel;
import org.apache.arrow.vector.ipc.message.MessageSerializer;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.Schema;
import org.apache.arrow.vector.util.ByteArrayReadableSeekableByteChannel;


public class App {

    private static class SimpleRowReader implements AutoCloseable {

        BufferAllocator allocator = new RootAllocator(Long.MAX_VALUE);
    
        // Decoder object will be reused to avoid re-allocation and too much garbage collection.
        private final VectorSchemaRoot root;
        private final VectorLoader loader;
    
        public SimpleRowReader(ArrowSchema arrowSchema) throws IOException {
          Schema schema =
              MessageSerializer.deserializeSchema(
                  new ReadChannel(
                      new ByteArrayReadableSeekableByteChannel(
                          arrowSchema.getSerializedSchema().toByteArray())));
          Preconditions.checkNotNull(schema);
          List<FieldVector> vectors = new ArrayList<>();
          for (Field field : schema.getFields()) {
            vectors.add(field.createVector(allocator));
          }
          root = new VectorSchemaRoot(vectors);
          loader = new VectorLoader(root);
        }
    
        /**
         * Sample method for processing Arrow data which only validates decoding.
         *
         * @param batch object returned from the ReadRowsResponse.
         */
        public void processRows(ArrowRecordBatch batch) throws IOException {
          org.apache.arrow.vector.ipc.message.ArrowRecordBatch deserializedBatch =
              MessageSerializer.deserializeRecordBatch(
                  new ReadChannel(
                      new ByteArrayReadableSeekableByteChannel(
                          batch.getSerializedRecordBatch().toByteArray())),
                  allocator);
    
          loader.load(deserializedBatch);
          // Release buffers from batch (they are still held in the vectors in root).
          deserializedBatch.close();
          System.out.println(root.contentToTSVString());
          // Release buffers from vectors in root.
          root.clear();
        }
    
        @Override
        public void close() {
          root.close();
          allocator.close();
        }
      }
    public static void main(String[] args) throws Exception {
        String projectId = "aspf-jp-test";
        Integer snapshotMillis = null;
        if (args.length > 1) {
          snapshotMillis = Integer.parseInt(args[1]);
        }
    
        try (BigQueryReadClient client = BigQueryReadClient.create()) {
          String parent = String.format("projects/%s", projectId);
    
          // This example uses baby name data from the public datasets.
          String srcTable =
              String.format(
                  "projects/%s/datasets/%s/tables/%s",
                  "aspf-jp-test", "my_dataset", "test_table1");
    
          // We specify the columns to be projected by adding them to the selected fields,
          // and set a simple filter to restrict which rows are transmitted.
          List<String> columnLst = new ArrayList<>();
          columnLst.add("record_num");
          columnLst.add("name");
          columnLst.add("age");
          columnLst.add("address");
          TableReadOptions options =
              TableReadOptions.newBuilder()
                  .addAllSelectedFields(columnLst)
                  .setRowRestriction(" record_num BETWEEN 1 AND 50 ")
                  .build();
    
          // Start specifying the read session we want created.
          ReadSession.Builder sessionBuilder =
              ReadSession.newBuilder()
                  .setTable(srcTable)
                  // This API can also deliver data serialized in Apache Avro format.
                  // This example leverages Apache Arrow.
                  .setDataFormat(DataFormat.ARROW)
                  .setReadOptions(options);
    
          // Optionally specify the snapshot time.  When unspecified, snapshot time is "now".
          if (snapshotMillis != null) {
            Timestamp t =
                Timestamp.newBuilder()
                    .setSeconds(snapshotMillis / 1000)
                    .setNanos((int) ((snapshotMillis % 1000) * 1000000))
                    .build();
            TableModifiers modifiers = TableModifiers.newBuilder().setSnapshotTime(t).build();
            sessionBuilder.setTableModifiers(modifiers);
          }
    
          // Begin building the session creation request.
          CreateReadSessionRequest.Builder builder =
              CreateReadSessionRequest.newBuilder()
                  .setParent(parent)
                  .setReadSession(sessionBuilder)
                  .setMaxStreamCount(1);
    
          ReadSession session = client.createReadSession(builder.build());
          // Setup a simple reader and start a read session.
          try (SimpleRowReader reader = new SimpleRowReader(session.getArrowSchema())) {
    
            // Assert that there are streams available in the session.  An empty table may not have
            // data available.  If no sessions are available for an anonymous (cached) table, consider
            // writing results of a query to a named table rather than consuming cached results
            // directly.
            Preconditions.checkState(session.getStreamsCount() > 0);
    
            // Use the first stream to perform reading.
            String streamName = session.getStreams(0).getName();
    
            ReadRowsRequest readRowsRequest =
                ReadRowsRequest.newBuilder().setReadStream(streamName).build();
    
            // Process each block of rows as they arrive and decode using our simple row reader.
            ServerStream<ReadRowsResponse> stream = client.readRowsCallable().call(readRowsRequest);
            for (ReadRowsResponse response : stream) {
              Preconditions.checkState(response.hasArrowRecordBatch());
              reader.processRows(response.getArrowRecordBatch());
            }
          }
        }
      }
}
