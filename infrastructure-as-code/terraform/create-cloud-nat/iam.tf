# Service Account
resource "google_service_account" "default" {
    project = var.project_id
    account_id = "terraform-demo"
    display_name = "Terraform Demo"
}

# IAM Role
resource "google_project_iam_member" "prjiam" {
    project = var.project_id
    role = "roles/iap.tunnelResourceAccessor"
    member = "serviceAccount:${google_service_account.default.email}"
}