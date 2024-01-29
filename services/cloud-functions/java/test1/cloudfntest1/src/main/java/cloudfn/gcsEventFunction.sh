#!/bin/bash

gsutil mb -c standard -l asia-northeast1 gs://testcfneventjp20240129

gcloud functions deploy java-finalize-function \
--gen2 \
--runtime=java17 \
--region=asia-northeast1 \
--source=. \
--entry-point=cloudfn.GCSEventFunction \
--memory=512MB \
--trigger-event-filters="type=google.cloud.storage.object.v1.finalized" \
--trigger-event-filters="bucket=testcfneventjp20240129"