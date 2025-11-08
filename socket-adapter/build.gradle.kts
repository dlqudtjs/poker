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

    // WebSocket 및 STOMP 지원
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // 메시지 처리 (STOMP 메시지 처리, SimpMessaging 등)
    implementation("org.springframework:spring-messaging")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
}
tasks.named<Jar>("jar") {
    enabled = true  // 다시 활성화
    archiveClassifier = "" // plain jar 생성
}
