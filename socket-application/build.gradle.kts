plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation(project(":application"))
    implementation(project(":core"))
    implementation(project(":lib"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter-logging")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
}
tasks.named<Jar>("jar") {
    enabled = true  // 다시 활성화
    archiveClassifier = "" // plain jar 생성
}
