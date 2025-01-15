dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.7.0")
    
    implementation(project(":common")) // for common response thingy
    implementation(project(":business"))
}
