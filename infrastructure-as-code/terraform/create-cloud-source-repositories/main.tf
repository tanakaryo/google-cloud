resource "google_service_account" "csr_svc_accnt" {
  account_id = "csr-service-account"
  display_name = "CSR Service Account"
}

resource "google_pubsub_topic" "topic" {
  name = "test-topic"
}

resource "google_sourcerepo_repository" "default" {
  name =  "${var.project_id}-repository"
  pubsub_configs {
    topic = google_pubsub_topic.topic.id
    message_format = "JSON"
    service_account_email = google_service_account.csr_svc_accnt.email
  }
}