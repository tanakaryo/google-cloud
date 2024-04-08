#!/bin/bash

gsutil mb -c standard -l asia-northeast1 gs://testcfneventjp20240129

gsutil mb -c standard -l asia-northeast1 gs://testuploadcfneventjp20240130

gcloud functions deploy feed-fmt-conv-fn \
--gen2 \
--runtime=java17 \
--region=asia-northeast1 \
--service-account=437530287615-compute@developer.gserviceaccount.com \
--source=. \
--entry-point=com.myapp.convfl.function.FeedFmtConverterFn \
--memory=512MB \
--trigger-event-filters="type=google.cloud.storage.object.v1.finalized" \
--trigger-event-filters="bucket=testcfneventjp20240129" \
--set-env-vars="UPLOADBKT=testuploadcfneventjp20240130" \
--set-env-vars="PROJECTID=dataflowtest002"