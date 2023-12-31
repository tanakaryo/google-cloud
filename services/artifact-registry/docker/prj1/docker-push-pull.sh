# artifact registryにリポジトリ作成
gcloud artifacts repositories create quickstart-docker-repo --repository-format=docker \
> --location=asia-northeast1 --description="Docker repos"

# docker認証
gcloud auth configure-docker \
    asia-northeast1-docker.pkg.dev

# pull
docker pull us-docker.pkg.dev/google-samples/containers/gke/hello-app:1.0

# tag
docker tag us-docker.pkg.dev/google-samples/containers/gke/hello-app:1.0 asia-northeast1-docker.pkg.dev/aeristest-404517/quickstart-docker-repo/quickstart-image:tag1

# push
docker push asia-northeast1-docker.pkg.dev/aeristest-404517/quickstart-docker-repo/quickstart-image:tag1

# pull
docker pull asia-northeast1-docker.pkg.dev/aeristest-404517/quickstart-docker-repo/quickstart-image:tag1

# remove repos
gcloud artifacts repositories delete quickstart-docker-repo --location=asia-northeast1
