resource "google_compute_network" "default" {
    name = "default-vpc"
    auto_create_subnetworks = false
}

resource "google_compute_subnetwork" "default" {
    name = "default-subnet"
    network = google_compute_network.default.id
    ip_cidr_range = "10.1.0.0/16"
    region = var.region
}