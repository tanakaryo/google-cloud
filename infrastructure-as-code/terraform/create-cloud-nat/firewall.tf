# Firewall
resource "google_compute_firewall" "default" {
    name = "allow-ssh"
    network = google_compute_network.default.id

    allow {
      protocol = "tcp"
      ports = [ "22" ]
    }
    source_ranges = [ "35.235.240.0/20" ]
}

