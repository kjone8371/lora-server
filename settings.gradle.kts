plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "spring-jwt-auth-template"
include("api")
include("business")
include("common")
include("core")
include("infra")
