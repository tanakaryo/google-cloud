package com.example.demo.adapter.out.persistence;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;

import com.example.demo.application.port.out.CreateTransferObjectPort;
import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.exceptions.BatchExecutionException;
import com.example.demo.common.helper.ContentsHelper;
import com.example.demo.common.type.MessageTypes;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;

import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleWithWebIdentityCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleWithWebIdentityRequest;

public class S3ObjectPersistenceAdapter implements CreateTransferObjectPort {

    private String targetAudience;

    private String roleArn;

    private String roleSessionName;

    private S3Client s3Client;

    private String bucketName;

    private String fileNamePrefix;

    private String contractorName;

    private List<String> fileNameList;

    @Override
    public void initialize(Properties batchParameter) throws Exception {

        this.targetAudience = batchParameter.getProperty(BatchConst.KEY_AWS_TARGET_AUDIENCE);
        this.roleArn = batchParameter.getProperty(BatchConst.KEY_AWS_ROLE_ARN);
        this.roleSessionName = batchParameter.getProperty(BatchConst.KEY_AWS_ROLE_SESSION_NAME);
        this.bucketName = batchParameter.getProperty(BatchConst.KEY_AWS_S3_BUCKET_NAME);
        this.fileNamePrefix = batchParameter.getProperty(BatchConst.KEY_YEAR)
                + batchParameter.getProperty(BatchConst.KEY_MONTH)
                + batchParameter.getProperty(BatchConst.KEY_DAY);
        this.contractorName = batchParameter.getProperty(BatchConst.KEY_CONTRACTOR);
        this.fileNameList = new ArrayList<>();

        Region region = Region.AP_NORTHEAST_1;

        GoogleCredentials googleCredentials = GoogleCredentials.getApplicationDefault();
        IdTokenCredentials idTokenCredentials = IdTokenCredentials.newBuilder()
                .setIdTokenProvider((IdTokenProvider) googleCredentials)
                .setTargetAudience(targetAudience)
                .setOptions(Arrays.asList(IdTokenProvider.Option.FORMAT_FULL, IdTokenProvider.Option.LICENSES_TRUE))
                .build();

        String token = idTokenCredentials.refreshAccessToken().getTokenValue();

        File file = new File(batchParameter.getProperty(BatchConst.KEY_AWS_WEB_IDENTITY_TOKEN_FILE));
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(token);
        fileWriter.close();

        StsClient stsClient = StsClient.builder()
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(WebIdentityTokenFileCredentialsProvider.create())
                .build();

        AssumeRoleWithWebIdentityRequest roleRequest = AssumeRoleWithWebIdentityRequest.builder()
                .webIdentityToken(token)
                .roleArn(roleArn)
                .roleSessionName(roleSessionName)
                .build();

        StsAssumeRoleWithWebIdentityCredentialsProvider provider = StsAssumeRoleWithWebIdentityCredentialsProvider
                .builder()
                .stsClient(stsClient)
                .refreshRequest(roleRequest)
                .build();

        this.s3Client = S3Client.builder()
                .credentialsProvider(provider)
                .region(region)
                .build();
    }

    @Override
    public void putObject(String jsonData, String vehicleId) throws Exception {
        String fileName = this.fileNamePrefix + "_" + this.contractorName + "_ev_charging_data_history_" + vehicleId
                + ".json";
        this.fileNameList.add(fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(jsonData.getBytes()));
    }

    @Override
    public int putFlagObject() throws Exception {
        if (CollectionUtils.isEmpty(fileNameList)) {
            return fileNameList.size();
        }

        String flagFileName = "transfer_process_completed_" + fileNamePrefix + ".json";

        String requestBody = ContentsHelper.toFlagFileContent(fileNameList);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(flagFileName)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(requestBody.getBytes()));
        } catch (Exception e) {
            throw new BatchExecutionException(
                    MessageTypes.ERR004.getMessage(this.contractorName, this.bucketName),
                    e);
        }

        return fileNameList.size();
    }

    @Override
    public void close() throws Exception {
        this.s3Client.close();
    }

}
