dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-data-elasticsearch")
//    api("org.springframework.data:spring-data-elasticsearch:5.4.1")  // 최신 버전 확인

    api(project(":business"))
    api(project(":common"))

}
