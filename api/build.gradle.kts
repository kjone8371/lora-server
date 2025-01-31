dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.7.0")
    
    implementation(project(":common")) // for common response thingy
    implementation(project(":business"))
//    implementation("org.springframework.orm:spring-orm:5.3.22") // 적절한 버전으로 수정

    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
}
