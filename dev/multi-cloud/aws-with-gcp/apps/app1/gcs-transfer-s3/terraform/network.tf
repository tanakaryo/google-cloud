resource "google_compute_network" "vpc_1" {
    name = "vpc-1"
    auto_create_subnetworks = false
}

resource "google_compute_subnetwork" "subnet_1" {
    name = "subnet-1"
    ip_cidr_range = "10.0.0.0/24"
    network = google_compute_network.vpc_1.id
    region = var.region
    private_ip_google_access = true
}

resource "google_compute_firewall" "default" {
    name = "default-fw"
    network = google_compute_network.vpc_1.name

    source_ranges = [ "0.0.0.0/0" ]
    source_tags = [ "all" ]

    deny {
      protocol = "all"
    }
}