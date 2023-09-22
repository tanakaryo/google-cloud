resource "google_cloudfunctions2_function_iam_member" "invoker" {
    project = var.project_id
    location = google_cloudfunctions2_function.default.location
    cloud_function = google_cloudfunctions2_function.default.name

    role = "roles/cloudfunctions.invoker"
    member = "user:myFunctionInvoker@example.com"
}