#!/bin/bash

# enable APIs.
gcloud services enable dataflow.googleapis.com compute.googleapis.com logging.googleapis.com storage-component.googleapis.com storage-api.googleapis.com pubsub.googleapis.com cloudresourcemanager.googleapis.com cloudscheduler.googleapis.com

# create saccount.
gcloud iam service-accounts create dataflowtester1

# enable roles to saccount.
gcloud projects add-iam-policy-binding projectid --member="serviceAccount:dataflowtester1@projectid.iam.gserviceaccount.com" --role=roles/dataflow.worker
gcloud projects add-iam-policy-binding projectid --member="serviceAccount:dataflowtester1@projectid.iam.gserviceaccount.com" --role=roles/storage.objectAdmin
gcloud projects add-iam-policy-binding projectid --member="serviceAccount:dataflowtester1@projectid.iam.gserviceaccount.com" --role=roles/pubsub.admin

# enable uaccount.
gcloud iam service-accounts add-iam-policy-binding dataflowtester1@projectid.iam.gserviceaccount.com --member="user:youraddress@gmail.com" --role=roles/iam.serviceAccountUser

# export params.
export BUCKET_NAME=dataflow20240101bkt
export PROJECT_ID=$(gcloud config get-value project)
export TOPIC_ID=dataflow-topic
export REGION=asia-northeast1
export SERVICE_ACCOUNT=dataflowtester1@projectid.iam.gserviceaccount.com

# create gcs bucket.
gsutil mb -c standard -l asia-northeast1 gs://$BUCKET_NAME 

# create pubsub topic.
gcloud pubsub topics create $TOPIC_ID

# deploy code to dataflow.
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
    --serviceAccount=$SERVICE_ACCOUNT"
