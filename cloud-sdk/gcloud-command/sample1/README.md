# [Summary about gcloud-command]
 - tasks: summarize about how to use of gcloud.
 - provider: google cloud
 - resource: etc.

## [Cloud SDK options]
### [gcloud verson]
 - gcloudのバージョンを確認する
 - `gcloud version`

### [gcloud components update]
 - cloud sdkを最新化する
 - `gcloud components update`

### [gcloud info]
 - 現在アクティブな詳細な構成設定を確認する
 - `gcloud info`

## [ Authentification ]
### [gcloud auth list]
 - 現在アクティブなアカウント情報を表示する
 - `gcloud auth list`

### [gcloud auth print-access-token]
 - 現在のアカウントのアクセストークンを表示する
 - `gcloud auth print-access-token`

### [gcloud auth application-default]
 - 現在アクティブのアカウントのアプリケーション認証情報を構成する
 - `gcloud auth application-default <some-command>`

## [ Project Control ]
### [gcloud projects describe]
 - プロジェクトのメタデータを表示する
 - `gcloud projects describe <project-id>`
 - `gcloud projects describe xxxxx-399805`

### [gcloud projects get-iam-policy]
 - プロジェクトのIAMポリシーを取得する
 - `gcloud projects get-iam-policy <project-id>`

### [gcloud projects create]
 - プロジェクトを作成する
 - `gcloud projects create <project-name or id>`

### [gcloud projects delete]
 - プロジェクトを削除する
 - `gcloud projects delete <project-id>`

## [ Configurations ]
### [gcloud config configurations list]
 - 現在アクティブな構成設定を確認する
 - `gcloud config configurations list`

### [gcloud config list]
 - ComputeEngineの初期設定と現在アクティブなアカウント情報を取得する
 - `gcloud config list`

## [ IAM Control ]
### [gcloud iam service-accounts create]
 - Service accountを作成する
 - `gcloud iam service-accounts create`
 - `gcloud iam service-accounts create gcloud-commmand-test --display-name="This is for test"`

### [gcloud iam service-accounts list]
 - Service accountの一覧を取得する
 - `gcloud iam service-accounts list`

### [gcloud iam service-accounts delete]
 - Service accountを削除する
 - `gcloud iam service-accounts delete`
 - `gcloud iam service-accounts delete gcloud-commmand-test@someproj.iam.gserviceaccount.com`

### [gcloud iam list-grantable-roles]
 - 特定のプロジェクトのIAMロールの一覧を取得する
 - `gcloud iam list-grantable-roles`
 - `gcloud iam list-grantable-roles //cloudresourcemanager.googleapis.com/projects/<project-id>`

### [gcloud iam roles create]
 - カスタムIAMロールを作成する
 - `gcloud iam roles create`
 - `gcloud iam roles create TestCommander --project=<project-id> --title=TestCommander --description="This is test." --permissions=resourcemanager.projects.get,resourcemanager.projects.update`

### [gcloud iam roles delete]
 - IAMロールを削除する
 - `gcloud iam roles delete`
 - `gcloud iam roles delete <role-id> --project=<project-id>`

## [ Service Control ]
### [gcloud services enable]
 - Google CloudリソースにアクセスするAPIを有効化する
 - `gcloud services enable <service-name>`
 - `gcloud services enable container.googleapis.com`

## [GKE or Docker Control]
### [gcloud components install kubectl]
 - GKE用のkubernetesコンポーネントをインストールする
 - `gcloud components install kubectl`

### [gcloud container clusters create-auto]
 - GKEのAutoPilotクラスターを作成する
 - `gcloud container clusters create-auto <cluster-name>`
 - `gcloud container clusters create-auto gcloud-test-cluster --location=asia-northeast1`

### [gcloud container clusters delete]
 - GKEクラスターを削除する
 - `gcloud container clusters delete <cluster-name> --location=<location-name>`
 - `gcloud container clusters delete gcloud-test-cluster --location=asia-northeast1`

## [ Compute Resource Control]
### [gcloud compute networks create]
 - VPCを作成する
 - `gcloud compute networks create <vpc-name> --subnet-mode=<mode> --bgp-routing-mode=<mode> --mtu=<mtu-size>`
 - `gcloud compute networks create vpc1 --subnet-mode=auto --bgp-routing-mode=regional --mtu=1300`

### [gcloud compute networks delete]
 - VPCを削除する
 - `gcloud compute networks delete <vpc-name>`
 - `gcloud compute networks delete vpc1`