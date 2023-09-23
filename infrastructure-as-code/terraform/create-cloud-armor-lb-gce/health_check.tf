resource "google_compute_health_check" "default" {
    name = "health-check"
    http_health_check {
      port_specification = "USE_SERVING_PORT"
    }
}