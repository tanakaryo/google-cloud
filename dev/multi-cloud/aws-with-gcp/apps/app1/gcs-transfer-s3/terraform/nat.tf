resource "google_compute_router" "default" {
    name = "default-router"
    network = google_compute_network.vpc_1.id
    region = var.region
}

resource "google_compute_router_nat" "default" {
    name = "default-nat"
    router = google_compute_router.default.name
    region = var.region
    nat_ip_allocate_option = "MANUAL_ONLY"
    nat_ips = [google_compute_address.address.self_link]
    source_subnetwork_ip_ranges_to_nat = "LIST_OF_SUBNETWORKS"
    subnetwork {
      name = google_compute_subnetwork.subnet_1.id
      source_ip_ranges_to_nat = ["ALL_IP_RANGES"]
    }

    log_config {
      enable = true
      filter = "ERRORS_ONLY"
    }
}