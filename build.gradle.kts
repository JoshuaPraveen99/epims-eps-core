plugins{
    java
    war
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group="com.epims.pharmacy"
version="0.1.0-SNAPSHOT"
description="EPIMS Pharmacy EPS Core Backend"

java{
    toolchain{
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories{
    mavenCentral()
}

dependencies{
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Liberty supplies the servlet container at runtime.
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}