provider "google" {
  project = var.project_id
}

resource "google_spanner_instance" "default" {
  config = var.google_spanner_instance_location
  display_name = var.google_spanner_database_name
  num_nodes = 1
}

resource "google_spanner_database" "default" {
  instance = google_spanner_instance.default.name
  name = var.google_spanner_database_name
}