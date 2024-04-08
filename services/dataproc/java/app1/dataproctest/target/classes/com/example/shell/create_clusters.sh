#!/bin/bash

# create dataproc clusters 
gcloud dataproc clusters create example-cluster --region=asia-northeast1 --zone=asia-northeast1-a

# delete
gcloud dataproc clusters delete example-cluster --region=asia-northeast1

# create tmp-bucket
export TEMP_BUCKET=stg-bucket-20240219-jp
gsutil mb -l asia-northeast1 gs://${TEMP_BUCKET}

# create input-bucket.
export INPUT_BUCKET=stg-input-bucket-20240219-jp
gsutil mb -l asia-northeast1 gs://${INPUT_BUCKET}

# upload file
gsutil cp /Users/yoichiikegawa/Documents/Github/google-cloud/services/dataproc/java/app1/dataproctest/src/main/resources/data/sample.csv gs://${INPUT_BUCKET}

# set default subnet
gcloud compute networks subnets update default --region=asia-northeast1 --enable-private-ip-google-access

# export params.
export PROJECT_ID=aspf-jp-test
export REGION=asia-northeast1
export TEMPLATE_VERSION=latest
export CLOUD_STORAGE_PATH=gs://$INPUT_BUCKET/sample.csv
export FORMAT=csv
export DATASET=my_dataset
export TABLE=my_table
export TEMPVIEW=my_data
export SQL_QUERY="select item_id, cast(round(sales, 0) as int) as sales, cast(datetime as date) as date from global_temp.my_data"

gcloud dataproc batches submit spark \
    --class=com.google.cloud.dataproc.templates.main.DataProcTemplate \
    --version="1.1" \
    --project="$PROJECT_ID" \
    --region="$REGION" \
    --jars="gs://dataproc-templates-binaries/$TEMPLATE_VERSION/java/dataproc-templates.jar" \
    -- --template=GCSTOBIGQUERY \
    --templateProperty project.id="$PROJECT_ID" \
    --templateProperty gcs.bigquery.input.location="$CLOUD_STORAGE_PATH" \
    --templateProperty gcs.bigquery.input.format="$FORMAT" \
    --templateProperty gcs.bigquery.output.dataset="$DATASET" \
    --templateProperty gcs.bigquery.output.table="$TABLE" \
    --templateProperty gcs.bigquery.temp.bucket.name="$TEMP_BUCKET" \
    --templateProperty gcs.bigquery.temp.table="$TEMPVIEW" \
    --templateProperty gcs.bigquery.temp.query="$SQL_QUERY"