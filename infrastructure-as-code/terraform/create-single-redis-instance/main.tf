resource "google_redis_instance" "default" {
    name = "redis-instance"
    tier = "BASIC"
    memory_size_gb = 1
    region = var.region
    redis_version = var.redis_ver
}