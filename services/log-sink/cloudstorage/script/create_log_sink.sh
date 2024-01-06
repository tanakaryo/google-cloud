#!/bin/bash

# create bucket.
gsutil mb gs://test-log-sink-jp-20240101

# create log-sink to bucket.
gcloud logging sinks create test-sink storage.googleapis.com/test-log-sink-jp-20240101 \
 --log-filter='logName="projects/<project-id>/logs/matched"' --description="Test sink"

# add role to service-account for sink.
 gcloud projects add-iam-policy-binding projectid --member="serviceAccount:service-project-num@gcp-sa-logging.iam.gserviceaccount.com" --role=roles/storage.objectCreator

 # delete sink.
 gcloud logging sinks delete test-sink

 # delete bucket.
 gsutil rm -r gs://test-log-sink-jp-20240101