package com.example.demo.common.parse;

import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.exceptions.BatchExecutionException;
import com.example.demo.common.utils.SystemUtils;

public class ArgumentParserTest {

    @Test
    @DisplayName("parseArgumentAndBuildJobParameter 正常系テスト1: 日付フォーマットの場合(yyyy/MM/dd)")
    public void test01() throws Exception {
        try (MockedStatic<SystemUtils> systemUtils = Mockito.mockStatic(SystemUtils.class)) {
            
            systemUtils.when(() -> SystemUtils.getEnv(BatchConst.KEY_CONTRACTOR)).thenReturn("DUMMY");
            systemUtils.when(() -> SystemUtils.getEnv(BatchConst.KEY_AWS_ROLE_ARN)).thenReturn("arn::dummy");
            systemUtils.when(() -> SystemUtils.getEnv(BatchConst.KEY_AWS_ROLE_SESSION_NAME)).thenReturn("dummy_session");
            systemUtils.when(() -> SystemUtils.getEnv(BatchConst.KEY_AWS_S3_BUCKET_NAME)).thenReturn("dummy_bucket");
            systemUtils.when(() -> SystemUtils.getEnv(BatchConst.KEY_AWS_WEB_IDENTITY_TOKEN_FILE)).thenReturn("/mnt/creds");
            systemUtils.when(() -> SystemUtils.getEnv(BatchConst.KEY_PROJECT_ID)).thenReturn("dummy-proj");
            systemUtils.when(() -> SystemUtils.getEnv(BatchConst.KEY_GCS_BUCKET_NAME)).thenReturn("dummy-gcs-bucket");
            
            
            String[] args = { "batch_exe_date=2025/05/28" };
            Properties actual = ArgumentParser.parseArgumentAndBuildJobParameter(args);
    
            Assertions.assertEquals("2025/05/28", actual.getProperty(BatchConst.KEY_JOB_PARAMETER_BATCH_EXE_DATE));
            Assertions.assertEquals("2025", actual.getProperty(BatchConst.KEY_YEAR));
            Assertions.assertEquals("05", actual.getProperty(BatchConst.KEY_MONTH));
            Assertions.assertEquals("28", actual.getProperty(BatchConst.KEY_DAY));
            Assertions.assertEquals("DUMMY", actual.getProperty(BatchConst.KEY_CONTRACTOR));
            Assertions.assertEquals("arn::dummy", actual.getProperty(BatchConst.KEY_AWS_ROLE_ARN));
            Assertions.assertEquals("dummy_session", actual.getProperty(BatchConst.KEY_AWS_ROLE_SESSION_NAME));
            Assertions.assertEquals("dummy_bucket", actual.getProperty(BatchConst.KEY_AWS_S3_BUCKET_NAME));
            Assertions.assertEquals("/mnt/creds", actual.getProperty(BatchConst.KEY_AWS_WEB_IDENTITY_TOKEN_FILE));
            Assertions.assertEquals("https://sts.amazonaws.com/", actual.getProperty(BatchConst.KEY_AWS_TARGET_AUDIENCE));
            Assertions.assertEquals("https://s3-ap-northeast-1.amazonaws.com", actual.getProperty(BatchConst.KEY_AWS_ENDPOINT_URL));
            Assertions.assertEquals("dummy-proj", actual.getProperty(BatchConst.KEY_PROJECT_ID));
            Assertions.assertEquals("dummy-gcs-bucket", actual.getProperty(BatchConst.KEY_GCS_BUCKET_NAME));
        }
    }

    @Test
    @DisplayName("parseArgumentAndBuildJobParameter 異常系テスト2: 日付フォーマットの場合(yyyy-MM-dd)")
    public void test02() throws Exception {
            
            
            String[] args = { "batch_exe_date=2025-05-28" };
            Exception actual = Assertions.assertThrows(BatchExecutionException.class, () -> ArgumentParser.parseArgumentAndBuildJobParameter(args));
            Assertions.assertEquals("【ERR】引数(バッチ処理日付)が不正な値です。(キー情報: 2025-05-28)", actual.getMessage());

        }
    }

