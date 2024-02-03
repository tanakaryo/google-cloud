package cloudfn;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.google.events.cloud.storage.v1.StorageObjectData;
import com.google.protobuf.util.JsonFormat;

import cloudfn.elements.StudentProfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.cloud.functions.CloudEventsFunction;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import io.cloudevents.CloudEvent;

public class JsonEventConverterFn implements CloudEventsFunction {

    private static final Logger logger = Logger.getLogger(GCSEventFunction.class.getName());

    private static final String HEADER_RECORD = "\"name\",\"age\",\"sex\",\"address\",\"school\"";

    private static final String EXT_JSON = ".json";
    private static final String EXT_CSV = ".csv";

    private static final char QUOTE_CHAR = '"';

    @Override
    public void accept(CloudEvent event) throws Exception {
        logger.info("Event" + event.getId());
        logger.info("Event Type:" + event.getType());

        if (event.getData() == null) {
            logger.warning("No data found in cloud event payload.");
            return;
        }

        String cloudEventData = new String(event.getData().toBytes(), StandardCharsets.UTF_8);
        StorageObjectData.Builder builder = StorageObjectData.newBuilder();
        JsonFormat.parser().merge(cloudEventData, builder);
        StorageObjectData data = builder.build();

        logger.info("Bucket: " + data.getBucket());
        logger.info("File: " + data.getName());
        logger.info("Metageneration: " + data.getMetageneration());
        logger.info("Created: " + data.getTimeCreated());
        logger.info("Updated: " + data.getUpdated());

        // ファイル名を作成
        String uploadFileName = StringUtils.replace(data.getName(), EXT_JSON, EXT_CSV);

        // Storageオブジェクト作成
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(System.getenv("PROJECTID"))
                .build().getService();

        // 対象ファイルダウンロード
        byte[] fileContent = storage.readAllBytes(data.getBucket(), data.getName());
        // ファイルはLF改行されているため、BufferedReaderで読み取り
        BufferedReader reader = new BufferedReader(new StringReader(new String(fileContent)));

        // CSVファイル構築用StringBuilder(ヘッダレコード追加)
        StringBuilder outputFileBuilder = new StringBuilder();
        outputFileBuilder.append(HEADER_RECORD);

        // ObjectMapper作成
        ObjectMapper objectMapper = new ObjectMapper();
        // CsvMapper作成(囲い文字設定有効化)
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
        // CsvSchema作成
        CsvSchema csvSchema = csvMapper.schemaFor(StudentProfile.class)
        .sortedBy("name", "age","sex","address","school")
        .withQuoteChar(QUOTE_CHAR)
        .withoutHeader();

        // JSON→CSV変換処理
        StudentProfile lineObject = new StudentProfile();
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                // 行データをオブジェクトに変換
                lineObject = objectMapper.readValue(line, StudentProfile.class);
                // オブジェクトをCSV行に変換してStringBuilderにCSV行を追加
                outputFileBuilder.append(csvMapper.writer(csvSchema).writeValueAsString(lineObject));
            } catch (Exception e) {
                logger.warning("Error happen when convert json to csv.");
                throw e;
            }
        }

        // ファイルを指定のバケットにアップロード
        BlobId blobId = BlobId.of(System.getenv("UPLOADBKT"), uploadFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.createFrom(blobInfo, new ByteArrayInputStream(outputFileBuilder.toString().getBytes()));

        logger.info("Success convert json to csv.");
    }

}
