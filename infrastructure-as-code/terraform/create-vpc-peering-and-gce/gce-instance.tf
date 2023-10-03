resource "google_compute_instance" "vpc1-vm" {
    name = "vpc1-vm"
    machine_type = "e2-micro"
    zone = "${var.region}-a"
    tags = [ "ssh" ]

    boot_disk {
      initialize_params {
        image = "debian-cloud/debian-11"
      }
    }

    scheduling {
      preemptible = true
      automatic_restart = false
    }

    network_interface {
      subnetwork = google_compute_subnetwork.vpc1_subnet.id
    }
}

resource "google_compute_instance" "vpc2-vm" {
    name = "vpc2-vm"
    machine_type = "e2-micro"
    zone = "${var.region}-b"
    tags = [ "ssh" ]

    boot_disk {
      initialize_params {
        image = "debian-cloud/debian-11"
      }
    }

    scheduling {
      preemptible = true
      automatic_restart = false
    }

    network_interface {
      subnetwork = google_compute_subnetwork.vpc2_subnet.id
    }
}

