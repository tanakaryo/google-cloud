package com.example.demo.common.utils;

import java.util.UUID;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;

public final class BigQueryUtils {

    private BigQueryUtils() {
    }


    public static BigQuery getService() {
        return BigQueryOptions.getDefaultInstance().getService();
    }

    public static QueryJobConfiguration buildConfig(String query) {
        return QueryJobConfiguration.newBuilder(query).setUseLegacySql(false).build();
    }

    public static JobInfo buildInfo(QueryJobConfiguration configuration) {
        JobId jobId = JobId.of(UUID.randomUUID().toString());
        return JobInfo.newBuilder(configuration).setJobId(jobId).build();
    }
}
