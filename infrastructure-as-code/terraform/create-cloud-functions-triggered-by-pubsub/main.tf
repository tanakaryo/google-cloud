resource "random_id" "bucket_prefix" {
    byte_length = 8
}

data "archive_file" "default" {
    type = "zip"
    output_path = "/tmp/function-src.zip"
    source_dir = "function-src/"
}