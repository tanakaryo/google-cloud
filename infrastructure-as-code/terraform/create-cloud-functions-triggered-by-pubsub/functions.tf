resource "google_cloudfunctions2_function" "default" {
    name = "pubsub-event-function"
    location = var.region
    description = "triggered by pubsub topic."
    build_config {
        runtime = "python311"
        entry_point = "hello_pubsub"
        environment_variables = {
            BUILD_CONFIG_TEST = "build_test"
        }
        source {
          storage_source {
            bucket = google_storage_bucket.default.name
            object = google_storage_bucket_object.default.name
          }
        }
    }

    service_config {
      max_instance_count = 1
      min_instance_count = 1
      available_memory = "256M"
      timeout_seconds = 60
      environment_variables = {
        SERVICE_CONFIG_TEST = "config_test"
      }
      ingress_settings = "ALLOW_INTERNAL_ONLY"
      all_traffic_on_latest_revision = true
      service_account_email = "nifty-gasket-399805@appspot.gserviceaccount.com"
    }

    event_trigger {
      trigger_region = var.region
      event_type = "google.cloud.pubsub.topic.v1.messagePublished"
      pubsub_topic = google_pubsub_topic.default.id
      retry_policy = "RETRY_POLICY_RETRY"
    }
}