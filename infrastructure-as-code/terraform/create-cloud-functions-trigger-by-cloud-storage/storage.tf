resource "google_storage_bucket" "default" {
    name = "tutorial-cf-20230922"
    location = var.region
    uniform_bucket_level_access = true
}

resource "google_storage_bucket_object" "default" {
    name = "function-src.zip"
    bucket = google_storage_bucket.default.name
    source = "./function-src.zip"
}