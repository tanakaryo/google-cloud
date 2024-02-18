package com.myapp.convfl.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.events.cloud.storage.v1.StorageObjectData;
import com.google.protobuf.util.JsonFormat;
import com.myapp.convfl.constants.CommonConstants;

import io.cloudevents.CloudEvent;

public final class CloudStorageUtility {

    public static byte[] download(CloudEvent event) throws Exception {
        String cloudEventData = new String(event.getData().toBytes(), StandardCharsets.UTF_8);
        StorageObjectData.Builder builder = StorageObjectData.newBuilder();
        JsonFormat.parser().merge(cloudEventData, builder);
        StorageObjectData data = builder.build();

        // Storageオブジェクト作成
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(System.getenv("PROJECTID"))
                .build().getService();

        // 対象ファイルダウンロード
        return storage.readAllBytes(data.getBucket(), data.getName());
    }

    public static boolean upload(CloudEvent event, String uploadContent) throws Exception {
        String cloudEventData = new String(event.getData().toBytes(), StandardCharsets.UTF_8);
        StorageObjectData.Builder builder = StorageObjectData.newBuilder();
        JsonFormat.parser().merge(cloudEventData, builder);
        StorageObjectData data = builder.build();

        // Storageオブジェクト作成
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(System.getenv("PROJECTID"))
                .build().getService();

        // ファイル名を作成
        String uploadFileName = StringUtils.replace(data.getName(), CommonConstants.FILE_EXT_JSON,
                CommonConstants.FILE_EXT_CSV);

        try {
        // ファイルを指定のバケットにアップロード
        BlobId blobId = BlobId.of(System.getenv("UPLOADBKT"), uploadFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.createFrom(blobInfo, new ByteArrayInputStream(uploadContent.getBytes()));
        } catch (IOException e) {
            // createFromメソッドの例外はアップロード失敗と見做してfalse
            return false;
        } catch (Exception ex) {
            throw ex;
        }

        // アップロード処理成功
        return true;
    }
}
