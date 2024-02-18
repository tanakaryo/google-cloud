package com.myapp.convfl.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.myapp.convfl.factory.FeedTypeFactory;
import com.myapp.convfl.type.FeedType;

public class App {
    public static void main(String[] args) throws Exception {

      try(BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/Users/yoichiikegawa/Documents/Github/software/java/feature1/app1/conv-file/src/main/resources/data/input/feed/personal_feed/FEED_20240210.json"))) ) {

        //FeedType fileType = FeedType.valueOf(new String("personal_feed").toUpperCase());
        FeedType feedType = FeedTypeFactory.getFeedType("/feed/personal_feed/P_DATA.json");
        ObjectMapper objectMapper = new ObjectMapper();
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);

        CsvSchema csvSchema = csvMapper.schemaFor(feedType.getFeedObjectClass())
        .sortedBy(feedType.getExifDef().getColumnsOrder())
        .withQuoteChar(feedType.getExifDef().getColumnQuote())
        .withoutHeader()
        .withColumnReordering(true);

        Class<?> clazz = feedType.getFeedObjectClass();
        Object feedObject = clazz.getDeclaredConstructor().newInstance();
        StringBuilder outputBuilder = new StringBuilder();

        outputBuilder.append(feedType.getExifDef().getHeaderRecord());
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            try {
                feedObject = objectMapper.readValue(line, clazz);
                outputBuilder.append(csvMapper.writer(csvSchema).writeValueAsString(feedObject));
            } catch (Exception e) {
                throw e;
            }
        }

        System.out.println(outputBuilder.toString());
      };
      


    }
}
