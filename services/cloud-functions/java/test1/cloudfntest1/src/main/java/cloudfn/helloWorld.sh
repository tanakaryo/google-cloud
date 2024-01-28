  gcloud functions deploy java-http-function \
    --gen2 \
    --entry-point=cloudfn.HelloWorld \
    --runtime=java17 \
    --region=asia-northeast1 \
    --source=. \
    --trigger-http \
    --allow-unauthenticated