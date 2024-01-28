#!/bin/bash

gsutil mb -c standard -l asia-northeast1 gs://jp20240120dftestbkt

mvn compile exec:java \
  -Dexec.mainClass=dataflowtest.PubSubToGcs \
  -Dexec.cleanupDaemonThreads=false \
  -Dexec.args=" \
    --jobName=dftest \
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