--image asia-northeast1-docker.pkg.dev/aspf-jp-test/common-file-transfer-repo:latest
gcloud run jobs deploy job-quickstart \
    --image asia-northeast1-docker.pkg.dev/aspf-jp-test/common-file-transfer-repo:latest \
    --tasks 1 \
    --set-env-vars SLEEP_MS=10000 \
    --set-env-vars FAIL_RATE=0.1 \
    --set-env-vars CONTRACTOR=PMJ \
    --set-env-vars AWS_WEB_IDENTITY_TOKEN_FILE=/mnt/my-volume/awscreds \
    --set-env-vars AWS_ROLE_ARN=arn:aws:iam::999999:role/aws-allow-gcp-access \
    --set-env-vars AWS_ROLE_SESSION_NAME=test_session \
    --set-env-vars AWS_S3_BUCKET_NAME=my-aws-bucket-test-jp-20250410 \
    --set-env-vars PROJECT_ID=aspf-jp-test \
    --set-env-vars GCS_BUCKET_NAME=common-file-transfer-private-test-bucket \
    --add-volume name=volume-mnt,type=in-memory,size-limit=1Mi \
    --add-volume-mount volume=volume-mnt,mount-path=/mnt/my-volume \
    --max-retries 5 \
    --region asia-northeast1 \
    --project=aspf-jp-test \
    --network=vpc-1 \
    --subnet=subnet-1 \
    --vpc-egress=all-traffic


gcloud run jobs deploy job-quickstart \
    --source . \
    --tasks 1 \
    --set-env-vars SLEEP_MS=10000 \
    --set-env-vars FAIL_RATE=0.1 \
    --set-env-vars CONTRACTOR=PMJ \
    --set-env-vars AWS_WEB_IDENTITY_TOKEN_FILE=/mnt/my-volume/awscreds \
    --set-env-vars AWS_ROLE_ARN=arn:aws:iam::99999:role/aws-allow-gcp-access \
    --set-env-vars AWS_ROLE_SESSION_NAME=test_session \
    --set-env-vars AWS_S3_BUCKET_NAME=my-aws-bucket-test-jp-20250410 \
    --set-env-vars PROJECT_ID=aspf-jp-test \
    --set-env-vars GCS_BUCKET_NAME=common-file-transfer-private-test-bucket \
    --add-volume name=volume-mnt,type=in-memory,size-limit=1Mi \
    --add-volume-mount volume=volume-mnt,mount-path=/mnt/my-volume \
    --max-retries 1 \
    --cpu=1 \
    --memory=1Gi \
    --region asia-northeast1 \
    --project=aspf-jp-test \
    --network=vpc-1 \
    --subnet=subnet-1 \
    --vpc-egress=all-traffic

gcloud run jobs deploy job-quickstart \
    --source . \
    --tasks 1 \
    --set-env-vars SLEEP_MS=10000 \
    --set-env-vars FAIL_RATE=0.1 \
    --set-env-vars CONTRACTOR=PMJ \
    --set-env-vars AWS_WEB_IDENTITY_TOKEN_FILE=/mnt/my-volume/awscreds \
    --set-env-vars AWS_ROLE_ARN=arn:aws:iam::99999:role/aws-allow-gcp-access \
    --set-env-vars AWS_ROLE_SESSION_NAME=test_session \
    --set-env-vars AWS_S3_BUCKET_NAME=my-aws-bucket-test-jp-20250410 \
    --set-env-vars PROJECT_ID=aspf-jp-test \
    --set-env-vars GCS_BUCKET_NAME=common-file-transfer-private-test-bucket \
    --add-volume name=volume-mnt,type=in-memory,size-limit=1Mi \
    --add-volume-mount volume=volume-mnt,mount-path=/mnt/my-volume \
    --max-retries 5 \
    --region asia-northeast1 \
    --project=aspf-jp-test \
    --network=vpc-1 \
    --subnet=subnet-1 \
    --vpc-egress=all-traffic

gcloud run jobs deploy job-quickstart \
    --source . \
    --tasks 1 \
    --set-env-vars SLEEP_MS=10000 \
    --set-env-vars FAIL_RATE=0.1 \
    --set-env-vars CONTRACTOR=PMJ \
    --set-env-vars AWS_WEB_IDENTITY_TOKEN_FILE=/mnt/my-volume/awscreds \
    --set-env-vars AWS_ROLE_ARN=arn:aws:iam::99999:role/aws-allow-gcp-access \
    --set-env-vars AWS_ROLE_SESSION_NAME=test_session \
    --set-env-vars PROJECT_ID=aspf-jp-test \
    --set-env-vars GCS_BUCKET_NAME=vehicle_history_bucket_for_buck_up_20250501 \
    --add-volume name=volume-mnt,type=in-memory,size-limit=1Mi \
    --add-volume-mount volume=volume-mnt,mount-path=/mnt/my-volume \
    --max-retries 5 \
    --region asia-northeast1 \
    --project=aspf-jp-test

gcloud run jobs deploy job-quickstart \
    --source . \
    --tasks 1 \
    --set-env-vars SLEEP_MS=10000 \
    --set-env-vars FAIL_RATE=0.1 \
    --set-env-vars CONTRACTOR=PMJ \
    --set-env-vars AWS_WEB_IDENTITY_TOKEN_FILE=/mnt/my-volume/awscreds \
    --set-env-vars AWS_ROLE_ARN=arn:aws:iam::99999:role/aws-allow-gcp-access \
    --set-env-vars AWS_ROLE_SESSION_NAME=test_session \
    --add-volume name=volume-mnt,type=in-memory,size-limit=1Mi \
    --add-volume-mount volume=volume-mnt,mount-path=/mnt/my-volume \
    --max-retries 5 \
    --region asia-northeast1 \
    --project=aspf-jp-test


gcloud run jobs update job-quickstart \
--add-volume name=volume-mnt,type=cloud-storage,bucket=cloudrunjobs-volume-mnt-20250424 \
--add-volume-mount volume=volume-mnt,mount-path=/mnt/my-volume
gcloud run jobs update job-quickstart \
--add-volume name=volume-mnt,type=in-memory,size-limit=1Mi \
--add-volume-mount volume=volume-mnt,mount-path=/mnt/my-volume
gcloud run jobs update job-quickstart --update-env-vars AWS_WEB_IDENTITY_TOKEN_FILE=/mnt/my-volume/awscreds
gcloud run jobs update job-quickstart --update-env-vars AWS_ROLE_ARN=arn:aws:iam::99999:role/aws-allow-gcp-access
gcloud run jobs update job-quickstart --update-env-vars AWS_ROLE_SESSION_NAME=hoge_session

gcloud run jobs execute job-quickstart --region asia-northeast1
gcloud run jobs describe job-quickstart

gcloud run jobs execute job-quickstart \
  --region asia-northeast1 \
  --args batch_exe_date=2025/05/28


ARN: arn:aws:iam::610035484565:role/aws-allow-gcp-access
roleSessionName: aws-allow-gcp-access

gcloud auth configure-docker asia-northeast1-docker.pkg.dev

gcloud builds submit --pack image=asia-northeast1-docker.pkg.dev/aspf-jp-test/common-file-transfer-repo:latest
pack build --publish asia-northeast1-docker.pkg.dev/aspf-jp-test/common-file-transfer-repo:latest

asia-northeast1-docker.pkg.dev/aspf-jp-test/common-file-transfer-repo:latest

mvn site:site
mvn surefire-report:report

gcloud builds submit --config ./gcs-transfer-s3/cloudbuild.yaml \
 --substitutions _PROJECT_ID=aspf-jp-test,_REGION=asia-northeast1