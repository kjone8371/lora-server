dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    
    api(project(":business"))
    api(project(":common"))
}
