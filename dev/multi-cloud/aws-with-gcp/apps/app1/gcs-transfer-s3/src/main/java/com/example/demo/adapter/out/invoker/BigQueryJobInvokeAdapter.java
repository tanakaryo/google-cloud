package com.example.demo.adapter.out.invoker;

import org.threeten.bp.Duration;

import com.example.demo.application.port.out.BigQueryJobPort;
import com.example.demo.common.utils.BigQueryUtils;
import com.google.cloud.RetryOption;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;

public final class BigQueryJobInvokeAdapter implements BigQueryJobPort {

    private static final int RETRY_DURATION = 1;

    private static final int TOTAL_TIMEOUT = 1;

    @Override
    public Job invoke(String query) throws Exception {
        
        BigQuery bigquery = BigQueryUtils.getService();
        QueryJobConfiguration configuration = BigQueryUtils.buildConfig(query);
        JobInfo jobInfo = BigQueryUtils.buildInfo(configuration);
        Job job = bigquery.create(jobInfo);
        return job.waitFor(RetryOption.initialRetryDelay(Duration.ofSeconds(RETRY_DURATION)),
        RetryOption.totalTimeout(Duration.ofMinutes(TOTAL_TIMEOUT)));
    }
}
