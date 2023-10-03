resource "google_compute_network_peering" "peering1" {
    name = "peering1"
    network = google_compute_network.vpc1.self_link
    peer_network = google_compute_network.vpc2.self_link
}

resource "google_compute_network_peering" "peering2" {
    name = "peering2"
    network = google_compute_network.vpc2.self_link
    peer_network = google_compute_network.vpc1.self_link
}

resource "google_compute_network" "vpc1" {
    name = "vpc1"
    auto_create_subnetworks = false
}

resource "google_compute_network" "vpc2" {
    name = "vpc2"
    auto_create_subnetworks = false
}

resource "google_compute_subnetwork" "vpc1_subnet" {
    name = "vpc1-subnet"
    network = google_compute_network.vpc1.id
    ip_cidr_range = "10.1.0.0/24"
    region = var.region
}

resource "google_compute_subnetwork" "vpc2_subnet" {
    name = "vpc2-subnet"
    network = google_compute_network.vpc2.id
    ip_cidr_range = "10.2.0.0/24"
    region = var.region
}