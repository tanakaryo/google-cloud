package com.myapp.convfl.service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.events.cloud.storage.v1.StorageObjectData;
import com.google.protobuf.util.JsonFormat;
import com.myapp.convfl.factory.FeedTypeFactory;
import com.myapp.convfl.type.FeedType;

import io.cloudevents.CloudEvent;

public class FeedFmtConverterService implements Service<byte[],String> {

    private StorageObjectData storageObjectData;


    @Override
    public String execute(byte[] readItem) throws Exception {
        FeedType feedType;
        StringBuilder outputBuilder;
        // ファイルはLF改行されているため、BufferedReaderで読み取り tryブロックにて処理終了後,Streamを閉じる
        try (BufferedReader bufferedReader = new BufferedReader(new StringReader(new String(readItem)))) {

            // 対象フィードのFeedType生成 フォルダ名: /feed/personal_feed/[FileName].json
            //String[] splitedDataName = StringUtils.split(data.getName(), "/");

            // フィードタイプ取得 magicnumberあり
            //feedType = FeedType.valueOf(new String(splitedDataName[1]).toUpperCase());
            feedType = FeedTypeFactory.getFeedType(storageObjectData.getName());
            // オブジェクトMapperインスタンス生成
            ObjectMapper objectMapper = new ObjectMapper();
            // CSVMapperインスタンス生成 クォーテーションを常時付与する
            CsvMapper csvMapper = new CsvMapper();
            csvMapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true);

            // CSVスキーマインスタンス生成
            CsvSchema csvSchema = csvMapper.schemaFor(feedType.getFeedObjectClass())
                    .sortedBy(feedType.getExifDef().getColumnsOrder())
                    .withQuoteChar(feedType.getExifDef().getColumnQuote())
                    .withoutHeader()
                    .withColumnReordering(true);

            // フィードオブジェクト型取得及びインスタンス生成
            Class<?> clazz = feedType.getFeedObjectClass();
            Object feedObject = clazz.getDeclaredConstructor().newInstance();
            outputBuilder = new StringBuilder();

            // Headerレコード挿入
            outputBuilder.append(feedType.getExifDef().getHeaderRecord());
            // 行読取及びCSV行へ変換
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    feedObject = objectMapper.readValue(line, clazz);
                    outputBuilder.append(csvMapper.writer(csvSchema).writeValueAsString(feedObject));
                } catch (Exception e) {
                    throw e;
                }
            }
        }

        return outputBuilder.toString();
    }

    public void setCloudEvent(CloudEvent event) throws Exception {
        String cloudEventData = new String(event.getData().toBytes(), StandardCharsets.UTF_8);
        StorageObjectData.Builder builder = StorageObjectData.newBuilder();
        JsonFormat.parser().merge(cloudEventData, builder);
        this.storageObjectData = builder.build();
    }
    
}
