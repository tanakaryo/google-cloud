resource "google_storage_bucket" "common_file_transfer_private_test_bucket" {
  name = "common-file-transfer-private-test-bucket"
  location = "ASIA-NORTHEAST1"
  force_destroy = true

  lifecycle_rule {
    condition {
      age = 3
    }

    action {
      type = "SetStorageClass"
      storage_class = "ARCHIVE"
    }
  }

  lifecycle_rule {
    condition {
        age = 3650
    }

    action {
        type = "Delete"
    }
  }
}