# State bucket is created by ../../bootstrap. Each environment uses its own prefix.
terraform {
  backend "gcs" {
    bucket = "nx-starter-tfstate"
    prefix = "environments/dev"
  }
}
