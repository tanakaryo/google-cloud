#!/bin/bash

gsutil mb -c standard -l asia-northeast1 gs://jp20240122dftestbktsub

mvn compile exec:java \
  -Dexec.mainClass=dataflowtest.pipeline.ParallelProcessPipeline \
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
    --windowSizeMain=1 \
    --windowSizeSub=2 \
    --shardNumMain=3 \
    --shardNumSub=5 \
    --serviceAccount=$SERVICE_ACCOUNT \
    --bucketMain=gs://jp20240120dftestbkt \
    --bucketSub=gs://jp20240122dftestbktsub" 
