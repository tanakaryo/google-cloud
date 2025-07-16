package com.example.demo.common.parse;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.example.demo.common.constants.BatchConst;
import com.example.demo.common.utils.DateUtils;
import com.example.demo.common.utils.SystemUtils;


public final class ArgumentParser {

    private static final Logger LOGGER = Logger.getLogger(ArgumentParser.class.getName());

    public static final Properties parseArgumentAndBuildJobParameter(String[] args) throws Exception {


        Properties properties = DateUtils.toPropertiesAndSetDefaultValue(Arrays.asList(args));


        String batchExeDate = (String)properties.get(BatchConst.KEY_JOB_PARAMETER_BATCH_EXE_DATE);
        DateUtils.validBatchExeDate(batchExeDate);

        //extract to year, month, day
        String[] extractYMD = StringUtils.split(properties.getProperty(BatchConst.KEY_JOB_PARAMETER_BATCH_EXE_DATE), BatchConst.CHAR_SLASH);
        properties.put(BatchConst.KEY_YEAR, extractYMD[0]);
        properties.put(BatchConst.KEY_MONTH, extractYMD[1]);
        properties.put(BatchConst.KEY_DAY, extractYMD[2]);

        // set System Env to JobParameter
        properties.put(BatchConst.KEY_CONTRACTOR, SystemUtils.getEnv(BatchConst.KEY_CONTRACTOR));
        properties.put(BatchConst.KEY_AWS_ROLE_ARN, SystemUtils.getEnv(BatchConst.KEY_AWS_ROLE_ARN));
        properties.put(BatchConst.KEY_AWS_ROLE_SESSION_NAME, SystemUtils.getEnv(BatchConst.KEY_AWS_ROLE_SESSION_NAME));
        properties.put(BatchConst.KEY_AWS_S3_BUCKET_NAME, SystemUtils.getEnv(BatchConst.KEY_AWS_S3_BUCKET_NAME));
        properties.put(BatchConst.KEY_AWS_WEB_IDENTITY_TOKEN_FILE, SystemUtils.getEnv(BatchConst.KEY_AWS_WEB_IDENTITY_TOKEN_FILE));
        properties.put(BatchConst.KEY_AWS_TARGET_AUDIENCE, "https://sts.amazonaws.com/");
        properties.put(BatchConst.KEY_AWS_ENDPOINT_URL, "https://s3-ap-northeast-1.amazonaws.com");
        properties.put(BatchConst.KEY_PROJECT_ID, SystemUtils.getEnv(BatchConst.KEY_PROJECT_ID));
        properties.put(BatchConst.KEY_GCS_BUCKET_NAME, SystemUtils.getEnv(BatchConst.KEY_GCS_BUCKET_NAME));

        LOGGER.info("------------- Batch Parameters -------------");
        LOGGER.info("BATCH EXE DATE: " + properties.getProperty(BatchConst.KEY_JOB_PARAMETER_BATCH_EXE_DATE));
        LOGGER.info("CONTRACTOR : " +  properties.get(BatchConst.KEY_CONTRACTOR));
        LOGGER.info("AWS ROLE ARN : " + properties.get(BatchConst.KEY_AWS_ROLE_ARN));
        LOGGER.info("AWS ROLE SESSION NAME : " + properties.get(BatchConst.KEY_AWS_ROLE_SESSION_NAME));
        LOGGER.info("AWS S3 BUCKET NAME : " + properties.get(BatchConst.KEY_AWS_S3_BUCKET_NAME));
        LOGGER.info("AWS WEB IDENTITY TOKEN FILE : " + properties.get(BatchConst.KEY_AWS_WEB_IDENTITY_TOKEN_FILE));
        LOGGER.info("AWS TARGET AUDIENCE : " + properties.get(BatchConst.KEY_AWS_TARGET_AUDIENCE));
        LOGGER.info("AWS ENDPOINT URL : " + properties.get(BatchConst.KEY_AWS_ENDPOINT_URL));
        LOGGER.info("PROJECT ID : " + properties.get(BatchConst.KEY_PROJECT_ID));
        LOGGER.info("GCS BUCKET NAME : " + properties.get(BatchConst.KEY_GCS_BUCKET_NAME));
        
        return properties;
    }
}
