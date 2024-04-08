#!/bin/bash

gsutil mb -c standard -l asia-northeast1 gs://test-jp-input-bucket-20240219
gsutil mb -c standard -l asia-northeast1 gs://out-personal-feed-20240219

# cloud storage bucket driven
gcloud functions deploy feed-fmt-conv-fn-multi-output \
--gen2 \
--runtime=java17 \
--region=asia-northeast1 \
--service-account=1009205782858-compute@developer.gserviceaccount.com \
--source=. \
--entry-point=com.myapp.convfl.function.FeedFmtConverterFn \
--memory=512MB \
--trigger-event-filters="type=google.cloud.storage.object.v1.finalized" \
--trigger-event-filters="bucket=test-jp-input-bucket-20240219" \
--set-env-vars="PROJECT_ID=aspf-jp-test" \
--set-env-vars="OUT_PERSONAL_FEED_BUCKET=out-personal-feed-20240219"


# audit-log event driven
gcloud functions deploy feed-fmt-conv-fn-multi-output \
--gen2 \
--runtime=java17 \
--region=asia-northeast1 \
--service-account=1009205782858-compute@developer.gserviceaccount.com \
--source=. \
--entry-point=com.myapp.convfl.function.FeedFmtConverterFn \
--memory=512MB \
--trigger-event-filters="type=google.cloud.audit.log.v1.written" \
--trigger-event-filters="serviceName=storage.googleapis.com" \
--trigger-event-filters="methodName=google.cloud.storage.object.v1.finalized" \
--trigger-event-filters-path-pattern="resourceName=projects/_/buckets/test-jp-input-bucket-20240219/objects/FEED_*.json" \
--set-env-vars="PROJECT_ID=aspf-jp-test" \
--set-env-vars="OUT_PERSONAL_FEED_BUCKET=out-personal-feed-20240219" 

--trigger-event-filters="bucket=test-jp-input-bucket-20240219" \