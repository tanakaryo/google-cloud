# VPC
resource "google_compute_network" "default" {
    name = "vpc1"
    auto_create_subnetworks = false
}

# Subnet
resource "google_compute_subnetwork" "default" {
    name = "subnet1"
    ip_cidr_range = "10.10.0.0/24"
    network = google_compute_network.default.id
    region = var.region
}