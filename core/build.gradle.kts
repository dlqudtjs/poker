plugins {
    kotlin("jvm")
    kotlin("plugin.jpa")
}

dependencies {
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.test { useJUnitPlatform() }
