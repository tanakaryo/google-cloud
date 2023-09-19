# Compute Engine
resource "google_compute_instance" "default" {
    name = "vm1"
    machine_type = "e2-micro"
    zone = "${var.region}-a"
    boot_disk {
      initialize_params {
        image = "debian-cloud/debian-11"
      }
    }

    network_interface {
      network = google_compute_network.default.id
      subnetwork = google_compute_subnetwork.default.id
    }
}