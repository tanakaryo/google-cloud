#!/bin/bash

# create dataset.
bq --location=asia-northeast1 mk \
   --dataset \
   --description="for log-sink" \
   project-id:testlogsink

# create sink.
gcloud logging sinks create my-sink bigquery.googleapis.com/projects/project-id/datasets/testlogsink \
  --log-filter='logName="projects/project-id/logs/matched"' --description="test sink"
# create sink(no-filter).
gcloud logging sinks create my-sink bigquery.googleapis.com/projects/project-id/datasets/testlogsink \
  --description="test sink"

# add iam-policy.
 gcloud projects add-iam-policy-binding project-id --member="serviceAccount:service-projectnum@gcp-sa-logging.iam.gserviceaccount.com" --role=roles/bigquery.dataEditor

 # delete sink.
 gcloud logging sinks delete my-sink