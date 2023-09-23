resource "google_compute_security_policy" "default" {
    name = "security-policy"
    description = "basic security policy"
    type = "CLOUD_ARMOR"

    adaptive_protection_config {
      layer_7_ddos_defense_config {
        enable = true
      }
    }

    recaptcha_options_config {
      redirect_site_key = google_recaptcha_enterprise_key.default.name
    }
}

resource "google_recaptcha_enterprise_key" "default" {
    display_name = "recaptcha"

    labels = {
      label-one = "value-one"
    }

    project = var.project_id

    web_settings {
      integration_type = "INVISIBLE"
      allow_all_domains = true
      allowed_domains = [ "localhost" ]
    }
}