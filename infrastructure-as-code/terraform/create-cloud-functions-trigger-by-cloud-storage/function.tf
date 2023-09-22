resource "google_cloudfunctions2_function" "default" {
    name = "hello-function"
    location = var.region
    description = "tutorial function"

    build_config {
      runtime = "python311"
      entry_point = "hello_gcs"
      source {
        storage_source {
            bucket = google_storage_bucket.default.name
            object = google_storage_bucket_object.default.name
        }
      }
    }

    service_config {
      max_instance_count = 1
      available_memory = "256M"
      timeout_seconds = 60
    }

    event_trigger {
        trigger_region = var.region
        event_type = "google.cloud.storage.object.v1.finalized"
        event_filters {
          attribute = "bucket"
          value = google_storage_bucket.default.name
        }
    }
}

output "function_uri" {
    value = google_cloudfunctions2_function.default.service_config[0].uri
}