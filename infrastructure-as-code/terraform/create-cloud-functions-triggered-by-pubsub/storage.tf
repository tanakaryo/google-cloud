resource "google_storage_bucket" "default" {
    name = "${random_id.bucket_prefix.hex}-gcf-source"
    location = var.region
    uniform_bucket_level_access = false
}

resource "google_storage_bucket_object" "default" {
    name = "function-src.zip"
    bucket = google_storage_bucket.default.name
    source = data.archive_file.default.output_path
}