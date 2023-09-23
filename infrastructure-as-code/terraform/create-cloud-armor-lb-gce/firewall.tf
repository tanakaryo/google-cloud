resource "google_compute_firewall" "default" {
    name = "firewall"
    direction = "INGRESS"
    network = google_compute_network.default.id
    source_ranges = [ "130.211.0.0/22","35.191.0.0/16" ]
    allow {
      protocol = "tcp"
    }
    target_tags = [ "allow-health-check" ]
}