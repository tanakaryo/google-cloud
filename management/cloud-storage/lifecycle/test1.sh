#!/bin/bash

PRJ="nifty-gasket-399805"
BKT_NAME="bkt-from-gcloud-jp-20231009"

## create new bucket.
# gcloud storage buckets create gs://${BKT_NAME} \
# --project=${PRJ} \
# --default-storage-class="COLDLINE" \
# --location="asia-northeast1" \
# --uniform-bucket-level-access
gsutil mb \
-p "${PRJ}" \
-c "COLDLINE" \
-l "asia-northeast1" \
-b on gs://${BKT_NAME} 

## add lifecycle-json
# gcloud storage buckets update gs://${BKT_NAME} \
# --lifecycle-file="./lifecycel/test1.json"
gsutil lifecycle set ./lifecycle/test1.json gs://${BKT_NAME}

## add object
gsutil cp ./upload-items/test1.txt gs://${BKT_NAME}

## delete bucket.
#gcloud storage rm --recursive gs://${BKT_NAME}
gsutil rm -r gs://${BKT_NAME}