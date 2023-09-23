resource "google_compute_instance_group_manager" "default" {
    name = "mig"
    zone = "${var.region}-a"
    named_port {
      name = "http"
      port = 8080
    }
    
    version {
      instance_template = google_compute_instance_template.default.id
      name = "primary"
    }

    base_instance_name = "vm"
    target_size = 2
}

resource "google_compute_instance_template" "default" {
    name = "instance-template"
    machine_type = "e2-micro"
    tags = ["allow-health-check"]

    network_interface {
      network = google_compute_network.default.id
      subnetwork = google_compute_subnetwork.default.id
    }

    disk {
        source_image = "debian-cloud/debian-10"
        auto_delete = true
        boot = true
    }

    lifecycle {
      create_before_destroy = true
    }
}