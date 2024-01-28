#!/bin/bash

mvn compile exec:java \
  -Dexec.mainClass=dataflowtest.pipeline.FilteringPipeline2 \
  -Dexec.cleanupDaemonThreads=false \
  -Dexec.args=" \
    --jobName=dftest1 \
    --project=$PROJECT_ID \
    --region=asia-northeast1 \
    --workerZone=asia-northeast1-a \
    --workerMachineType=e2-medium \
    --inputTopic=projects/$PROJECT_ID/topics/$TOPIC_ID \
    --output=gs://$BUCKET_NAME/samples/output \
    --gcpTempLocation=gs://$BUCKET_NAME/temp \
    --runner=DataflowRunner \
    --windowSize=2 \
    --serviceAccount=$SERVICE_ACCOUNT \
    --bucketName=gs://jp20240120dftestbkt"