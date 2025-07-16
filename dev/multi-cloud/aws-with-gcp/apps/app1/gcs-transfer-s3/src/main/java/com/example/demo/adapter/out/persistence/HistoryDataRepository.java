package com.example.demo.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;

import com.example.demo.application.domain.entity.TmpNontcuUcdHistoryEntity;
import com.example.demo.application.port.out.HistoryDataReadPort;
import com.example.demo.common.constants.BatchConst;
import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigquery.storage.v1.AvroRows;
import com.google.cloud.bigquery.storage.v1.BigQueryReadClient;
import com.google.cloud.bigquery.storage.v1.CreateReadSessionRequest;
import com.google.cloud.bigquery.storage.v1.DataFormat;
import com.google.cloud.bigquery.storage.v1.ReadRowsRequest;
import com.google.cloud.bigquery.storage.v1.ReadRowsResponse;
import com.google.cloud.bigquery.storage.v1.ReadSession;
import com.google.cloud.bigquery.storage.v1.ReadSession.TableReadOptions;
import com.google.common.base.Preconditions;

public class HistoryDataRepository implements HistoryDataReadPort {

    private static final String DATASET_NAME = "ddl_test";

    private static final String TABLE_NAME = "TMP_NONTCU_UCD_HISTORY";

    private static final String KEY_WD_PROJECTS = "projects/";

    private static final String FMT_SRC_TABLE_PATH = "projects/%s/datasets/%s/tables/%s";

    private String projectPath;

    private String srcTable;

    @Override
    public void initialize(Properties batchParameter) throws Exception {
        String strProjectId = batchParameter.getProperty(BatchConst.KEY_PROJECT_ID);
        this.projectPath = KEY_WD_PROJECTS + strProjectId;
        this.srcTable = String.format(FMT_SRC_TABLE_PATH, strProjectId, DATASET_NAME, TABLE_NAME);
    }

    @Override
    public List<String> findById(String vehicleId) throws Exception {
        List<String> records = new ArrayList<>();

        try (BigQueryReadClient client = BigQueryReadClient.create()) {

            TableReadOptions options = TableReadOptions.newBuilder()
                    .addAllSelectedFields(TmpNontcuUcdHistoryEntity.getSchemaItr())
                    .setRowRestriction("vehicleId = '" + vehicleId + "'")
                    .build();

            ReadSession.Builder sessionBuilder = ReadSession.newBuilder()
                    .setTable(this.srcTable)
                    .setDataFormat(DataFormat.AVRO)
                    .setReadOptions(options);

            CreateReadSessionRequest.Builder builder = CreateReadSessionRequest.newBuilder()
                    .setParent(this.projectPath)
                    .setReadSession(sessionBuilder)
                    .setMaxStreamCount(1);

            ReadSession session = client.createReadSession(builder.build());

            SimpleRowReader reader = new SimpleRowReader(
                    new Schema.Parser().parse(session.getAvroSchema().getSchema()));

            Preconditions.checkState(session.getStreamsCount() > 0);

            String streamName = session.getStreams(0).getName();

            ReadRowsRequest readRowsRequest = ReadRowsRequest.newBuilder().setReadStream(streamName).build();

            ServerStream<ReadRowsResponse> stream = client.readRowsCallable().call(readRowsRequest);

            for (ReadRowsResponse response : stream) {
                Preconditions.checkState(response.hasAvroRows());
                List<String> streamRecords = reader.processRows(response.getAvroRows());
                records.addAll(streamRecords);
            }
        }

        return records;
    }

    private static class SimpleRowReader {

        private final DatumReader<GenericRecord> datumReader;

        private BinaryDecoder decoder = null;

        private GenericRecord row = null;

        public SimpleRowReader(Schema schema) {
            Preconditions.checkNotNull(schema);
            datumReader = new GenericDatumReader<>(schema);
        }

        public List<String> processRows(AvroRows avroRows) throws Exception {
            List<String> records = new ArrayList<>();
            decoder = DecoderFactory.get().binaryDecoder(avroRows.getSerializedBinaryRows().toByteArray(), decoder);

            while (!decoder.isEnd()) {
                row = datumReader.read(row, decoder);
                records.add(row.toString());
            }

            return records;
        }
    }

}
