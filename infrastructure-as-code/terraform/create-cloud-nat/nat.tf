#Router
resource "google_compute_router" "default" {
    name = "nat-router"
    network = google_compute_network.default.id
    region = var.region
}

#NAT Gateway
resource "google_compute_router_nat" "default" {
    name = "nat-router"
    router = google_compute_router.default.name
    region = var.region
    nat_ip_allocate_option = "AUTO_ONLY"
    source_subnetwork_ip_ranges_to_nat = "ALL_SUBNETWORKS_ALL_IP_RANGES"

    log_config {
      enable = true
      filter = "ERRORS_ONLY"
    }
}