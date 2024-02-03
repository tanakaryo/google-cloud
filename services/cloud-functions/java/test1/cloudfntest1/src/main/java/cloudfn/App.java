package cloudfn;

import java.io.File;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {

        String jsn = "[{\"name\": \"mosh\", \"job\" : \"Officer\"},{\"name\": \"pp\", \"job\" : \"oo\"}]";
        JsonNode jsonTree = new ObjectMapper().readTree(jsn);
        Builder csvBuilder = CsvSchema.builder();
        JsonNode firstObject = jsonTree.elements().next();
        firstObject.fieldNames().forEachRemaining(fieldName -> {csvBuilder.addColumn(fieldName);});
        CsvSchema csvSchema = csvBuilder.build().withQuoteChar('"').withHeader();

        String csv = "";
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);
        csvMapper.writerFor(JsonNode.class)
        .with(csvSchema)
        .writeValue(new File("test.csv"), jsonTree);

        String jsn2 = "{\"name\": \"mosh\", \"job\" : \" Officer\"},{\\\"name\\\": \\\"henrig\\\", \\\"job\\\" : \\\" pop\\\"}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsn2);

        System.out.println(root.get(0).toString());
        System.out.println(root.get(1).toString());


    }
}
