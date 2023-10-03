resource "google_compute_firewall" "vpc1-subnet-firewall" {
    name = "allow-ssh1"

    allow {
      protocol = "icmp"
    }

    allow {
      ports = [ "22" ]
      protocol = "tcp"
    }

    network = google_compute_network.vpc1.id
    priority = 100
    source_ranges = [ "0.0.0.0/0" ]
    target_tags = [ "ssh" ]
}

resource "google_compute_firewall" "vpc2-subnet-firewall" {
    name = "allow-ssh2"

    allow {
      protocol = "icmp"
    }

    allow {
      ports = [ "22" ]
      protocol = "tcp"
    }

    network = google_compute_network.vpc2.id
    priority = 100
    source_ranges = [ "0.0.0.0/0" ]
    target_tags = [ "ssh" ]
}