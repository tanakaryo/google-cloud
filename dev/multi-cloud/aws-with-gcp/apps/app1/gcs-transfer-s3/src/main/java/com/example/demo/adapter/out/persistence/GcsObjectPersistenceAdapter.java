package com.example.demo.adapter.out.persistence;

import java.util.Properties;

import com.example.demo.application.port.out.CreateBackupObjectPort;
import com.example.demo.common.constants.BatchConst;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class GcsObjectPersistenceAdapter implements CreateBackupObjectPort {

    private String bucketName;

    private String prefixTimestamp;

    private Storage storage;

    @Override
    public void initialize(Properties batchParameter) throws Exception {
        String strProjectId = batchParameter.getProperty(BatchConst.KEY_PROJECT_ID);
        this.bucketName = batchParameter.getProperty(BatchConst.KEY_GCS_BUCKET_NAME);
        this.prefixTimestamp = batchParameter.getProperty(BatchConst.KEY_YEAR)
                + batchParameter.getProperty(BatchConst.KEY_MONTH)
                + batchParameter.getProperty(BatchConst.KEY_DAY);

        this.storage = StorageOptions.newBuilder().setProjectId(strProjectId).build().getService();
    }

    @Override
    public void putObject(String jsonData, String vehicleId) throws Exception {
        String objectName = "vehicle_history_" + vehicleId + "_" + prefixTimestamp + ".json";

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, jsonData.getBytes());
    }

    @Override
    public void close() throws Exception {
        this.storage.close();
    }

}
