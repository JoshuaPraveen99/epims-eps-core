plugins{
    java
    war
    id("org.springframework.boot") version "4.0.9"
    id("io.spring.dependency-management") version "1.1.6"
}

group="com.epims.pharmacy"
version="0.1.0-SNAPSHOT"
description="EPIMS Pharmacy EPS Core Backend"

java{
    toolchain{
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories{
    mavenCentral()
}

extra["springCloudVersion"] = "2025.1.4"

dependencyManagement {
    imports {
        mavenBom(
            "org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}"
        )
    }
}

dependencies{
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    // Liberty supplies the servlet container at runtime.
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}