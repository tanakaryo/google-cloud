package example;

import java.util.UUID;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;

public class BqReadInsert {
    public static void main(String[] args) throws Exception {
        BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();
        QueryJobConfiguration queryConfig =
        QueryJobConfiguration.newBuilder(
            "INSERT INTO `test_dataset.for_ins_table` SELECT * FROM `my_dataset_listing.test_table1`"
        ).setUseLegacySql(false)
        .build();

        JobId jobId = JobId.of(UUID.randomUUID().toString());
        Job queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

        queryJob = queryJob.waitFor();

        if (queryJob == null) {
            throw  new RuntimeException("Job is failed.");
        } else if (queryJob.getStatus().getError() != null) {
            throw new RuntimeException("Error :" + queryJob.getStatus().getError().toString());
        }
        System.out.println("SUCCESS");
    }
}
