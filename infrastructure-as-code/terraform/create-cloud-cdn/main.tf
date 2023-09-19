# VPC
resource "google_compute_network" "default" {
    name = "default-vpc"
    auto_create_subnetworks = false
}

# backend subnet
resource "google_compute_subnetwork" "default" {
    name = "be-subnet"
    ip_cidr_range = "10.0.1.0/24"
    network = google_compute_network.default.id
}

# reserved IP address
resource "google_compute_global_address" "default" {
    name = "global-ip"
}

# forwarding rule
resource "google_compute_global_forwarding_rule" "default" {
    name = "forwarding-rule"
    ip_protocol = "TCP"
    load_balancing_scheme = "EXTERNAL"
    port_range = "80"
    target = google_compute_target_http_proxy.default.id
    ip_address = google_compute_global_address.default.id
}

# http proxy
resource "google_compute_target_http_proxy" "default" {
    name = "http-proxy"
    url_map = google_compute_url_map.default.id
}

# url map
resource "google_compute_url_map" "default" {
    name = "url-map"
    default_service = google_compute_backend_service.default.id
}

# backend service with custom request and responce headers
resource "google_compute_backend_service" "default" {
    name = "backend-svc"
    protocol = "HTTP"
    port_name = "default-port"
    load_balancing_scheme = "EXTERNAL"
    timeout_sec = 10
    enable_cdn = true
    custom_request_headers = ["X-Client-Geo-Location: {client_region_subdivision}, {client_city}"]
    custom_response_headers = ["X-Cache-Hit: {cdn_cache_status}"]
    health_checks = [ google_compute_health_check.default.id ]
    backend {
      group = google_compute_instance_group_manager.default.instance_group
      balancing_mode = "UTILIZATION"
      capacity_scaler = 1.0
    }
}

# instance template
resource "google_compute_instance_template" "default" {
    name = "instance-template"
    machine_type = "e2-micro"
    tags = ["allow-health-check"]

    network_interface {
      network = google_compute_network.default.id
      subnetwork = google_compute_subnetwork.default.id
      access_config {
      }
    }

    disk {
        source_image = "debian-cloud/debian-10"
        auto_delete = true
        boot = true
    }

    metadata = {
      startup-script = <<-EOF1
      #! /bin/bash
      set -euo pipefail

      export DEBIAN_FRONTEND=noninteractive
      apt-get update
      apt-get install -y nginx-light jq

      NAME=$(curl -H "Metadata-Flavor: Google" "http://metadata.google.internal/computeMetadata/v1/instance/hostname")
      IP=$(curl -H "Metadata-Flavor: Google" "http://metadata.google.internal/computeMetadata/v1/instance/network-interfaces/0/ip")
      METADATA=$(curl -f -H "Metadata-Flavor: Google" "http://metadata.google.internal/computeMetadata/v1/instance/attributes/?recursive=True" | jq 'del(.["startup-script"])')

      cat <<EOF > /var/www/html/index.html
      <pre>
      Name: $NAME
      IP: $IP
      Metadata: $METADATA
      </pre>
      EOF
    EOF1
    }

    lifecycle {
      create_before_destroy = true
    }
}

# health check
resource "google_compute_health_check" "default" {
    name = "health-check"
    http_health_check {
      port_specification = "USE_SERVING_PORT"
    }
}

# Managed Instance Group
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

# firewall
resource "google_compute_firewall" "default" {
    name = "default-firewall"
    direction = "INGRESS"
    network = google_compute_network.default.id
    source_ranges = ["130.211.0.0/22", "35.191.0.0/16"]
    allow {
      protocol = "tcp"
    }
    target_tags = [ "allow-health-check" ]
}
