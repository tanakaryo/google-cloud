resource "google_artifact_registry_repository" "common_file_transfer_repo" {
    location = "asia-northeast1"
    repository_id = "common-file-transfer-repo"
    description = "This is test repo."
    format = "DOCKER"
}