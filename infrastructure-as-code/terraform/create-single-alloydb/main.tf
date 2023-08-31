provider "google" {
    project = var.project_id
}

resource "google_compute_network" "default" {
  name = "test-vpc"
}

resource "google_compute_global_address" "default" {
  name = "${var.project_id}-alloydb-cluster-ip"
  address_type = "INTERNAL"
  purpose = "VPC_PEERING"
  prefix_length = 16
  network = google_compute_network.default.id
}

resource "google_service_networking_connection" "default" {
  network = google_compute_network.default.id
  service = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [ google_compute_global_address.default.name ]
}

resource "google_alloydb_cluster" "default" {
  cluster_id = "${var.project_id}-alloydb-cluster"
  network = google_compute_network.default.id
  location = "asia-northeast1"
}

resource "google_alloydb_instance" "default" {
  instance_id = "${var.project_id}-alloydb-primary-instance"
  instance_type = var.alloydb_instance_type
  cluster = google_alloydb_cluster.default.name
  
  database_flags = {
    "alloydb.logical_decoding" = "on"
  }

  depends_on = [
    google_service_networking_connection.default
  ]
}
