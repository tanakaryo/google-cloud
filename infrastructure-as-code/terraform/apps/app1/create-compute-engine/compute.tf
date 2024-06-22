resource "google_compute_instance" "instance1" {
  name         = "instance1"
  machine_type = "e2-micro"
  zone         = "asia-northeast1-a"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-11"
      labels = {
        my_label = "value"
      }
    }
  }

  network_interface {
    network    = google_compute_network.vpc1.name
    subnetwork = google_compute_subnetwork.subnet1.name
    network_ip = "10.2.0.2"
    access_config {
    }
  }
}