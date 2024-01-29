package cloudfn;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import cloudfn.elements.StudentProfile;

public class App2 {

    private static final String HEADER = "\"name\",\"age\",\"sex\",\"address\",\"school\"";

    public static void main(String[] args) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId("dataflowtest002").build().getService();

        byte[] content = storage.readAllBytes("testcfneventjp20240129", "test.json");
        BufferedReader reader = new BufferedReader(new StringReader(new String(content)));

        StringBuilder sb = new StringBuilder();
        
        sb.append(HEADER);
        ObjectMapper oMapper = new ObjectMapper();
        StudentProfile student = new StudentProfile();
        CsvMapper mapper = new CsvMapper();
        mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
        CsvSchema schema = mapper.schemaFor(StudentProfile.class)
            .sortedBy("name", "age","sex","address","school")
            .withQuoteChar('"') .withoutHeader();

        String line;
        while ((line = reader.readLine()) != null) {
            // Object Mapper convert Json to Object.
            try {
                student = oMapper.readValue(line, StudentProfile.class);
            } catch (Exception e) {
                System.err.println("error");
            }

            // CSV Mapper conver Object to CSV.
            try {
                sb.append(mapper.writer(schema).writeValueAsString(student));
            } catch (Exception e) {
                System.err.println("error");
            }
        }

        String uploadFileName = StringUtils.replace("test.json", ".json", ".csv");
        BlobId blobId = BlobId.of("testcfneventjp20240129", uploadFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.createFrom(blobInfo, new ByteArrayInputStream(sb.toString().getBytes()));
    }

}
