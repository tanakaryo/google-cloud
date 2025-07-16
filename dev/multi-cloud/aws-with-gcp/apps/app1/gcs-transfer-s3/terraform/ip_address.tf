resource "google_compute_address" "address" {
    name = "nat-manual-ip"
    region = var.region
}