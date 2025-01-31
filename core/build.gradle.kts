dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-data-elasticsearch")

    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
//    api("org.springframework.data:spring-data-elasticsearch:5.4.1")  // 최신 버전 확인

    api ("org.springframework.retry:spring-retry:1.3.1")
    api ("org.springframework.boot:spring-boot-starter-aop")

    api(project(":business"))
    api(project(":common"))

}
