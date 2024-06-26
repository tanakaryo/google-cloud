package com.myapp.convfl.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.cloud.functions.CloudEventsFunction;
import com.myapp.convfl.service.FeedFmtConverterService;
import com.myapp.convfl.type.FeedType;
import com.myapp.convfl.util.CloudEventUtility;
import com.myapp.convfl.util.CloudStorageUtility;

import io.cloudevents.CloudEvent;

public class FeedFmtConverterFn implements CloudEventsFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedFmtConverterFn.class);

    @Override
    public void accept(CloudEvent event) throws Exception {

        // CloudEventチェック
        boolean hasPayload = CloudEventUtility.hasPayload(event, LOGGER);
        if (!hasPayload) {
            LOGGER.info("No data found in cloud event payload or Not convertiable type of feed.");
            return;
        }

        // ファイルコンテンツダウンロード
        byte[] fileContent = CloudStorageUtility.download(event);

        // フォーマット変換実行
        FeedFmtConverterService service = new FeedFmtConverterService();
        service.setCloudEvent(event);
        String csvData = service.execute(fileContent);
        FeedType feedType = service.getFeedType();

        // アップロード処理
        boolean isSuccess = CloudStorageUtility.upload(event, csvData, feedType);
        if (!isSuccess) {
            LOGGER.error("Failed to Format Convert process.");
            return;
        }

        LOGGER.info("Success to convert json to csv.");
    }

}
