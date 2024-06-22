resource "google_compute_network" "vpc1" {
  name                    = "vpc1"
  auto_create_subnetworks = "false"
}

resource "google_compute_subnetwork" "subnet1" {
  name          = "subnet1"
  ip_cidr_range = "10.2.0.0/16"
  region        = var.REGION
  network       = google_compute_network.vpc1.id
}
