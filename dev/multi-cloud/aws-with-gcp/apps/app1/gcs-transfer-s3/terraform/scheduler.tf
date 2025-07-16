data "google_service_account" "compute_sa" {
  account_id = "1009205782858-compute@developer.gserviceaccount.com"
}

resource "google_cloud_scheduler_job" "job" {
  name             = "test-job"
  description      = "test http job"
  schedule         = "*/8 * * * *"
  attempt_deadline = "600s"
  time_zone = "Asia/Tokyo"
  region           = "asia-northeast1"

  retry_config {
    retry_count = 1
  }

  http_target {
    http_method = "POST"
    uri         = "https://asia-northeast1-run.googleapis.com/apis/run.googleapis.com/v1/namespaces/1009205782858/jobs/job-quickstart:run"

    oauth_token {
      service_account_email = data.google_service_account.compute_sa.email
    }
  }
}
