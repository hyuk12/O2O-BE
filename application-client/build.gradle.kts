plugins {
}

dependencies {
    implementation(project(":domains:member"))
    implementation(project(":domains:store"))
    implementation(project(":domains:order"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-tx")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
}
