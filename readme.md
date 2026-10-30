# EPIMS Pharmacy System — EPS Core Backend

## System Architecture at a Glance

> **Diagram assets:** The images are stored under `docs/diagrams/`. Keep this folder alongside `README.md` when copying the README or pushing it to GitHub; otherwise, the images will not render.

The following block diagram shows the intended system-level design. The repository and configuration components mentioned later in this README are at different implementation stages; the diagram is an architecture target, not a claim that every service is already running.

![EPIMS Pharmacy System target architecture](docs/diagrams/epims-system-architecture.png)

**Architecture interpretation**
- The service names describe planned repository responsibilities. Implement services incrementally and verify each integration.
- `epims-config` stores non-sensitive configuration and placeholders only.
- `epims-config-server` reads the private configuration repository using a dedicated read-only SSH deploy key.
- The SSH key is for Git repository access; it is not an application database password or a general-purpose secret.
- OpenBao and the secret-management API are planned components. Do not assume secret placeholders are resolved until the integration is implemented and tested.
- `epims-infrastructure` is intended to own container orchestration and deployment configuration. The Config Server application itself stays in `epims-config-server`.

---

## 1. Project Overview

### 1.1 Purpose

EPIMS (Enterprise Pharmacy Information Management System) is a planned enterprise pharmacy platform that will support prescription processing, patient information management, medication verification, order fulfillment, payment processing, and notifications.

This repository contains the **EPS Core Backend**, which will manage the pharmacy order lifecycle and coordinate with other services.

### 1.2 Planned EPS Workflow

The EPS order workflow will include:

1. Order Entry
2. Data Entry
3. Third-Party Adjudication
4. Drug Utilization Review (DUR)
5. Third-Party Exception Handling, when required
6. Data Verification
7. Fill
8. Product Verification
9. Point of Sale (POS) payment for pickup orders

For mail orders, the workflow will integrate with SBMO for packing, fulfillment, shipping, and status updates. Patient notifications will be handled by the notification service.

These workflows describe the intended architecture; they will be implemented incrementally.

![EPS pharmacy order workflow](docs/diagrams/eps-pharmacy-workflow.png)

### 1.3 Technology Stack

| Technology | Purpose |
|---|---|
| Java 25 | Application programming language |
| Spring Boot | Backend application framework |
| Gradle Kotlin DSL | Build and dependency management |
| MongoDB | Document-oriented database |
| IBM WebSphere Liberty | Application server |
| Git | Local version control |
| GitHub | Remote repository hosting |
| Docker | Containerization |
| Kubernetes | Container orchestration |
| Oracle Cloud Infrastructure | Planned deployment platform |

**Important:** These are the planned technologies. Each component will be configured and verified in its corresponding development step.

---

## 2. Repository Architecture

EPIMS uses a multi-repository approach. Each backend microservice and frontend application will have its own Git repository.

### 2.1 Repository Register

| Repository | Intended Responsibility |
|---|---|
| `epims-api-gateway` | API routing and gateway concerns |
| `epims-digital-core` | Shared digital/core capabilities |
| `epims-eps-core` | EPS pharmacy backend |
| `epims-eps-core-ui` | EPS frontend |
| `epims-epr-core` | Electronic patient record service |
| `epims-sbmo-core` | Mail-order pharmacy operations backend |
| `epims-sbmo-ui` | SBMO frontend |
| `epims-infrastructure` | Infrastructure and deployment configuration |
| `epims-notification-service` | Notification processing |
| `epims-shared-contracts` | Shared API and event contracts |

The responsibilities listed above are the initial working definitions. Service boundaries and ownership will be refined as the architecture is implemented.

### 2.2 Current Repository

- **Repository name:** `epims-eps-core`
- **Application:** EPS Core Backend
- **Build tool:** Gradle
- **Database:** MongoDB
- **Application server:** IBM WebSphere Liberty
- **Source control:** Git and GitHub

### 2.3 Repository Independence

Each repository will have its own:

- Source code
- README documentation
- Build configuration
- Git commit history
- Automated tests
- Build and deployment configuration, as applicable

This approach allows each service or UI to evolve independently.

---

## 3. Development Log

# Step 1 — Create the GitHub Repository and Initialize Git

## 3.1 Objective

Create the remote repository, prepare the local workspace, and initialize Git before implementing application functionality.

Starting with Git ensures that project changes can be tracked from the beginning.

## 3.2 Create the GitHub Repository

### Procedure

1. Sign in to GitHub.
2. Select the **+** menu and choose **New repository**.
3. Enter the repository name:

   `epims-eps-core`

4. Add a description such as:

   `EPIMS Pharmacy System - EPS Core Backend`

5. Select **Private** visibility if the project should remain private.
6. If creating a completely new repository, leave README initialization disabled so that the local README can be pushed without an initial unrelated remote commit.
7. Create the repository.
8. Copy the repository's HTTPS or SSH URL for use when configuring the Git remote.

### Explanation

A GitHub repository is the remote location for storing the project and its history. The local repository is where development and commits take place.

The local and remote repositories are connected using a Git remote, commonly named `origin`.

## 3.3 Create the Local Workspace

Open PowerShell and navigate to the parent directory where your EPIMS repositories are stored.

If the `epims-eps-core` folder already exists, **do not create it again**.

Example:

```powershell
cd <your-EPIMS-workspace>
cd epims-eps-core
```

Replace `<your-EPIMS-workspace>` with your actual parent directory. Do not type the angle brackets literally.

Verify the current directory:

```powershell
Get-Location
```

The resulting path should end with:

```text
epims-eps-core
```

### Explanation

Working from the correct directory is important because Git commands operate on the repository associated with the current directory.

Running commands in the wrong folder can accidentally create a nested repository or put project files in the wrong location.

## 3.4 Initialize the Local Git Repository

If Git has not already been initialized in this folder, run:

```powershell
git init
```

Verify the repository:

```powershell
git status
```

### Expected Result

Git should report that the current directory is a repository and show the working tree status.

If the repository is already initialized, you do not need to run `git init` again.

To check the repository's root directory:

```powershell
git rev-parse --show-toplevel
```

The returned path should point to `epims-eps-core`.

### Explanation of Commands

| Command | Purpose |
|---|---|
| `git init` | Initializes a local Git repository |
| `git status` | Shows modified, staged, and untracked files |
| `git rev-parse --show-toplevel` | Displays the repository root directory |

## 3.5 Configure Git Identity

Git records the author of each commit.

If your identity is not configured, set it using your own details:

```powershell
git config --global user.name "Your Name"
git config --global user.email "your-email@example.com"
```

Verify the configuration:

```powershell
git config --global user.name
git config --global user.email
```

Use your actual name and preferred email address rather than the example values.

### Explanation

- `user.name` sets the author name recorded in commits.
- `user.email` sets the author email recorded in commits.
- `--global` applies the setting to your user account across repositories.

If you need different identities for different repositories, you can configure repository-specific settings instead.

## 3.6 GitHub Connection Strategy

After the first project files are ready, configure the remote repository:

```powershell
git remote add origin <your-GitHub-repository-URL>
```

Verify it:

```powershell
git remote -v
```

Replace the placeholder with the actual repository URL.

If `origin` already exists, inspect it before making changes:

```powershell
git remote get-url origin
```

Do not add a duplicate remote.

We will perform the first commit and push after the initial build configuration has been established and verified.

## 3.7 Common Problems

### Problem: `git` is not recognized

Git may not be installed, or its installation directory may not be on the Windows PATH.

Verify the installation:

```powershell
git --version
```

Install Git for Windows if necessary, reopen PowerShell, and retry.

### Problem: `Author identity unknown`

Configure `user.name` and `user.email` using the commands in Section 3.5.

### Problem: `remote origin already exists`

Run:

```powershell
git remote -v
```

Check whether the existing remote points to the intended `epims-eps-core` repository before changing it.

### Problem: Authentication fails when pushing

Check the repository URL and your GitHub access. HTTPS authentication generally requires an approved authentication method, such as Git Credential Manager, rather than your ordinary GitHub account password.

## 3.8 Completion Checklist

- [ ] GitHub repository `epims-eps-core` created.
- [ ] Local workspace directory verified.
- [ ] Git repository initialized or existing initialization verified.
- [ ] Git identity configured.
- [ ] Git repository root verified.
- [ ] GitHub remote URL verified, when configured.

**Status:** Completed according to the development log; verify individual items if needed.

---

# Step 2 — Create the Initial Project Directory Structure

## 4.1 Objective

Manually create the standard source-code and test directories for the EPS backend.

We are not using Spring Initializr. Understanding the directory structure first will make the Gradle and Spring Boot configuration easier to understand.

## 4.2 Verify the Working Directory

Open PowerShell inside `epims-eps-core`:

```powershell
Get-Location
```

Verify the Git root:

```powershell
git rev-parse --show-toplevel
```

Both should identify the intended repository.

## 4.3 Create the Source Directories

Run these commands from the repository root:

```powershell
mkdir src
mkdir src\main
mkdir src\main\java
mkdir src\main\resources
mkdir src\test
mkdir src\test\java
```

If a directory already exists, PowerShell may report that it exists. Verify the structure rather than deleting or recreating existing directories.

## 4.4 Understand the Directory Structure

```text
epims-eps-core/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│       └── java/
├── README.md
└── .git/
```

The Gradle files, application classes, configuration, and Liberty deployment files will be added in later steps.

### `src/main/java`

This directory contains production Java source code.

As the application grows, it will contain packages for responsibilities such as:

- Controllers: receive and handle HTTP requests.
- Services: implement business logic.
- Repositories: access MongoDB.
- Models: represent application data.
- Configuration: configure application components.
- Security: authentication and authorization components.

These packages will be introduced when the corresponding features are implemented.

### `src/main/resources`

This directory stores application resources that are not ordinary Java source files.

Examples include:

- `application.yml`: application configuration.
- Logging configuration.
- Database-related configuration and migration resources, where applicable.
- Liberty deployment-related resources, depending on the deployment arrangement.

We will create these files when their configuration is introduced.

### `src/test/java`

This directory contains automated tests.

Tests will be used to verify business logic, API behavior, database integration, and other application features.

### `.git`

This hidden directory stores local Git metadata, including repository configuration and version-control information.

**Do not manually edit or delete `.git`.**

## 4.5 Verify the Directory Structure

Run:

```powershell
tree /F
```

If you want to list the source directories explicitly:

```powershell
Get-ChildItem src -Recurse
```

### Expected Result

You should see:

- `src/main/java`
- `src/main/resources`
- `src/test/java`

The folders may initially be empty. That is expected.

## 4.6 Why We Are Creating the Structure Manually

Gradle uses conventional project directories to identify source code, resources, and tests.

Following these conventions means we generally do not need to tell Gradle where each source directory is located.

The build configuration will connect this structure to Java compilation, dependency resolution, testing, packaging, and eventually deployment to Liberty.

## 4.7 Common Problems

### Problem: The terminal is in the wrong directory

Run:

```powershell
Get-Location
git rev-parse --show-toplevel
```

Navigate to the intended repository root before continuing.

### Problem: A directory already exists

Do not delete it automatically. Inspect it and reuse it if it is part of the correct project.

### Problem: The folders appear empty

Empty source directories are normal at this stage. They will contain files as we implement the application.

## 4.8 Completion Checklist

- [ ] Correct repository root verified.
- [ ] `src/main/java` created.
- [ ] `src/main/resources` created.
- [ ] `src/test/java` created.
- [ ] Directory structure verified using `tree /F` or `Get-ChildItem`.
- [ ] README updated with completed steps.

**Status:** Completed according to the development log; verify individual items if needed.

---

## 5. Development Rules

> **Current decision update (supersedes older setup values below):** The current EPIMS setup uses **Java 25**, **Spring Boot 4.0.9**, **Gradle Kotlin DSL**, and **Spring Cloud release train 2025.1.4** for the Config Server and EPS Config Client integration. Some early development-log steps below record the original Java 21 / Spring Boot 3.3.4 setup. Those values are retained as historical context, not as the versions to choose when recreating the current project. For a clean recreation, use the current version values recorded in Step 10.4 and the actual Gradle Wrapper version verified against Java 25. Do not force Gradle 8.10.2 if it is incompatible with the selected Java toolchain.


1. Create the project manually without Spring Initializr.
2. Use Java 21 and Gradle Kotlin DSL.
3. Use MongoDB for the EPS backend.
4. Prepare the application for IBM WebSphere Liberty deployment.
5. Maintain separate Git repositories for each microservice and UI.
6. Explain the purpose of important files, commands, and configurations.
7. Verify each step before proceeding.
8. Update this README after every development step.
9. Commit and push code to GitHub at appropriate milestones.
10. Keep credentials, passwords, API keys, and other secrets out of source control.

## 6. Next Development Step

### Step 3 — Configure Gradle

The next step will establish the Gradle build configuration, including:

- Project metadata in `settings.gradle.kts`.
- Java version and Gradle plugins.
- Dependency declarations.
- Packaging configuration for the planned Liberty deployment.
- Gradle wrapper setup and build verification.

The exact Spring Boot and Liberty configuration will be selected to maintain compatibility with the chosen Java version and application-server capabilities.

# Step 3 — Configure Gradle

## 5.1 Objective

Configure Gradle manually for the EPS Core Backend.

The configuration establishes the project name, Java version, build plugins, dependency repositories, application dependencies, test framework, and intended WAR packaging.

Spring Initializr is not used.

## 5.2 Prerequisites

Verify the installed Java runtime and compiler:

```powershell
java -version
javac -version
```

Both should report Java 21.

Verify Gradle:

```powershell
gradle --version
```

This project uses Gradle 8.10.2. Gradle 8.5 and later support running on Java 21.

## 5.3 Create `settings.gradle.kts`

Create this file at the repository root:

```kotlin
rootProject.name = "epims-eps-core"
```

### Explanation

`settings.gradle.kts` is the Gradle settings file. It identifies the project and can later define subprojects or other build-level settings.

The `.kts` extension indicates Kotlin DSL.

## 5.4 Create `build.gradle.kts`

Create this file at the repository root:

```kotlin
plugins {
    java
    war
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.epims.pharmacy"
version = "0.1.0-SNAPSHOT"
description = "EPIMS Pharmacy EPS Core Backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
```

### Explanation of the plugins

- `java`: Enables Java compilation and testing tasks.
- `war`: Adds support for packaging a web application as a WAR.
- `org.springframework.boot`: Adds Spring Boot build and packaging tasks.
- `io.spring.dependency-management`: Helps manage dependency versions.

### Explanation of project metadata

- `group`: Identifies the logical organization or namespace for the project.
- `version`: Identifies the current development version.
- `description`: Documents the project's purpose.

### Explanation of the Java toolchain

`JavaLanguageVersion.of(21)` configures Java 21 for compilation.

This does not automatically install Java 21 on every machine. The appropriate JDK must be available to Gradle, or toolchain provisioning must be configured separately.

### Explanation of repositories

`mavenCentral()` tells Gradle where to retrieve dependencies that are published to Maven Central.

### Explanation of dependencies

| Dependency | Purpose |
|---|---|
| `spring-boot-starter-web` | Spring MVC and HTTP API development |
| `spring-boot-starter-data-mongodb` | MongoDB data access |
| `spring-boot-starter-validation` | Validation of application data |
| `spring-boot-starter-actuator` | Health and operational endpoints |
| `spring-boot-starter-tomcat` with `providedRuntime` | Provides the servlet container during local WAR execution or development packaging while excluding its normal runtime inclusion in the external-server deployment |
| `spring-boot-starter-test` | Testing utilities and test framework support |

Spring Boot's dependency management supplies compatible versions for managed dependencies, so individual versions are not required for each starter.

### Important Liberty deployment note

This build targets WAR packaging for an external application server.

Before deployment, the project must include an appropriate Spring Boot servlet initializer and a compatible Liberty server configuration. The Liberty runtime and enabled features must support the application's Spring Boot and Jakarta Servlet requirements.

The current build file alone does not complete the deployment configuration.

## 5.5 Generate the Gradle Wrapper

If a compatible Gradle installation is available, run:

```powershell
gradle wrapper --gradle-version 8.10.2
```

This generates the wrapper scripts, wrapper JAR, and wrapper properties.

The wrapper should be checked into Git so that developers and build environments can use the project-defined Gradle version.

Do not manually manufacture the wrapper JAR or scripts.

## 5.6 Verify the Wrapper

Run:

```powershell
.\gradlew.bat --version
```

Confirm that the reported Gradle version is `8.10.2`.

Then run:

```powershell
.\gradlew.bat tasks
```

This verifies that Gradle can evaluate the project configuration and list its tasks.

Inspect the repository files:

```powershell
Get-ChildItem
```

## 5.7 Expected Structure

```text
epims-eps-core/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
│       └── java/
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

The `gradle` wrapper directory and scripts appear after wrapper generation.

## 5.8 Common Problems

### `gradle` is not recognized

Gradle is not available through the current PATH or is not installed.

Do not create wrapper files by hand. A Gradle distribution must be installed or otherwise made available to generate the wrapper.

### Gradle uses the wrong Java version

Check:

```powershell
java -version
javac -version
gradle --version
```

The Java runtime shown by Gradle may differ from the Java version reported by another terminal command. Check `JAVA_HOME` and the configured Gradle JVM if necessary.

### Plugin or dependency resolution fails

Check network connectivity, proxy settings, repository access, and the reported error before changing dependency versions.

### Gradle cannot find the wrapper

Run the wrapper generation command from the directory containing `settings.gradle.kts` and `build.gradle.kts`.

## 5.9 Completion Checklist

- [ ] `settings.gradle.kts` created.
- [ ] `build.gradle.kts` created.
- [ ] Java 21 configuration added.
- [ ] Spring Boot and dependency-management plugins configured.
- [ ] MongoDB and other application dependencies declared.
- [ ] WAR packaging configured.
- [ ] Gradle Wrapper generated.
- [ ] Wrapper version verified.
- [ ] Gradle tasks successfully listed.
- [ ] README updated.

**Status:** Complete only after the applicable verification commands succeed.

## 5.10 Next Step

**Step 4 — Create the Spring Boot application entry point and package structure.**

This step will introduce the main application class, explain the package structure, and prepare the application for external deployment on IBM WebSphere Liberty.

# Step 4 — Create the Spring Boot Application Entry Point

## 6.1 Objective

Create the main Spring Boot application class and establish the initial enterprise package structure for the EPS backend.

This step prepares the application for further REST API development and external deployment on IBM WebSphere Liberty.

## 6.2 Package Structure

The application uses the base package:

`com.epims.pharmacy.eps`

The directory structure is:

```text
src/main/java/com/epims/pharmacy/eps/
├── EpsApplication.java
├── config/
├── controller/
├── service/
├── repository/
├── model/
└── exception/
```

### Package Responsibilities

- `config`: application and integration configuration.
- `controller`: REST endpoints and HTTP request handling.
- `service`: business logic and workflow orchestration.
- `repository`: database access.
- `model`: domain objects and data models.
- `exception`: custom exceptions and error handling.

The main application class resides in the root package so that Spring's default component scanning can discover components in the subpackages.

## 6.3 Create the Package Directories

Run from the repository root in PowerShell:

```powershell
$base = "src\main\java\com\epims\pharmacy\eps"

$packages = @(
    "",
    "config",
    "controller",
    "service",
    "repository",
    "model",
    "exception"
)

foreach ($package in $packages) {
    New-Item -ItemType Directory -Force `
        -Path (Join-Path $base $package) | Out-Null
}
```

### Explanation

- `$base` stores the common Java package directory.
- `$packages` lists the required package directories.
- `foreach` iterates over each package.
- `Join-Path` constructs the directory path.
- `New-Item -ItemType Directory` creates each directory.
- `-Force` allows existing directories to be reused.

## 6.4 Create `EpsApplication.java`

File location:

`src/main/java/com/epims/pharmacy/eps/EpsApplication.java`

Source code:

```java
package com.epims.pharmacy.eps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class EpsApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(EpsApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(EpsApplication.class, args);
    }
}
```

## 6.5 Understand the Application Class

### Package declaration

The package declaration identifies the namespace of the application class and must correspond to its source directory.

### `@SpringBootApplication`

This annotation enables Spring Boot configuration, auto-configuration, and component scanning.

Spring Boot can discover eligible components in the base package and its subpackages, subject to the component scanning configuration.

### `SpringBootServletInitializer`

This class supports bootstrapping Spring Boot applications in a traditional servlet container when packaged appropriately as a WAR.

### `configure()`

The method registers `EpsApplication` as the source for the Spring application when the external servlet container initializes it.

### `main()`

The main method allows the application to be launched using the standard Spring Boot startup mechanism.

## 6.6 Compile the Application

Run:

```powershell
.\gradlew.bat clean compileJava
```

### Expected Result

The Gradle build completes successfully with:

`BUILD SUCCESSFUL`

This confirms that Gradle can compile the Java application against the configured dependencies.

Compilation does not prove that the application starts successfully or deploys correctly to Liberty; those will be verified in subsequent steps.

## 6.7 Troubleshooting

### Package and directory mismatch

Ensure the package declaration is:

`package com.epims.pharmacy.eps;`

and the file is located under:

`src/main/java/com/epims/pharmacy/eps/`

### Spring Boot imports cannot be resolved

Check that `spring-boot-starter-web` is present in `build.gradle.kts` and that Gradle dependency resolution completes successfully.

### Compilation fails

Review the complete Gradle error. Check the Java version, source file location, package declaration, imports, and dependency configuration before modifying the build.

## 6.8 Completion Checklist

- [ ] Package directories created.
- [ ] `EpsApplication.java` created.
- [ ] `@SpringBootApplication` configured.
- [ ] `SpringBootServletInitializer` configured.
- [ ] `main()` method added.
- [ ] `.\gradlew.bat clean compileJava` succeeds.
- [ ] Detailed Step 4 documentation appended to `README.md`.

**Status:** Complete after all applicable checks succeed.

## 6.9 Next Step

**Step 5 — Add application configuration and prepare the Liberty deployment descriptor.**

The next step will introduce the initial application configuration and the server-side settings required to deploy the EPS backend to IBM WebSphere Liberty.

# EPIMS EPS Core — Backend Setup

## Step 5 — Configure IBM WebSphere Liberty

### 5.1 Objective

Configure the EPS backend for deployment to IBM WebSphere Liberty. The application will use Spring Boot, Java 21, Gradle, and MongoDB.

### 5.2 Create the application configuration

Create or update:

`src/main/resources/application.yml`

```yaml
spring:
  application:
    name: epims-eps-core

  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/epims-eps}

server:
  port: 9080

management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: never

info:
  app:
    name: EPIMS EPS Core
    description: Pharmacy order management backend
    version: 0.1.0
```

**Configuration explanation**

- `spring.application.name`: Identifies the EPS application.
- `spring.data.mongodb.uri`: Defines the MongoDB connection URI. `MONGODB_URI` can override the default value.
- `server.port`: Sets the application port to `9080` when running with an embedded server. Liberty's HTTP endpoint is also configured on port `9080`; the actual port arrangement must be validated when deploying the WAR to Liberty.
- `management.endpoints`: Exposes basic Actuator health and information endpoints.

**Important:** The default URI is for local development without authentication. Once MongoDB authentication is enabled in Step 6, update the connection URI or set `MONGODB_URI` to include credentials. Do not commit passwords to Git.

### 5.3 Create the Liberty server configuration

At the repository root, create:

`liberty/server.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<server description="EPIMS EPS Core Server">

    <featureManager>
        <feature>servlet-6.0</feature>
        <feature>monitor-1.0</feature>
    </featureManager>

    <httpEndpoint
        id="defaultHttpEndpoint"
        host="*"
        httpPort="9080"
        httpsPort="9443" />

    <webApplication
        id="epims-eps-core"
        name="epims-eps-core"
        location="epims-eps-core.war"
        contextRoot="/eps" />

</server>
```

**Configuration explanation**

- `featureManager`: Enables the required Liberty features.
- `httpEndpoint`: Configures HTTP on port `9080` and HTTPS on port `9443`.
- `webApplication`: Configures Liberty to deploy `epims-eps-core.war` under the `/eps` context root.

This is the initial server configuration. The WAR file location, Liberty feature compatibility, Spring Boot deployment, and endpoint behavior must be verified during deployment testing.

### 5.4 Verify Java and Gradle

Open PowerShell in the repository root:

```powershell
java -version
.\gradlew.bat --version
```

Confirm that Java 21 is available and that the Gradle wrapper runs successfully.

### 5.5 Build the application

```powershell
.\gradlew.bat clean build -x test
```

This builds the project while skipping test execution. Tests should be run separately before treating the build as production-ready.

**Expected result:** The Gradle build completes successfully. The WAR artifact should be generated under `build/libs/` if the WAR task is configured correctly.

---

## Step 6 — Set Up MongoDB Using Docker

### 6.1 Objective

Run MongoDB locally in Docker for EPS development. Docker provides the MongoDB server; MongoDB Compass is the graphical interface used to inspect the database.

### 6.2 Verify Docker

Run:

```powershell
docker --version
docker info
docker ps
```

Confirm that Docker Desktop is running and that Docker commands complete successfully.

### 6.3 Download the MongoDB image

```powershell
docker pull mongo:8
```

This downloads the MongoDB 8 image.

### 6.4 Create persistent storage

```powershell
docker volume create epims-eps-core-data
```

The named volume preserves MongoDB data when the container is stopped or recreated, provided the volume itself is retained.

### 6.5 Start MongoDB

For the initial unauthenticated setup, the container was created with:

```powershell
docker run -d `
  --name epims-mongodb `
  -p 127.0.0.1:27017:27017 `
  -v epims-eps-core-data:/data/db `
  mongo:8
```

**Security note:** This command starts MongoDB without authentication. It is only an initial local-development configuration. Authentication is addressed in Step 6.4.

The port binding restricts host access to the local computer rather than all network interfaces.

### 6.6 Verify the container

```powershell
docker ps -a --filter "name=epims-mongodb"
docker volume ls --filter "name=epims-eps-core-data"
docker inspect epims-mongodb --format '{{json .Mounts}}'
```

Confirm that:

- The container is named `epims-mongodb`.
- The image is `mongo:8`.
- Port `127.0.0.1:27017` is mapped to container port `27017`.
- `epims-eps-core-data` is mounted at `/data/db`.

Check MongoDB connectivity:

```powershell
docker exec epims-mongodb mongosh --quiet --eval "db.runCommand({ ping: 1 })"
```

Expected output includes:

```json
{ "ok": 1 }
```

### 6.7 Start and stop MongoDB

Stop the container:

```powershell
docker stop epims-mongodb
```

Start it again:

```powershell
docker start epims-mongodb
```

Check its status:

```powershell
docker ps
```

Stopping the container does not delete the named data volume.

**Important:** Do not use `docker rm -v` or delete `epims-eps-core-data` when reconfiguring MongoDB.

---

## Step 6.1 — Install MongoDB Compass and Connect to Docker

### 6.1.1 What is MongoDB Compass?

MongoDB Compass is a graphical user interface (GUI) for connecting to MongoDB, viewing databases and collections, running queries, and inspecting documents.

Compass does not replace MongoDB Server. Our MongoDB server runs inside Docker.

Download Compass from the official website:

https://www.mongodb.com/products/tools/compass

Install and open MongoDB Compass.

### 6.1.2 Verify the MongoDB container

```powershell
docker ps
```

If the container exists but is stopped:

```powershell
docker start epims-mongodb
```

### 6.1.3 Connect Compass

During the initial unauthenticated setup, use:

```text
mongodb://127.0.0.1:27017
```

Click **Connect**.

This connection string applies only while authentication is disabled. After authentication is enforced, use the authenticated connection URI described in Step 6.4.

### 6.1.4 Create the EPS database

In Compass:

1. Connect to MongoDB.
2. Click **Create database**.
3. Enter database name `epims-eps`.
4. Enter collection name `orders`.
5. Click **Create database**.

The initial database structure will be:

```text
epims-eps
└── orders
```

MongoDB creates databases and collections when data is first written, so they may not appear in the database list until created or populated.

### 6.1.5 Verify using the MongoDB shell

```powershell
docker exec -it epims-mongodb mongosh
```

Then run:

```javascript
show dbs
use epims-eps
show collections
exit
```

Expected: `epims-eps` appears after it contains data, and `orders` appears if the collection has been created.

---

## Step 6.2 — Inspect Existing MongoDB Users and Storage

Before enabling authentication, inspect the existing container and volume to avoid accidental data loss.

```powershell
docker ps -a --filter "name=epims-mongodb"
docker volume ls --filter "name=epims-eps-core-data"
docker inspect epims-mongodb --format '{{json .Mounts}}'
```

Open the MongoDB shell:

```powershell
docker exec -it epims-mongodb mongosh
```

Inspect users in the administrative database:

```javascript
use admin
db.getUsers()
exit
```

The initial inspection showed:

```javascript
{ users: [], ok: 1 }
```

This confirmed that no users were present in the `admin` database at that point.

---

## Step 6.3 — Create the MongoDB Administrator

### 6.3.1 Objective

Create an administrative user before enforcing authentication.

### 6.3.2 Create the administrator

Open the MongoDB shell:

```powershell
docker exec -it epims-mongodb mongosh
```

Run:

```javascript
use admin
```

Create the user:

```javascript
db.createUser({
  user: "epimsAdmin",
  pwd: passwordPrompt(),
  roles: [
    { role: "root", db: "admin" }
  ]
})
```

Enter a strong, unique password when prompted.

**Never store the password in this README, source code, Git, or screenshots.** Keep it in a password manager.

### 6.3.3 Verify the administrator

```javascript
db.getUsers()
```

The successful creation returned:

```javascript
{ ok: 1 }
```

The user listing confirmed:

- Username: `epimsAdmin`
- Authentication database: `admin`
- Role: `root`

Exit the shell:

```javascript
exit
```

### 6.3.4 Security considerations

The `root` role grants full administrative privileges. Use this account only for administrative tasks, not for the EPS application.

**Creating a user does not by itself enforce authentication.** Until MongoDB is restarted with authentication enabled, connections may still be possible without credentials.

---

## Step 6.4 — Enable MongoDB Authentication

### Objective

Enable MongoDB access control while retaining the existing EPIMS database and administrator account.

### 6.4.1 Stop and remove the old container

```powershell
docker stop epims-mongodb
docker rm epims-mongodb
```

The container is removed, but the persistent `epims-eps-core-data` volume is retained.

**Never use `docker rm -v` or delete the data volume during this procedure.**

### 6.4.2 Recreate MongoDB with authentication enabled

```powershell
docker run -d `
  --name epims-mongodb `
  --restart unless-stopped `
  -p 127.0.0.1:27017:27017 `
  -v epims-eps-core-data:/data/db `
  mongo:8 mongod --auth
```

**Configuration explanation:**

- `--name epims-mongodb`: Sets the container name.
- `--restart unless-stopped`: Restarts the container automatically unless it has been explicitly stopped.
- `127.0.0.1:27017:27017`: Restricts the published port to the local computer.
- `epims-eps-core-data:/data/db`: Reuses the existing database files.
- `mongod --auth`: Starts MongoDB with authentication enforcement enabled.

### 6.4.3 Verify the container

```powershell
docker ps --filter "name=epims-mongodb"
docker logs --tail 50 epims-mongodb
```

Confirm that the container remains running and MongoDB is ready to accept connections.

### 6.4.4 Verify unauthenticated access is restricted

```powershell
docker exec epims-mongodb mongosh --quiet --eval "db.getSiblingDB('admin').runCommand({ usersInfo: 1 })"
```

Expected: An authorization error such as `Unauthorized`.

### 6.4.5 Verify administrator authentication

```powershell
docker exec -it epims-mongodb mongosh --username epimsAdmin --authenticationDatabase admin --password
```

Enter the password interactively, then run:

```javascript
db.runCommand({ connectionStatus: 1 })
```

Expected: `authenticatedUsers` contains `epimsAdmin` with authentication database `admin`.

Exit:

```javascript
exit
```

### Security notes

- Keep the administrator password in a password manager.
- Do not store credentials in Git or the README.
- Use a separate, least-privilege account for the EPS application.
- Localhost port binding does not replace authentication, network security, or TLS in a deployed environment.
- Authentication is not the same as encryption; production deployments require appropriate transport security and secret management.

**Completion criteria:** MongoDB is running with `--auth`, unauthenticated administrative access is rejected, authenticated administrator access succeeds, and the original data volume is retained.

## Step 6.5 — Create and Verify the Restricted EPS Application User

### 6.5.1 Objective

Create a dedicated MongoDB user for the EPIMS EPS Core Spring Boot application. The application must use this restricted account instead of the MongoDB administrator account.

### 6.5.2 Application User Configuration

| Property | Value |
|---|---|
| Username | `epsAppUser` |
| Database | `epims-eps` |
| Authentication database | `epims-eps` |
| Role | `readWrite` |
| Permission scope | `epims-eps` database only |

### 6.5.3 Connect as the MongoDB Administrator

Open PowerShell and run:

```powershell
docker exec -it epims-mongodb mongosh --username epimsAdmin --authenticationDatabase admin --password
```

Enter the administrator password when prompted.

Select the EPS database:

```javascript
use epims-eps
```

Expected output:

```text
switched to db epims-eps
```

### 6.5.4 Create the EPS Application User

Execute:

```javascript
db.createUser({
  user: "epsAppUser",
  pwd: passwordPrompt(),
  roles: [
    { role: "readWrite", db: "epims-eps" }
  ]
})
```

Enter a strong, unique password when prompted. Store it securely in a password manager.

Expected result:

```javascript
{ ok: 1 }
```

### 6.5.5 Verify the Application User

While still in the `epims-eps` database, run:

```javascript
db.getUsers()
```

Verify that `epsAppUser` exists with the `readWrite` role on `epims-eps`.

Exit the administrator session:

```javascript
exit
```

### 6.5.6 Test Application User Authentication

Connect using the application account:

```powershell
docker exec -it epims-mongodb mongosh --username epsAppUser --authenticationDatabase epims-eps --password
```

Enter the application user's password when prompted.

Select the EPS database explicitly:

```javascript
use epims-eps
```

Expected output:

```text
switched to db epims-eps
```

### 6.5.7 Verify the Authenticated Identity

Execute:

```javascript
db.runCommand({ connectionStatus: 1 })
```

Verify that the output contains:

```javascript
authenticatedUsers: [
  { user: "epsAppUser", db: "epims-eps" }
]
```

The authenticated role should include `readWrite` on `epims-eps`.

### 6.5.8 Test Database Write Access

Insert a temporary test document:

```javascript
db.auth_test.insertOne({
  message: "EPS application authentication verified",
  createdAt: new Date()
})
```

Expected result: the operation succeeds and returns `acknowledged: true`.

Verify the document:

```javascript
db.auth_test.find()
```

Delete the temporary document:

```javascript
db.auth_test.deleteMany({})
```

Expected result: `deletedCount: 1`.

Exit the MongoDB shell:

```javascript
exit
```

### 6.5.9 Security Considerations

- Use `epsAppUser` for routine EPS database operations.
- Never use `epimsAdmin` as the application's database account.
- Do not store MongoDB passwords in the README, source code, or Git repository.
- Configure application credentials through environment variables or a secure secret-management system.
- The `readWrite` role is limited to the `epims-eps` database and does not grant MongoDB server-administration privileges.

**Completion criteria:** The application user authenticates successfully, has the expected `readWrite` role, and can insert, query, and delete documents in `epims-eps`.

---

## Step 6.6 — Connect MongoDB Compass with Authentication

### 6.6.1 Objective

Connect MongoDB Compass to the authenticated MongoDB Docker container using the restricted EPS application account.

### 6.6.2 Open MongoDB Compass

Install and open MongoDB Compass:

https://www.mongodb.com/products/tools/compass

### 6.6.3 Configure the connection

Create a new connection and use:

```text
mongodb://epsAppUser@127.0.0.1:27017/epims-eps?authSource=epims-eps
```

Enter the `epsAppUser` password when prompted.

**Connection parameters:**

| Parameter | Value |
|---|---|
| Username | `epsAppUser` |
| Host | `127.0.0.1` |
| Port | `27017` |
| Default database | `epims-eps` |
| Authentication database | `epims-eps` |
| Authentication mechanism | Negotiated by MongoDB driver |

### 6.6.4 Verify the connection

1. Click **Connect**.
2. Confirm that the connection succeeds.
3. Locate the `epims-eps` database.
4. Expand the database and verify its collections.
5. If the `orders` collection was previously created, verify that it is listed.

### 6.6.5 Security considerations

- Use `epsAppUser` for normal database access.
- Do not use the `epimsAdmin` account for routine application access.
- Do not commit passwords or credential-bearing connection strings to Git.
- The connection uses local MongoDB access; production deployments require appropriate network restrictions, TLS, and secure credential management.

**Completion criteria:** Compass connects successfully using `epsAppUser` and can access the `epims-eps` database.

# Step 7 — Connect the Local Repository to GitHub and Push Changes

## 7.1 Objective

Connect the local `epims-eps-core` Git repository to the existing GitHub repository, commit the EPIMS backend setup, preserve the remote repository's initial history, and push the changes to the `main` branch.

**Repository details**

| Property | Value |
|---|---|
| Local repository | `epims-eps-core` |
| Remote name | `origin` |
| Remote repository | `https://github.com/JoshuaPraveen99/epims-eps-core.git` |
| Final branch | `main` |
| Git hosting | GitHub |
| Status | Completed |

## 7.2 Navigate to the Local Repository

Open PowerShell and navigate to the project directory:

```powershell
cd C:\Users\Joshua\Documents\EPIMS\epims-eps-core
```

Verify the current directory:

```powershell
Get-Location
```

## 7.3 Initialize and Configure Git

Git was initialized locally during the initial repository setup:

```powershell
git init
```

Check the repository status:

```powershell
git status
```

The project initially had no local commits, and its files appeared as untracked.

## 7.4 Configure the GitHub Remote

The GitHub repository had already been created separately.

Add the remote:

```powershell
git remote add origin https://github.com/JoshuaPraveen99/epims-eps-core.git
```

Verify the remote:

```powershell
git remote -v
```

Expected output:

```text
origin  https://github.com/JoshuaPraveen99/epims-eps-core.git (fetch)
origin  https://github.com/JoshuaPraveen99/epims-eps-core.git (push)
```

The `origin` remote identifies the GitHub repository used to fetch and push changes.

## 7.5 Fetch Existing Remote History

The GitHub repository already contained an initial commit and the following branches:

- `main`
- `Nova/InitialSetup`
- `release/nova`

Fetch the remote branch information:

```powershell
git fetch origin
```

Inspect the remote branches:

```powershell
git branch -a
```

Inspect the commit history:

```powershell
git log --oneline --decorate --graph --all -10
```

The initial remote commit was:

```text
cf46bfe Initial commit
```

Because the local repository had no commits and the remote already had its own initial commit, the existing remote history needed to be preserved.

## 7.6 Create a `.gitignore` File

Create `.gitignore` in the repository root to exclude generated files and local secrets.

```gitignore
# Gradle generated files
.gradle/
build/

# IntelliJ IDEA
.idea/
*.iml
out/

# Eclipse
.classpath
.project
.settings/

# Operating system
.DS_Store
Thumbs.db

# Logs
*.log

# Local environment and secrets
.env
.env.*
!.env.example

# Local configuration containing credentials
application-local.yml
application-local.yaml
```

Verify the untracked files:

```powershell
git status --short
```

The generated `.gradle/` and `build/` directories should be excluded from the untracked file listing.

**Security requirement:** Review the project files before committing. Never commit database passwords, API tokens, private keys, or other credentials.

## 7.7 Stage and Commit the Project Files

Stage the intended project files:

```powershell
git add .gitignore build.gradle.kts settings.gradle.kts gradle gradlew gradlew.bat liberty readme.md src
```

Review the staged changes:

```powershell
git status
git diff --cached --stat
git diff --cached
```

After verifying the files and confirming that no secrets are staged, create the commit:

```powershell
git commit -m "Set up EPIMS EPS core backend"
```

The local project commit was:

```text
e698ff6 Set up EPIMS EPS core backend
```

## 7.8 Integrate the Existing Remote History

Rename the local branch from `master` to `main`:

```powershell
git branch -M main
```

Merge the existing remote history without discarding either history:

```powershell
git merge origin/main --allow-unrelated-histories
```

This was necessary because the local project and the existing GitHub initial commit had been created independently.

The resulting merge commit was:

```text
ac12dd5 Merge remote-tracking branch 'origin/main'
```

## 7.9 Push to GitHub

Push the local `main` branch and configure upstream tracking:

```powershell
git push -u origin main
```

The `-u` option establishes `origin/main` as the upstream branch, so future pushes can generally use:

```powershell
git push
```

Fetch future changes with:

```powershell
git pull
```

Before pulling, ensure you understand any local changes or merge conflicts.

## 7.10 Verify the Final Repository State

Run:

```powershell
git status
```

Expected state:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

Inspect the latest commits:

```powershell
git log --oneline --decorate -5
```

The verified history included:

```text
ac12dd5 (HEAD -> main, origin/main) Merge remote-tracking branch 'origin/main'
e698ff6 Set up EPIMS EPS core backend
cf46bfe (origin/release/nova, origin/Nova/InitialSetup) Initial commit
```

This confirms that the local project commit and the original GitHub history were preserved.

## 7.11 Git Workflow for Future Changes

For subsequent changes to `epims-eps-core`, use the following workflow:

### Check the current state

```powershell
git status
git branch --show-current
```

### Stage and review changes

```powershell
git add <file-or-directory>
git diff --cached
```

Replace `<file-or-directory>` with the specific files or directories being committed.

### Commit changes

```powershell
git commit -m "Describe the change"
```

### Push changes

```powershell
git push
```

Use descriptive commit messages and review staged files for secrets before every commit.

## 7.12 Completion Checklist

- [x] Local Git repository initialized.
- [x] GitHub remote configured.
- [x] Existing remote branches fetched.
- [x] `.gitignore` created.
- [x] EPIMS project files committed.
- [x] Local branch renamed to `main`.
- [x] Existing remote history preserved.
- [x] Changes pushed to GitHub.
- [x] Local branch synchronized with `origin/main`.
- [x] Working tree verified clean.

**Step 7 status: Completed.**

GitHub repository: https://github.com/JoshuaPraveen99/epims-eps-core

---
# Step 8 — Design the EPIMS Centralized Configuration System

## 8.1 Objective

Design a centralized configuration system for EPIMS microservices. Configuration must be version-controlled, service-specific, environment-aware, and secure.

The design separates non-sensitive configuration from sensitive credentials and supports authorized credential updates through an API.

## 8.2 Architecture Components

1. **`epims-config`:** Private Git repository containing shared and service-specific YAML configuration with placeholders and non-sensitive defaults only.
2. **Spring Cloud Config Server:** Retrieves configuration from Git and serves it to Spring Boot microservices.
3. **Secret manager:** Stores database passwords, API tokens, signing keys, and other sensitive values.
4. **Credential Management API:** Provides an authenticated and authorized mechanism to update secrets.
5. **EPIMS microservices:** Consume the configuration and secrets required for their individual responsibilities.
6. **`epims-infrastructure`:** Holds deployment and infrastructure configuration.

OpenBao and HashiCorp Vault will be evaluated before selecting the secret manager.

## 8.3 Planned Repository Structure

```text
epims-config/
├── README.md
├── application.yml
├── epims-eps-core.yml
├── epims-eps-core-dev.yml
├── epims-eps-core-test.yml
├── epims-epr-core.yml
├── epims-sbmo-core.yml
├── epims-notification-service.yml
├── epims-api-gateway.yml
└── epims-digital-core.yml
```

Files will be introduced as the corresponding services are implemented.

## 8.4 Configuration and Secret Separation

Git will contain only non-sensitive properties, defaults, and placeholders. Actual passwords, API tokens, private keys, and other credentials must never be committed.

Illustrative configuration:

```yaml
epims:
  mongodb:
    host: ${MONGODB_HOST}
    port: ${MONGODB_PORT:27017}
    database: ${MONGODB_DATABASE}
    username: ${MONGODB_USERNAME}
    password: ${MONGODB_PASSWORD}
```

The placeholders must be resolved at runtime through a configured secret-management or environment-injection mechanism.

Private repository visibility does not make committed credentials safe. Accidentally exposed secrets must be rotated.

## 8.5 Secret Manager Requirements

The selected secret manager must support:

- Secure secret storage and access policies.
- Service-specific authentication and least-privilege access.
- Secret recovery or versioning where supported.
- Audit logging.
- Secure secret updates through an API.
- Local Docker development and future deployment.

The final selection will consider licensing, operational complexity, security requirements, and the free-tier deployment objective.

## 8.6 Credential Management API

An illustrative endpoint is:

```http
PUT /api/v1/configuration/secrets/{service}/{secretName}
```

The final API contract will be established during implementation.

Required controls:

1. Authenticate the caller.
2. Authorize changes for the specified service and secret.
3. Validate the request.
4. Update the secret manager.
5. Record an audit event without logging secret values.
6. Avoid returning secret values in API responses.
7. Coordinate a safe application refresh or credential-rotation procedure.

The API must use appropriate TLS, authorization, validation, and audit controls.

## 8.7 Runtime Configuration Flow

1. A service identifies its application name and active environment.
2. Spring Cloud Config retrieves the corresponding Git-managed configuration.
3. The service obtains its authorized secrets from the secret manager.
4. Spring resolves properties and initializes the required integrations.
5. Credential updates trigger a controlled refresh or connection-rotation process.

Updating a stored secret does not automatically update existing database connections. Credential rotation must be implemented and tested explicitly.

## 8.8 Security Requirements

- Restrict access to Git and the secret manager.
- Use distinct identities and least-privilege policies for services.
- Do not use administrator credentials for routine application operations.
- Protect sensitive traffic with TLS in deployed environments.
- Keep secrets out of Git, logs, API responses, and error messages.
- Implement auditing, backups, recovery, and credential rotation.
- Verify software licensing and hosting constraints before finalizing component choices.

## 8.9 Implementation Roadmap

| Step | Task | Status |
|---|---|---|
| 8 | Design centralized configuration and secret management | In progress |
| 9 | Create the private `epims-config` repository | Pending |
| 10 | Define repository structure and YAML placeholders | Pending |
| 11 | Select and run the secret manager locally | Pending |
| 12 | Implement Spring Cloud Config Server | Pending |
| 13 | Connect EPS to centralized configuration | Pending |
| 14 | Implement and secure the credential management API | Pending |
| 15 | Test credential rotation and failure recovery | Pending |

**Completion criteria:** The architecture, repository responsibilities, secret-management approach, API responsibilities, and implementation roadmap are documented.

**Next:** Step 9 — Initialize the `epims-config` GitHub repository.

# Step 9 — Initialize the Centralized Configuration Repository

## 9.1 Create the GitHub Repository

**Objective:** Create a separate private Git repository for shared and service-specific, non-sensitive configuration used by EPIMS microservices.

Repository details:

- Repository name: `epims-config`
- Owner: `JoshuaPraveen99`
- Visibility: Private
- Description: Centralized configuration for EPIMS pharmacy microservices
- Initial file: `README.md`

Procedure:

1. Open https://github.com/new.
2. Enter `epims-config` as the repository name.
3. Set the description to `Centralized configuration for EPIMS pharmacy microservices`.
4. Select Private visibility.
5. Enable Add a README file.
6. Leave the `.gitignore` template and license unselected.
7. Create the repository.

## 9.2 Clone the Repository Locally

Open PowerShell and run:

```powershell
cd C:\Users\Joshua\Documents\EPIMS
git clone https://github.com/JoshuaPraveen99/epims-config.git
cd epims-config
```

## 9.3 Verify the Remote

```powershell
git remote -v
```

Expected remote:

```text
origin  https://github.com/JoshuaPraveen99/epims-config.git (fetch)
origin  https://github.com/JoshuaPraveen99/epims-config.git (push)
```

## 9.4 Verify the Branch and Working Tree

```powershell
git branch --show-current
git status
```

Confirm that the repository is on `main` and the working tree is clean.

## 9.5 Security Requirements

- Store only non-sensitive configuration and placeholders in Git.
- Never commit actual database passwords, API tokens, private keys, or other credentials.
- Store sensitive values separately in the selected secret-management system.
- Restrict repository access to authorized contributors.

**Completion criteria:** The private `epims-config` repository exists on GitHub, has been cloned locally, and its Git remote, branch, and working-tree status have been verified.

**Status:** Pending confirmation.

**Next:** Step 10 — Define the configuration repository structure and create the initial placeholder-only YAML files.

### Step 10.1 — Create the Initial Configuration Files

**Objective:** Create shared and EPS-specific YAML configuration files in the `epims-config` repository.

**Repository location:**

`C:\Users\Joshua\Documents\EPIMS\epims-config`

#### Create `application.yml`

This file contains shared configuration for EPIMS microservices.

```yaml
spring:
  application:
    name: ${SPRING_APPLICATION_NAME}

management:
  endpoints:
    web:
      exposure:
        include: health,info
```

#### Create `epims-eps-core.yml`

This file contains the MongoDB configuration properties required by the EPS Core service.

```yaml
spring:
  data:
    mongodb:
      host: ${MONGODB_HOST}
      port: ${MONGODB_PORT}
      database: ${MONGODB_DATABASE}
      username: ${MONGODB_USERNAME}
      password: ${MONGODB_PASSWORD}
      authentication-database: ${MONGODB_AUTH_DATABASE}
```

#### Verify the files

```powershell
Get-ChildItem
git status --short
```

Expected files include:

- `README.md`
- `application.yml`
- `epims-eps-core.yml`

The YAML files may appear as untracked files until they are staged and committed.

#### Security requirements

- Never commit actual MongoDB credentials or other secrets.
- Keep sensitive values in the selected secret manager.
- Configure placeholder resolution when integrating Spring Cloud Config with the microservices.
- Do not consider the application ready for database access until secret resolution and authenticated connectivity have been tested.

**Completion criteria:** Both YAML files exist in the `epims-config` repository and contain placeholders instead of actual credentials.

**Status:** Pending confirmation.

## Step 10.2 — Inspect EPS and Central Configuration

**Objective:** Review the existing EPS Gradle dependencies and application configuration before connecting the service to the centralized configuration repository.

### Inspection commands

Run from the EPS repository:

```powershell
Get-Content .\build.gradle.kts
Get-Content .\src\main\resources\application.yml
```

Run from the `epims-config` repository:

```powershell
Get-ChildItem
Get-Content .\application.yml
Get-Content .\epims-eps-core.yml
```

### Review checklist

- [ ] Confirm the Spring Boot and MongoDB dependencies in `build.gradle.kts`.
- [ ] Review the existing EPS `application.yml`.
- [ ] Confirm the centralized configuration files exist.
- [ ] Verify that the centralized YAML files contain placeholders rather than real credentials.
- [ ] Identify any existing configuration that must be preserved.

### Security notes

- Never commit real passwords, tokens, or private connection strings to Git.
- Do not paste secrets into README files or chat messages.
- Central configuration and secret management are separate responsibilities. The Config Server connection and secure credential retrieval will be configured in subsequent steps.

**Status:** Inspection pending; complete this section after reviewing the files.

## Step 10.3 — Create the Central Configuration Server Repository

**Objective:** Create a dedicated repository for the Spring Cloud Config Server application.

### Repository details

- Repository: `epims-config-server`
- Owner: `JoshuaPraveen99`
- Visibility: Private
- Purpose: Serve centralized application configuration from the `epims-config` Git repository.

### Commands

```powershell
cd C:\Users\swoni\Documents\epims

git clone https://github.com/JoshuaPraveen99/epims-config-server.git

cd .\epims-config-server

git remote -v
git status
```

### Architecture separation

- `epims-config`: Stores shared and service-specific YAML configuration.
- `epims-config-server`: Hosts the Spring Boot application that reads and serves configuration.
- `epims-eps-core`: Consumes centralized configuration when the Config Server integration is implemented.

Secrets must remain outside Git and must eventually be supplied through the approved secret-management mechanism.

**Status:** Pending repository creation and clone verification.

## Step 10.4 — Create the Config Server Project Skeleton

**Status:** Completed

### Objective

Create the initial Spring Boot project for `epims-config-server` using IntelliJ IDEA and Gradle Kotlin DSL.

### Technology stack

- Java: 25
- Spring Boot: 4.0.9
- Spring Cloud release train: 2025.1.4
- Build system: Gradle Kotlin DSL
- Application: Spring Cloud Config Server

### Directory structure

```text
epims-config-server/
├── .gradle/
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── build.gradle.kts
├── settings.gradle.kts
└── src/
    └── main/
        └── java/
            └── com/
                └── epims/
                    └── configserver/
                        └── ConfigServerApplication.java
```

*Note: `.gradle/` is generated Gradle state and should not be committed to Git. The wrapper scripts, wrapper JAR, and wrapper properties should be committed.*

### 1. Gradle project settings

File: `settings.gradle.kts`

```kotlin
rootProject.name = "epims-config-server"
```

This defines the Gradle project name.

### 2. Gradle build configuration

File: `build.gradle.kts`

The build configuration declares:

- The Java plugin.
- Spring Boot plugin version `4.0.9`.
- Spring dependency management plugin.
- Java toolchain version `25`.
- Maven Central as the dependency repository.
- Spring Cloud BOM version `2025.1.4`.
- Spring Web and Spring Cloud Config Server dependencies.
- Spring Boot test dependency and JUnit Platform configuration.

### 3. Java application entry point

File: `src/main/java/com/epims/configserver/ConfigServerApplication.java`

```java
package com.epims.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

**Purpose:**

- `@SpringBootApplication` enables Spring Boot application configuration and component scanning.
- `@EnableConfigServer` enables Spring Cloud Config Server functionality.
- `main()` starts the Spring Boot application.

### 4. Gradle Wrapper setup

The project was loaded as a Gradle project in IntelliJ IDEA, and the Gradle Wrapper files were created or configured.

The wrapper provides a consistent Gradle version without requiring every developer to install Gradle globally.

Wrapper files:

- `gradlew` — Unix-like operating systems.
- `gradlew.bat` — Windows.
- `gradle/wrapper/gradle-wrapper.jar` — wrapper implementation.
- `gradle/wrapper/gradle-wrapper.properties` — specifies the Gradle distribution.

Verify the configured version and wrapper operation using:

```powershell
.\gradlew.bat --version
```

The actual Gradle version is determined by the `distributionUrl` in `gradle-wrapper.properties`.

### 5. Architecture and security

The Config Server will serve configuration from the private `epims-config` repository. The Git connection and authentication have not yet been configured.

No database passwords, Git tokens, or other secrets should be committed to this repository.

### Completion checklist

- [x] Created the `epims-config-server` repository.
- [x] Configured `settings.gradle.kts`.
- [x] Configured `build.gradle.kts`.
- [x] Created `ConfigServerApplication.java`.
- [x] Created or configured the Gradle Wrapper through IntelliJ IDEA.
- [ ] Verify the wrapper version and run a successful Gradle build.
- [ ] Configure the Config Server to access the private Git repository.
- [ ] Configure secure access to the Config Server.

**Next step:** Verify Gradle compatibility and build the Config Server before configuring its Git repository connection.

## Step 10.5 — Verify Gradle Wrapper and Build the Config Server

**Objective:** Verify the Gradle Wrapper, confirm Java compatibility, and build the initial Config Server application.

### Commands

Run from the `epims-config-server` project root:

```powershell
java -version

Get-Content .\gradle\wrapper\gradle-wrapper.properties

.\gradlew.bat --version

.\gradlew.bat clean build

Get-ChildItem .\build\libs
```

### Verification checklist

- [ ] Confirm the project root is correct.
- [ ] Confirm the installed Java version.
- [ ] Confirm IntelliJ's Gradle JVM is configured correctly.
- [ ] Confirm the wrapper version supports Java 25.
- [ ] Confirm the Wrapper runs successfully.
- [ ] Confirm the Gradle build completes successfully.
- [ ] Confirm the application JAR exists under `build/libs`.

### Troubleshooting

If the build fails, capture the relevant error and resolve it before proceeding. Do not commit secrets or change the configuration repository connection during this step.

### Expected result

The Config Server project compiles and produces a JAR artifact.

**Status:** Pending verification.

## Step 10.6 — Configure SSH Authentication for the Central Configuration Repository

### Overview — How SSH Authentication Works

**Objective:** Understand how the EPIMS Config Server securely accesses the private `epims-config` GitHub repository using SSH authentication.

#### 1. Why SSH is required

The `epims-config` repository contains centralized YAML configuration for EPIMS microservices. Since the repository is private, the Config Server needs authenticated access to retrieve configuration files.

Instead of using a personal GitHub password or a personal access token, this setup uses a dedicated SSH key pair registered as a read-only deploy key.

#### 2. How SSH key authentication works

SSH uses a pair of cryptographic keys:

- **Private key:** Kept secret on the machine or in the deployment environment running the Config Server.
- **Public key:** Registered with GitHub on the `epims-config` repository.

The public and private keys are mathematically related. GitHub stores the public key and uses it to verify that a connection possesses the corresponding private key.

The private key is never sent to GitHub during normal SSH authentication.

#### 3. EPIMS SSH authentication flow

![Config Server SSH authentication flow](docs/diagrams/config-server-ssh-flow.png)

This diagram describes the intended architecture. The end-to-end flow will be verified after the Config Server's Git configuration is completed.

#### 4. Why a read-only deploy key is used

The SSH key is dedicated to the `epims-config` repository.

- It can be restricted to that repository.
- Write access remains disabled.
- It is separate from personal GitHub credentials.
- It can be replaced independently if compromised or during planned rotation.
- The private key can be provisioned securely to the Config Server environment.

**Important:** A deploy key is a repository-access credential, not a complete secret-management solution. MongoDB passwords and other application secrets must be managed separately.

#### 5. SSH security considerations

- Never commit private keys or passphrases to Git.
- Verify GitHub's SSH host-key fingerprint before trusting a new host key.
- Keep the deploy key read-only.
- Restrict filesystem permissions and access to the private key.
- Use a managed secret store or equivalent secure provisioning mechanism in production.
- Rotate or revoke the key when necessary.
- Protect the Config Server itself with authentication and network access controls.

---

### Step 10.6.1 — Create the SSH Directory and Generate a Dedicated SSH Key

**Objective:** Create the SSH directory and generate a dedicated SSH key pair for read-only access from the Config Server to the private `epims-config` repository.

#### 1. Create the SSH directory

Run in Windows PowerShell or the IntelliJ IDEA terminal:

```powershell
New-Item -ItemType Directory -Force -Path "$HOME\.ssh"
```

This creates the `.ssh` directory under the current Windows user's home directory if it does not already exist.

Expected location:

```text
C:\Users\swoni\.ssh
```

The `-Force` parameter allows the command to run when the directory already exists.

#### 2. Generate the SSH key pair

Run:

```powershell
ssh-keygen -t ed25519 -f "$HOME\.ssh\epims-config-readonly" -C "epims-config-server-readonly"
```

When prompted for a passphrase, press **Enter twice** to use an empty passphrase for this local development setup.

**Production note:** For production deployments, use a managed secret store or SSH agent with appropriate access controls. An unencrypted private key requires particularly careful filesystem and runtime protection.

#### 3. Verify the generated files

Run:

```powershell
Get-ChildItem "$HOME\.ssh\epims-config-readonly*"
```

The following files should exist:

```text
epims-config-readonly
epims-config-readonly.pub
```

#### 4. Understand the generated files

| File | Purpose |
|---|---|
| `epims-config-readonly` | Private key; must remain secret |
| `epims-config-readonly.pub` | Public key; registered with GitHub |

Display the public key only when preparing to register it:

```powershell
Get-Content "$HOME\.ssh\epims-config-readonly.pub"
```

To copy the public key to the clipboard:

```powershell
Get-Content "$HOME\.ssh\epims-config-readonly.pub" | Set-Clipboard
```

#### 5. Security requirements

- Never share the private key or commit it to Git.
- Register the public key as a **read-only deploy key** on the private `epims-config` repository.
- Do not enable write access for the deploy key.
- Keep the private key outside the application repositories.
- In deployment environments, provision the private key securely and restrict filesystem access to the Config Server process.

#### 6. Verification checklist

- [x] Created or verified the `.ssh` directory.
- [x] Generated the dedicated SSH key pair.
- [x] Confirmed that both key files exist.
- [x] Registered the public key with the `epims-config` repository.
- [x] Verified that the deploy key has read-only access.

**Status:** Completed.

---

### Step 10.6.2 — Register the SSH Public Key as a Read-Only Deploy Key

**Objective:** Grant the Config Server read-only SSH access to the private `epims-config` repository.

#### 1. Copy the public key

Run:

```powershell
Get-Content "$HOME\.ssh\epims-config-readonly.pub" | Set-Clipboard
```

This copies the public key to the clipboard. The private key is not copied.

#### 2. Register the key with GitHub

Open the `epims-config` repository:

https://github.com/JoshuaPraveen99/epims-config

Navigate to:

**Settings → Deploy keys → Add deploy key**

Enter the following details:

- **Title:** `epims-config-server-readonly`
- **Key:** Paste the contents of `epims-config-readonly.pub`.
- **Allow write access:** Leave unchecked.

Save the deploy key.

#### 3. Verify registration

Return to **Settings → Deploy keys** and confirm that:

- The deploy key is listed.
- The title is `epims-config-server-readonly`.
- Write access is disabled.

#### 4. Security requirements

- The public key may be registered with GitHub.
- Never upload or commit the private key.
- Keep the deploy key read-only.
- Use the key only for the intended repository access.

#### 5. Verification checklist

- [x] Deploy key appears in the repository's Deploy keys settings.
- [x] Write access is disabled.
- [x] Private key remains outside Git repositories.

**Status:** Completed.

---

### Step 10.6.3 — Test SSH Authentication and Repository Access

**Objective:** Verify that the dedicated SSH deploy key can authenticate with GitHub and read the private `epims-config` repository.

#### 1. Test SSH authentication

Run in PowerShell:

```powershell
ssh -i "$HOME\.ssh\epims-config-readonly" -o IdentitiesOnly=yes -T git@github.com
```

The command explicitly selects the dedicated private key rather than relying on other keys configured on the machine.

The first connection may display a host-key verification prompt.

**Security requirement:** Verify GitHub's published SSH host-key fingerprint against its official documentation before accepting an unfamiliar host key:

https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/githubs-ssh-key-fingerprints

A successful authentication typically displays a message confirming that GitHub has authenticated the account but does not provide shell access. This is normal for GitHub.

#### 2. Test access to the private repository

Run:

```powershell
$env:GIT_SSH_COMMAND = "ssh -i `"$HOME\.ssh\epims-config-readonly`" -o IdentitiesOnly=yes"

git ls-remote git@github.com:JoshuaPraveen99/epims-config.git

Remove-Item Env:\GIT_SSH_COMMAND
```

The command temporarily tells Git to use the dedicated SSH key.

**Expected result:** Git prints the available repository references and their commit hashes, such as `refs/heads/main`, if the repository has branches.

If the repository has no references, the command may return no output. In that case, successful SSH authentication alone does not prove that Git can retrieve configuration files.

The final command removes the temporary environment variable.

#### 3. Troubleshooting

**Error: `Permission denied (publickey)`**

- Verify that the public key was added to the correct repository.
- Confirm that the private-key file exists.
- Confirm that the SSH command uses the intended private key.

**Error: `Host key verification failed`**

- Verify the GitHub host-key fingerprint against the official documentation.
- Do not disable host-key verification as a workaround.

**Error: `Repository not found`**

- Verify the repository name and SSH URL.
- Confirm that the deploy key is registered on `epims-config`.
- Confirm that the key is associated with the correct repository.

#### 4. Security requirements

- Use the dedicated private key for the test.
- Never display or share the private key.
- Keep write access disabled.
- Remove temporary environment variables after testing.
- Do not commit credentials or private keys to Git.

#### 5. Verification checklist

- [ ] SSH authentication succeeds using the dedicated key.
- [ ] GitHub's host-key fingerprint has been verified.
- [ ] Repository access has been confirmed with `git ls-remote`.
- [ ] The temporary `GIT_SSH_COMMAND` environment variable has been removed.

**Status:** Pending SSH authentication and repository-access verification.

### Result of Step 10.6

Once Step 10.6.3 succeeds, the dedicated SSH key will be registered and verified for read-only access to `epims-config`.

The next step is to configure `epims-config-server` to use the repository's SSH URL and private-key location, and then verify that Spring Cloud Config can retrieve the centralized YAML files.

The private key must remain outside Git and be provisioned securely to the server runtime. The repository-access test does not yet verify end-to-end Spring Cloud Config Server operation.

### Step 10.7 — Configure the Config Server Git Backend

**Objective:** Configure `epims-config-server` to retrieve centralized configuration from the private `epims-config` GitHub repository over SSH.

#### Configuration file

Location: `epims-config-server/src/main/resources/application.yml`

The configuration defines:

- Server port: `8888`
- Application name: `epims-config-server`
- Git repository: `git@github.com:JoshuaPraveen99/epims-config.git`
- Default branch: `main`
- Clone repository at startup: enabled
- SSH private key: supplied through `CONFIG_GIT_PRIVATE_KEY`
- Known hosts: `${user.home}/.ssh/known_hosts`
- Strict SSH host-key checking: enabled
- Actuator health and info endpoints

#### Security requirements

- Do not put the private key directly in the YAML file.
- Do not commit private keys or other credentials.
- Supply `CONFIG_GIT_PRIVATE_KEY` securely at runtime.
- Keep SSH host-key verification enabled.
- Restrict access to the Config Server and its configuration endpoints.

#### Verification checklist

- [ ] Created `src/main/resources/application.yml`.
- [ ] Verified the SSH repository URI and branch.
- [ ] Confirmed that the YAML contains no private-key contents.
- [ ] Confirmed the GitHub host entry exists in `known_hosts`.
- [ ] Configured the private-key environment variable securely.
- [ ] Started the Config Server and tested configuration retrieval.

**Status:** Configuration file creation pending verification. Runtime SSH configuration and end-to-end testing remain outstanding.

#### Step 10.7.2 — Generate a Config Server-Compatible RSA PEM Key

**Objective:** Generate an RSA private key in PEM format for the Spring Cloud Config Server Git backend.

**Reason:** The configured property-based SSH authentication requires a private-key format supported by the Config Server's Git implementation.

**Command:**

```powershell
ssh-keygen -m PEM -t rsa -b 4096 `
  -f "$HOME\.ssh\epims-config-server-rsa" `
  -C "epims-config-server-readonly"
```

For local development, leave the passphrase empty and protect the private key carefully.

**Verification:**

```powershell
Get-ChildItem "$HOME\.ssh\epims-config-server-rsa*"

Get-Content "$HOME\.ssh\epims-config-server-rsa" -TotalCount 1
```

Expected first line: `-----BEGIN RSA PRIVATE KEY-----`

**Security:** Keep the private key outside Git. Only the corresponding public key may be registered with GitHub, with write access disabled.

**Status:** Pending key generation and verification.

#### Additional SSH setup details — host verification, key validation, and common recovery

Use these checks if reproducing the setup on a new Windows machine or if SSH authentication works in one terminal but fails when Spring Cloud Config starts.

##### A. Check OpenSSH availability

Run in PowerShell:

```powershell
ssh -V
ssh-keygen -?
where.exe ssh
where.exe ssh-keygen
```

If commands are not found, install/enable the Windows OpenSSH Client feature, reopen PowerShell, and repeat the checks. IntelliJ's terminal normally uses the same Windows executables when they are on `PATH`.

##### B. Check whether key files already exist before generating new ones

```powershell
Get-ChildItem "$HOME\.ssh" -Force
Test-Path "$HOME\.ssh\epims-config-server-rsa"
Test-Path "$HOME\.ssh\epims-config-server-rsa.pub"
```

Do not overwrite an existing key without confirming where it is used. If a key already exists and is the intended key, validate and reuse it rather than generating another key and leaving GitHub with a mismatched public key.

##### C. Verify the RSA private key without displaying the key contents

```powershell
Get-Content "$HOME\.ssh\epims-config-server-rsa" -TotalCount 1
Get-Content "$HOME\.ssh\epims-config-server-rsa" | Select-Object -Last 1
ssh-keygen -y -f "$HOME\.ssh\epims-config-server-rsa" | Out-Null
$LASTEXITCODE
```

Expected format for this setup:

```text
-----BEGIN RSA PRIVATE KEY-----
...
-----END RSA PRIVATE KEY-----
```

The final command should return exit code `0`. Do not paste the private-key body into a ticket, README, GitHub issue, or chat. If the key is encrypted, `ssh-keygen` may prompt for its passphrase.

##### D. Verify that the public key corresponds to the private key

```powershell
ssh-keygen -y -f "$HOME\.ssh\epims-config-server-rsa"
Get-Content "$HOME\.ssh\epims-config-server-rsa.pub"
```

Compare the generated public-key line with the `.pub` file. They should match. The public key is the one registered with GitHub; never upload the private key.

##### E. Populate `known_hosts` only after verifying GitHub's fingerprint

First, read GitHub's official SSH fingerprint page and compare the published fingerprint with a trusted value:
https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/githubs-ssh-key-fingerprints

Then obtain the host-key records:

```powershell
New-Item -ItemType Directory -Force -Path "$HOME\.ssh" | Out-Null
ssh-keyscan github.com 2>$null | Add-Content "$HOME\.ssh\known_hosts"
ssh-keygen -lf "$HOME\.ssh\known_hosts"
```

**Important:** `ssh-keyscan` collects keys but does not itself authenticate them. Do not trust the output until its fingerprints have been compared with GitHub's official published fingerprints. If there is a mismatch, stop and investigate; do not disable strict host-key checking as a workaround.

##### F. Test the exact RSA key and the private repository

```powershell
ssh -i "$HOME\.ssh\epims-config-server-rsa" -o IdentitiesOnly=yes -T git@github.com
$env:GIT_SSH_COMMAND = "ssh -i `"$HOME\.ssh\epims-config-server-rsa`" -o IdentitiesOnly=yes"
git ls-remote git@github.com:JoshuaPraveen99/epims-config.git
Remove-Item Env:\GIT_SSH_COMMAND
```

GitHub may state that authentication succeeded but shell access is unavailable; that is normal. `git ls-remote` should list refs and commit hashes if the repository has refs. If it fails, check the SSH key registration, repository URL, host-key verification, and network access to port 22.

##### G. Avoid common private-key environment-variable mistakes

The Config Server configuration uses `CONFIG_GIT_PRIVATE_KEY`. For local PowerShell testing, set it only in the terminal that will launch the Config Server:

```powershell
$env:CONFIG_GIT_PRIVATE_KEY = Get-Content "$HOME\.ssh\epims-config-server-rsa" -Raw
```

Do not run `Write-Output $env:CONFIG_GIT_PRIVATE_KEY`, `Get-Content` on the private key for display, or any command that prints the variable. Start the server from that same PowerShell session. When finished, stop the server and remove the variable:

```powershell
Remove-Item Env:\CONFIG_GIT_PRIVATE_KEY -ErrorAction SilentlyContinue
```

A PowerShell process environment variable is not a durable production secret-management method. For containers and production, inject the key through the approved secret store or protected deployment secret mechanism. Do not bake the key into a Docker image or commit it to a repository.

##### H. Error-to-action guide

| Error or symptom | Checks and remedy |
|---|---|
| `Permission denied (publickey)` | Confirm the matching `.pub` key is registered as a deploy key on `epims-config`; test with `-i` and `IdentitiesOnly=yes`; verify the correct private-key path. |
| `Host key verification failed` | Verify the GitHub fingerprint and repair `known_hosts`; do not disable strict host-key checking. |
| `Repository not found` | Check owner/repository spelling and that the deploy key belongs to `epims-config`. |
| `invalid private key` or `privateKey is not a valid private key` | Verify PEM header/footer and key readability; this Config Server setup uses the RSA PEM key because the earlier Ed25519 key was rejected by the configured property-based integration. |
| SSH test succeeds but Config Server fails | Confirm `CONFIG_GIT_PRIVATE_KEY` is set in the same process environment, the application is configured to use that variable, `known_hosts` is at the path the server reads, and the Config Server logs show the underlying Git/SSH error. |
| Config Server returns placeholders literally | Git retrieval may be working, but Config Server does not automatically resolve application secret placeholders. Secret injection or secret-manager integration remains a separate task. |

#### Step 10.7.3 — Register and Test the Config Server RSA Deploy Key

**Objective:** Register the RSA public key for read-only access to the private `epims-config` repository and verify that Git can authenticate with it.

**Copy the public key:**

```powershell
Get-Content "$HOME\.ssh\epims-config-server-rsa.pub" | Set-Clipboard
```

**GitHub registration:**

1. Open `epims-config`.
2. Navigate to **Settings → Deploy keys → Add deploy key**.
3. Set the title to `epims-config-server-rsa-readonly`.
4. Paste the RSA public key.
5. Leave write access disabled.
6. Save the deploy key.

**Test repository access:**

```powershell
$env:GIT_SSH_COMMAND = "ssh -i `"$HOME\.ssh\epims-config-server-rsa`" -o IdentitiesOnly=yes"

git ls-remote git@github.com:JoshuaPraveen99/epims-config.git

Remove-Item Env:\GIT_SSH_COMMAND
```

**Security requirements:**

- Keep the RSA private key outside Git.
- Register only the public key.
- Keep the deploy key read-only.
- Remove the temporary environment variable after testing.

**Status:** Pending GitHub registration and repository-access verification.

#### Step 10.7.4 — Configure and Verify the Config Server

**Objective:** Configure Spring Cloud Config Server to authenticate with the private `epims-config` GitHub repository using a runtime-provided RSA private key.

**Configuration:**
- Config Server port: `8888`
- Git repository: `git@github.com:JoshuaPraveen99/epims-config.git`
- Default branch: `main`
- Private key supplied through `CONFIG_GIT_PRIVATE_KEY`
- GitHub host verification enabled
- Automatic repository clone at startup enabled

**Runtime setup:**

```powershell
$env:CONFIG_GIT_PRIVATE_KEY = Get-Content "$HOME\.ssh\epims-config-server-rsa" -Raw
Set-Location "C:\Users\swoni\Documents\epims\epims-config-server"
.\gradlew.bat bootRun
```

**Verification endpoint:**

```text
http://localhost:8888/epims-eps-core/default
```

The endpoint should return the EPS configuration environment, including its application name, active profile information, and property sources.

**Security requirements:**
- Never commit private keys or actual credentials to Git.
- Do not print or share private-key contents.
- Keep the GitHub deploy key read-only.
- Remove the temporary environment variable after stopping the server.

**Status:** Pending successful Config Server startup and endpoint verification.


**Configuration flow diagram:**

![EPS Config Client and Config Server request flow](docs/diagrams/config-server-ssh-flow.png)
#### Step 10.7.5.1 — Add Spring Cloud Config Client

**Objective:** Add the client library required for EPS Core to retrieve centralized configuration from EPIMS Config Server.

**Repository:** `epims-eps-core`

**File modified:** `build.gradle.kts`

**Dependency added:**

```kotlin
implementation("org.springframework.cloud:spring-cloud-starter-config")
```

**Dependency management:**

- Spring Cloud release train: `2025.1.4`
- Spring Cloud BOM imported through Gradle dependency management.
- Existing Java, Spring Boot, MongoDB, Web, Actuator, and WAR configuration retained.

**Verification:**

Reload Gradle in IntelliJ or execute:

```powershell
.\gradlew.bat dependencies
```

**Status:** Pending Gradle reload and dependency verification.

#### Step 10.7.5.2 — Configure EPS Core to Contact Config Server

**Objective:** Configure EPS Core to retrieve remote configuration from the centralized EPIMS Config Server.

**Repository:** `epims-eps-core`

**File modified:** `src/main/resources/application.yml`

**Configuration added:**

```yaml
spring:
  config:
    import: "configserver:http://localhost:8888"
```

**Existing settings preserved:**
- Application name: `epims-eps-core`
- Application port: `9080`
- MongoDB environment-variable placeholders and local defaults
- Actuator health and information endpoints
- Application metadata

**Startup requirement:** Start the Config Server before EPS Core. The configuration import is mandatory, so EPS will report an error if the remote configuration cannot be loaded.

**Status:** Pending startup and remote-configuration verification.



---

## Reproduction Notes and Final Verification

This README preserves the original detailed step-by-step development log, command examples, explanations, and troubleshooting records. Where the log contains early setup values that differ from the current implementation, use the current decision note in Section 5 and the latest configuration sections as the source of truth.

Before declaring a fresh setup complete, verify these independently:

1. `epims-eps-core` builds using the chosen Java 25-compatible Gradle Wrapper.
2. MongoDB 8 is running with authentication enabled and the persistent `epims-eps-core-data` volume retained.
3. The `epsAppUser` account authenticates against `epims-eps` and has only the intended database permissions.
4. MongoDB Compass connects using the application account.
5. `epims-config` contains only placeholders and non-sensitive configuration.
6. The RSA PEM private key is outside Git and is injected only into the Config Server runtime.
7. GitHub host-key verification is enabled and the fingerprint has been verified.
8. `epims-config-server` retrieves the private repository's `main` branch and serves `http://localhost:8888/epims-eps-core/default`.
9. EPS includes the Spring Cloud Config Client and starts with the intended Config Server import.
10. The current end-to-end EPS startup is tested only after configuration, MongoDB authentication, and runtime secret resolution are all compatible.

A successful Config Server response that contains literal values such as `${MONGODB_PASSWORD}` confirms configuration retrieval, **not** secret resolution or successful database connectivity.

# EPIMS Infrastructure

## Step 1 — Initialize the Infrastructure Repository

### 1.1 Objective

Prepare the dedicated `epims-infrastructure` Git repository for managing the containerized deployment and operational infrastructure of the Enterprise Pharmacy Information Management System (EPIMS).

This repository will hold infrastructure definitions and deployment-related resources. Application source code will remain in its respective repositories.

### 1.2 Repository Details

| Property | Value |
|---|---|
| Repository name | `epims-infrastructure` |
| GitHub owner | `JoshuaPraveen99` |
| Visibility | Private |
| Local workspace | `C:\Users\swoni\Documents\epims\epims-infrastructure` |
| Primary purpose | Container infrastructure and deployment orchestration |
| Source control | Git and GitHub |

### 1.3 Repository Responsibilities

The infrastructure repository will progressively manage:

- Docker Compose service definitions.
- Container networks and service dependencies.
- Persistent storage definitions for stateful services.
- Container health checks and startup dependencies.
- Non-sensitive infrastructure configuration.
- OpenBao deployment configuration and policies, when implemented.
- Setup, health-check, and backup scripts.
- Deployment and troubleshooting documentation.

The repository will not replace the application repositories or the centralized application configuration repository.

### 1.4 Separation of Responsibilities

| Repository or component | Responsibility |
|---|---|
| `epims-eps-core` | EPS pharmacy backend application |
| `epims-config-server` | Spring Cloud Config Server application |
| `epims-config` | Version-controlled, non-sensitive application configuration and placeholders |
| `epims-infrastructure` | Container definitions, networks, volumes, and deployment orchestration |
| OpenBao | Planned secret storage and access policies |
| Secret Management API | Planned authenticated API for authorized secret updates |

### 1.5 Verify the Local Workspace

Open PowerShell and run:

```powershell
Set-Location "C:\Users\swoni\Documents\epims"

Get-Location
Get-ChildItem -Directory
Test-Path ".\epims-infrastructure"
```

If the local directory already exists, inspect it before proceeding. Do not overwrite existing files or initialize a nested Git repository.

### 1.6 Verify the GitHub Repository

Open:

https://github.com/JoshuaPraveen99/epims-infrastructure

Verify that the repository exists and that the signed-in account has access. A `404` response alone does not prove that a private repository is absent.

If the repository does not exist, create a private GitHub repository named `epims-infrastructure`.

If the repository already exists, retain its existing branches and commit history.

### 1.7 Clone the Repository

If the GitHub repository exists and the local directory does not, run:

```powershell
Set-Location "C:\Users\swoni\Documents\epims"

git clone https://github.com/JoshuaPraveen99/epims-infrastructure.git

Set-Location ".\epims-infrastructure"
```

Verify the clone:

```powershell
Get-Location
git remote -v
git branch --show-current
git status
git rev-parse --show-toplevel
```

The repository root should be `epims-infrastructure`, and the `origin` remote should point to the intended GitHub repository.

### 1.8 Security Requirements

- Never commit real passwords, API tokens, private keys, or other credentials.
- Keep `.env` files containing actual secrets out of Git.
- Use `.env.example` for variable names and safe placeholders only.
- Do not put the Config Server's GitHub private key in the infrastructure repository.
- Introduce secrets through an explicitly configured secure provisioning mechanism.
- Do not expose database or secret-management ports to the public internet by default.

### 1.9 Completion Checklist

- [ ] Checked whether `epims-infrastructure` exists on GitHub.
- [ ] Checked whether the local directory already exists.
- [ ] Created the private repository if it was absent.
- [ ] Cloned the repository if it was not already local.
- [ ] Verified the repository root.
- [ ] Verified the `origin` remote.
- [ ] Verified the current branch and working-tree status.
- [ ] Preserved any existing files and Git history.
- [ ] Added this documentation to `README.md`.

**Status:** Pending verification.

### 1.10 Next Step

**Step 2 — Define the infrastructure directory structure and security baseline.**

The next step will establish the initial `.gitignore`, safe `.env.example`, directory layout, and infrastructure conventions before adding Docker Compose services.

## Step 2 — Create the Infrastructure Directory Structure

### Objective

Establish a consistent directory structure for EPIMS infrastructure configuration, container orchestration, secret management, operational scripts, and technical documentation.

The structure separates deployment configuration from application source code and prepares the repository for a containerized deployment model.

### 2.1 Repository Location

Local repository path:

`C:\Users\swoni\Documents\epims\epims-infrastructure`

GitHub repository:

`https://github.com/JoshuaPraveen99/epims-infrastructure`

### 2.2 Directory Structure

The following directories are created during this step:

```text
epims-infrastructure/
├── README.md
├── compose/
│   └── services/
├── config/
│   └── mongodb/
├── openbao/
│   ├── config/
│   └── policies/
├── scripts/
│   ├── setup/
│   ├── health/
│   └── backup/
└── docs/
    ├── architecture/
    ├── operations/
    └── troubleshooting/
```

### 2.3 Directory Responsibilities

| Directory | Purpose |
|---|---|
| `compose/` | Container orchestration configuration |
| `compose/services/` | Optional service-specific Compose definitions |
| `config/` | Non-sensitive infrastructure configuration |
| `config/mongodb/` | MongoDB configuration and initialization resources |
| `openbao/config/` | OpenBao server configuration |
| `openbao/policies/` | OpenBao authorization policies |
| `scripts/setup/` | Infrastructure setup and initialization scripts |
| `scripts/health/` | Service health checks and diagnostics |
| `scripts/backup/` | Backup and recovery automation |
| `docs/architecture/` | Infrastructure architecture documentation |
| `docs/operations/` | Deployment and operational procedures |
| `docs/troubleshooting/` | Troubleshooting guides and known issues |

### 2.4 Security Baseline

The following rules apply to all infrastructure files:

1. Actual credentials, access tokens, private keys, and secret values must never be committed to Git.
2. Real environment files must be excluded from version control.
3. Example environment files may contain variable names and safe placeholders only.
4. OpenBao policies must define permissions without embedding secret values.
5. Database and secret-management services must not be publicly exposed by default.
6. TLS certificate verification must remain enabled in secure deployments.
7. Logs and diagnostic output must not disclose secret values.
8. Production deployment settings must be reviewed separately from local development settings.

These are repository security requirements. Enforcement will be added and verified as the relevant configuration and deployment files are implemented.

### 2.5 Verification

Run the following commands from PowerShell:

```powershell
Set-Location "C:\Users\swoni\Documents\epims\epims-infrastructure"

Get-ChildItem -Directory -Recurse |
    Select-Object FullName

git status
```

Verify that all expected directories exist and that Git reports the newly created directories through their contents when files are added. Git does not track empty directories.

### 2.6 Completion Checklist

- [ ] Confirmed the repository location.
- [ ] Created all required infrastructure directories.
- [ ] Verified the directory structure.
- [ ] Reviewed the security baseline.
- [ ] Confirmed that no actual secrets have been added to the repository.

**Step 2 status:** Pending verification.

**Next step:** Step 3 — Create the Git ignore rules and safe environment-variable template before adding infrastructure configuration.

## Step 3 — Configure Git Ignore Rules and Environment Templates

### Objective

Protect sensitive environment files and establish a documented template for infrastructure configuration.

### 3.1 Create `.gitignore`

The `.gitignore` file excludes local environment files, private keys, runtime data, database backups, logs, temporary files, IDE settings, and build outputs.

The `.env.example` template is explicitly allowed so that variable names can be documented without publishing actual credentials.

### 3.2 Create `.env.example`

The environment template defines initial configuration variables for:

- EPIMS environment and timezone.
- MongoDB connection details.
- MongoDB administrator credentials.
- MongoDB application credentials.
- Config Server hostname and port.
- OpenBao hostname and port.
- Secret Management API hostname and port.

All credential values in `.env.example` must be placeholders. Replace them with actual values only in a local `.env` file or an approved secret-management mechanism.

### 3.3 Security Requirements

1. Never commit actual passwords, private keys, tokens, or secret values.
2. Never use `.env.example` as a production secrets store.
3. Do not assume `.gitignore` protects files that are already tracked.
4. Do not print or share actual `.env` contents during troubleshooting.
5. Do not commit OpenBao root tokens, unseal keys, runtime state, or database backups.
6. Review changes to `.gitignore` before committing to ensure sensitive files remain excluded.

### 3.4 Verification Commands

Run from the repository root:

```powershell
Test-Path ".gitignore"
Test-Path ".env.example"
git check-ignore .env
git status --short
```

Confirm that both files exist and `.env` is ignored by Git.

### 3.5 Completion Checklist

- [ ] Created or reviewed `.gitignore`.
- [ ] Created or reviewed `.env.example`.
- [ ] Confirmed `.env` is ignored.
- [ ] Confirmed `.env.example` is not excluded.
- [ ] Verified that no real credentials are present in tracked files.

**Step 3 status:** Pending verification.

**Next step:** Step 4 — Design the Docker Compose service layout, network boundaries, persistent volumes, and health checks before implementing the container configuration.

## Step 4 — Design the Docker Compose Architecture

### Objective

Define the infrastructure service responsibilities, networking, persistence, and startup-readiness requirements before implementing Docker Compose configuration.

### 4.1 Planned Services

| Service | Responsibility |
|---|---|
| MongoDB | Persistent database for EPS application data |
| Config Server | Supplies application configuration from the `epims-config` repository |
| OpenBao | Centralized secret storage and access control |
| Secret Management API | Authorized REST interface for managing secrets in OpenBao |
| EPS Core | Pharmacy backend that consumes application configuration and connects to its required services |

The service definitions will be implemented incrementally. The planned architecture does not mean every service is already deployed.

### 4.2 Networking

- Application containers should use Compose service names for internal communication.
- MongoDB and OpenBao should remain on private container networks unless a documented operational requirement justifies additional access.
- Host port bindings must be explicit and restricted where host access is required.
- Production access must be reviewed separately from local development access.
- Secrets must not be passed through URLs or written to logs.

### 4.3 Persistence

- MongoDB must use a named volume for database persistence.
- OpenBao storage must be persistent and separate from MongoDB storage.
- Database backups and OpenBao recovery material must be protected and excluded from Git.
- Container recreation must not be treated as a backup or recovery strategy.

### 4.4 Startup and Health Checks

Docker Compose startup dependencies must be configured according to actual service requirements.

Where a service provides a reliable health check, dependent services should use the `service_healthy` condition when appropriate. Merely starting a container does not guarantee that its application is ready to accept connections.

Application-level retry and reconnection behavior must also be considered.

### 4.5 Security Requirements

1. Do not expose database or secret-management services publicly by default.
2. Do not store actual credentials in Compose files.
3. Do not disable TLS verification to work around connectivity errors.
4. Use separate credentials and access policies for application and administrator access.
5. Plan OpenBao initialization, storage, recovery, and access control before using it for application secrets.

### 4.6 Environment Verification

Run:

```powershell
docker --version
docker compose version
docker info
```

Confirm that Docker Engine is available and the Compose command works.

### 4.7 Completion Checklist

- [ ] Defined the responsibilities of the planned services.
- [ ] Defined the initial private-networking approach.
- [ ] Defined data-persistence requirements.
- [ ] Documented startup-readiness and health-check requirements.
- [ ] Verified Docker and Docker Compose availability.

**Step 4 status:** Pending verification.

**Next step:** Step 5 — Design the initial `compose.yaml`, beginning with MongoDB and its persistent volume, health check, and restricted host binding. Additional services will be added only after their configuration and security requirements are defined.

# Step 5 — Configure MongoDB in Docker Compose

## Objective

Integrate the existing EPIMS MongoDB 8 container into the `epims-infrastructure` repository using Docker Compose while preserving existing database files, authentication, and host-port restrictions.

This step first inspects the existing MongoDB deployment, verifies application-user authentication, identifies persistent storage, and prepares the Compose configuration. The container migration must not be considered complete until the existing database and authentication have been verified after the transition.

## 5.1 Existing MongoDB Configuration

The existing MongoDB deployment was identified with the following configuration:

| Property | Value |
|---|---|
| Container name | `epims-mongodb` |
| Docker image | `mongo:8` |
| Database | `epims-eps` |
| Application username | `epsAppUser` |
| Database authentication | Enabled using `mongod --auth` |
| Database storage volume | `epims-eps-core-data` |
| Database storage mount | `/data/db` |
| Auxiliary storage mount | `/data/configdb` |
| Host binding | `127.0.0.1:27017` |
| Restart policy | `unless-stopped` |
| Docker network | `bridge` |

The application-user authentication test returned `{ ok: 1 }`.

The existing database volume must be preserved during the transition to Compose.

## 5.2 Inspect Existing Containers and Volumes

Open PowerShell and navigate to the infrastructure repository:

```powershell
Set-Location "C:\Users\swoni\Documents\epims\epims-infrastructure"
```

List the MongoDB container:

```powershell
docker ps -a --filter "name=epims-mongodb"
```

List available Docker volumes:

```powershell
docker volume ls
```

Inspect the image:

```powershell
docker inspect epims-mongodb --format '{{.Config.Image}}'
```

Inspect the published host ports:

```powershell
docker inspect epims-mongodb --format '{{json .HostConfig.PortBindings}}'
```

Inspect the current network:

```powershell
docker inspect epims-mongodb --format '{{json .NetworkSettings.Networks}}'
```

Inspect the startup command:

```powershell
docker inspect epims-mongodb --format '{{json .Config.Entrypoint}}'
docker inspect epims-mongodb --format '{{json .Config.Cmd}}'
```

Inspect the restart policy:

```powershell
docker inspect epims-mongodb --format '{{json .HostConfig.RestartPolicy}}'
```

Inspect the mounted volumes:

```powershell
docker inspect epims-mongodb --format '{{json .Mounts}}'
```

Inspect the database volume:

```powershell
docker volume inspect epims-eps-core-data
```

These commands establish the existing configuration before any container replacement is attempted.

## 5.3 Verify MongoDB Authentication

MongoDB authentication must be verified independently of the container's running status.

Execute:

```powershell
docker exec -it epims-mongodb mongosh `
  --username epsAppUser `
  --authenticationDatabase epims-eps `
  --password `
  --eval "db.runCommand({ ping: 1 })"
```

Enter the application user's password when prompted.

Expected result:

```javascript
{ ok: 1 }
```

A successful result confirms that the supplied account authenticated and the MongoDB ping command succeeded.

Do not place actual passwords in the README, Compose files, source code, or shared terminal output.

## 5.4 Inspect Environment-Variable Names

Inspect the container's environment-variable names without displaying their values:

```powershell
docker inspect epims-mongodb --format '{{range .Config.Env}}{{println .}}{{end}}' |
    ForEach-Object {
        ($_ -split '=', 2)[0]
    }
```

The inspected environment contained MongoDB image/runtime variables, including `MONGO_VERSION`, `MONGO_PACKAGE`, and `MONGO_MAJOR`.

No MongoDB initialization credential variables were present in the inspected container environment.

This does not mean authentication is disabled. The authentication test in Section 5.3 succeeded.

Existing MongoDB users are stored in the database. Do not assume that adding initialization environment variables to a replacement container will recreate or modify existing users.

## 5.5 Verify Persistent Storage

Inspect the existing database volume:

```powershell
docker volume inspect epims-eps-core-data
```

Inspect all container mounts:

```powershell
docker inspect epims-mongodb --format '{{json .Mounts}}'
```

The inspection identified these mounts:

| Docker volume | Container destination | Purpose |
|---|---|---|
| `epims-eps-core-data` | `/data/db` | MongoDB database files |
| `9962fbcb1944f0d775b92467e4f86edc3b83128f56a770da10f0211c4d78fe7f` | `/data/configdb` | Auxiliary storage mounted by the existing container |

The database volume is a named Docker volume and must remain available during the Compose transition.

The auxiliary volume has a Docker-generated name. The initial Compose configuration references that existing volume explicitly so that the mount can be preserved.

Do not delete either volume during this step.

## 5.6 Create the Compose Configuration

Create a file named `compose.yaml` in the root of `epims-infrastructure`, alongside `README.md` and `.gitignore`.

Use the following configuration:

```yaml
services:
  mongodb:
    image: mongo:8
    container_name: epims-mongodb
    command: ["mongod", "--auth"]
    restart: unless-stopped

    ports:
      - "127.0.0.1:27017:27017"

    volumes:
      - epims-eps-core-data:/data/db
      - mongo-configdb:/data/configdb

    healthcheck:
      test:
        - CMD
        - mongosh
        - --quiet
        - --eval
        - "if (db.adminCommand({ ping: 1 }).ok !== 1) quit(1)"
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 30s

volumes:
  epims-eps-core-data:
    external: true
    name: epims-eps-core-data

  mongo-configdb:
    external: true
    name: 9962fbcb1944f0d775b92467e4f86edc3b83128f56a770da10f0211c4d78fe7f
```

### Configuration explanation

**Image**

```yaml
image: mongo:8
```

Uses the MongoDB 8 image family that is already running in the existing deployment.

**Startup command**

```yaml
command: ["mongod", "--auth"]
```

Preserves the existing MongoDB startup behavior with authentication enabled.

**Restart policy**

```yaml
restart: unless-stopped
```

Allows Docker to restart the container after an unexpected exit, unless it has been explicitly stopped.

**Host-port binding**

```yaml
ports:
  - "127.0.0.1:27017:27017"
```

Restricts published MongoDB access to the host machine's loopback interface. It does not expose the published port on every host interface.

**Database volume**

```yaml
- epims-eps-core-data:/data/db
```

Reuses the existing named database volume instead of creating a new database volume.

**Auxiliary volume**

```yaml
- mongo-configdb:/data/configdb
```

Preserves the additional mount identified during container inspection.

**External volumes**

```yaml
volumes:
  epims-eps-core-data:
    external: true
    name: epims-eps-core-data
```

The `external: true` declaration tells Docker Compose to use an existing Docker volume. Compose will not create a replacement volume for this declaration and will report an error if the volume is missing.

The auxiliary volume is also declared external and references its existing Docker-generated name.

## 5.7 Understand the Health Check

The Compose service uses this health-check configuration:

```yaml
healthcheck:
  test:
    - CMD
    - mongosh
    - --quiet
    - --eval
    - "if (db.adminCommand({ ping: 1 }).ok !== 1) quit(1)"
  interval: 10s
  timeout: 5s
  retries: 5
  start_period: 30s
```

### How it works

- `CMD` executes the command directly without invoking a shell.
- `mongosh` runs the MongoDB shell.
- `--quiet` suppresses unnecessary shell output.
- The JavaScript expression checks the result of the MongoDB ping command and exits with a nonzero status if the result is not successful.
- `interval: 10s` runs the check every 10 seconds.
- `timeout: 5s` limits how long an individual check may run.
- `retries: 5` marks the container unhealthy after five consecutive unsuccessful checks.
- `start_period: 30s` provides an initialization period for the service.

This check is intended to verify MongoDB server responsiveness. It is not a replacement for the separate application-user authentication test. If the health check cannot execute the ping successfully under the server's authorization configuration, it must be adjusted and tested before being relied on for orchestration.

## 5.8 Validate the Compose Configuration

From the repository root, run:

```powershell
docker compose -f compose.yaml config --quiet
```

If no validation error is reported, Compose accepts the configuration syntax and structure.

Inspect the resolved configuration:

```powershell
docker compose -f compose.yaml config
```

Verify that:

- The service is named `mongodb`.
- The image is `mongo:8`.
- The command is `mongod --auth`.
- The host-port binding is `127.0.0.1:27017:27017`.
- The health check matches Section 5.7.
- Both volume declarations use `external: true`.
- The external volume names match the existing Docker volumes.

Configuration validation does not start the service and does not prove that MongoDB authentication or the health check works.

**Do not run `docker compose up` yet.** The existing container already uses the name `epims-mongodb` and host port `27017`. The replacement procedure must account for these conflicts.

## 5.9 Migration Safety Requirements

Before transitioning the existing container to Compose:

1. Confirm that the database volume exists.
2. Ensure that the existing container's database volume is preserved.
3. Confirm that the auxiliary mount is accounted for.
4. Prepare a backup and recovery plan appropriate for the database.
5. Plan for the existing container name and host-port conflicts.
6. Stop the existing container only when the replacement procedure is ready.
7. Do not remove the existing database volume.
8. After the transition, verify the container status, health check, application-user authentication, and existing database contents.

The transition must be treated as a controlled migration, not as an ordinary container restart.

## 5.10 Security Requirements

- Never commit actual passwords, access tokens, private keys, or secret values.
- Do not add MongoDB passwords directly to `compose.yaml`.
- Keep real environment files excluded from Git.
- Do not publish MongoDB on all host interfaces by default.
- Do not delete persistent volumes as a troubleshooting shortcut.
- Do not assume container health proves that application authentication succeeds.
- Do not use a container recreation as a substitute for backup and recovery.
- Verify the configuration after every significant migration change.

## 5.11 Verification Checklist

- [x] Identified the existing MongoDB container.
- [x] Confirmed the MongoDB 8 image.
- [x] Confirmed that authentication is enabled.
- [x] Verified application-user authentication and MongoDB ping.
- [x] Identified the existing database volume.
- [x] Identified the auxiliary `/data/configdb` volume.
- [x] Confirmed the host-port binding.
- [x] Identified the existing startup command and restart policy.
- [ ] Created `compose.yaml`.
- [ ] Validated the Compose configuration.
- [ ] Tested the health check.
- [ ] Completed the controlled transition to Compose.
- [ ] Verified authentication and existing database contents after the transition.

**Step 5 status:** Existing deployment inspected and authentication verified. Compose configuration and migration remain pending until their corresponding checks succeed.

**Next step:** Validate `compose.yaml`, then execute the controlled migration from the standalone MongoDB container to the Compose-managed service.

### 5.15 Final Migration Results

The existing MongoDB deployment was transitioned from a standalone Docker container to a Docker Compose-managed service.

| Verification | Result |
|---|---|
| MongoDB image | `mongo:8` |
| Compose service | `mongodb` |
| Container name | `epims-mongodb` |
| Container status | Running and healthy |
| Host port | `127.0.0.1:27017` |
| Database volume | `epims-eps-core-data` |
| Auxiliary volume | Existing `/data/configdb` volume |
| Application authentication | Successful |
| Application database | `epims-eps` |
| Accessible collection | `orders` |

### 5.16 Application Role Correction

The initial `epsAppUser` role was assigned to `epims_eps`, while the application database is named `epims-eps`.

The correct role was granted on the intended database:

```javascript
{ role: "readWrite", db: "epims-eps" }
```

After the correction, the application account successfully listed the `orders` collection.

The original, incorrect role on `epims_eps` may remain assigned. Remove it only after confirming that no application or workflow depends on that database.

### 5.17 Final Completion Checklist

- [x] Inspected the existing MongoDB container.
- [x] Identified and preserved the existing database volume.
- [x] Identified the auxiliary storage volume.
- [x] Confirmed MongoDB authentication.
- [x] Created and validated `compose.yaml`.
- [x] Started MongoDB using Docker Compose.
- [x] Confirmed that the container is healthy.
- [x] Confirmed application-user access to `epims-eps`.
- [x] Confirmed access to the existing `orders` collection.
- [ ] Remove the stopped standalone container after the transition is accepted and rollback is no longer needed.

**Step 5 status:** Complete — MongoDB is running under Docker Compose and application-level collection access is verified.

**Next step:** Step 6 — Integrate the EPIMS Config Server into the infrastructure deployment, including its container build, configuration repository access, environment variables, and health checks.

## Step 6 — Integrate the EPIMS Config Server

### Objective

Prepare the existing `epims-config-server` Spring Boot application for containerized deployment through the `epims-infrastructure` repository.

The Config Server retrieves non-secret configuration from the private `epims-config` Git repository. It must not embed private SSH keys, Git credentials, or application passwords in its source code, Docker image, or committed configuration.

### 6.1 Inspect the Existing Config Server

Local project path:

`C:\Users\swoni\Documents\epims\epims-config-server`

GitHub repository:

`https://github.com/JoshuaPraveen99/epims-config-server`

The existing project uses Java 25, Spring Boot 4.0.9, and Spring Cloud Config Server. These versions must be preserved unless a compatibility issue is identified and explicitly addressed.

### 6.2 Verify the Project Files

Run:

```powershell
Set-Location "C:\Users\swoni\Documents\epims\epims-config-server"

Get-ChildItem -Force

Test-Path ".\Dockerfile"
Test-Path ".\build.gradle.kts"
Test-Path ".\src\main\resources\application.yml"
```

### 6.3 Review the Build Configuration

Run:

```powershell
Get-Content ".\build.gradle.kts"
```

Confirm that the existing Java toolchain, Spring Boot plugin, Spring Cloud dependencies, and test configuration are preserved.

### 6.4 Review the Application Configuration

Run:

```powershell
Get-Content ".\src\main\resources\application.yml"
```

Confirm that the configuration references the private Git repository and uses an environment-provided private key rather than a committed credential.

Never include the contents of private keys or actual secret values in this documentation.

### 6.5 Verify Repository State

Run:

```powershell
git status
git remote -v
```

Confirm that the working directory belongs to the expected Config Server repository.

### 6.6 Security Requirements

1. Do not commit private SSH keys or Git credentials.
2. Do not copy private keys into the Docker image.
3. Keep private configuration-repository access separate from application runtime secrets.
4. Preserve the existing Git host-key verification configuration.
5. Do not expose the Config Server publicly by default.
6. Use Docker service DNS for communication between containers.
7. Validate the container build before integrating the service into the main Compose deployment.

### 6.7 Completion Checklist

- [ ] Confirmed the Config Server repository location.
- [ ] Inspected the existing Dockerfile status.
- [ ] Reviewed `build.gradle.kts`.
- [ ] Reviewed `application.yml`.
- [ ] Verified Git repository state.
- [ ] Confirmed that no private key or credential will be committed.

**Step 6 status:** Pending inspection.

**Next step:** Create or refine the Config Server Dockerfile and design secure runtime delivery of its private Git SSH key.


### 6.2 Make Config Server Configuration Container-Independent

The Config Server must not depend on a developer's home directory or workstation-specific SSH configuration.

The previous configuration referenced:

```yaml
known-hosts-file: ${user.home}/.ssh/known_hosts
```

This path depends on the runtime user's home directory and may not exist inside the container.

The container configuration now uses a fixed path:

```yaml
known-hosts-file: /run/secrets/known_hosts
```

The private key will be supplied through a runtime-mounted secret file. Spring Boot configuration-tree import is enabled using:

```yaml
spring:
  config:
    import: "optional:configtree:/run/secrets/"
```

The private-key secret file must be mounted using this filename:

`/run/secrets/spring.cloud.config.server.git.private-key`

Spring Boot maps the filename to the property:

`spring.cloud.config.server.git.private-key`

The Git backend continues to use the private repository:

`git@github.com:JoshuaPraveen99/epims-config.git`

The following security settings remain enabled:

```yaml
ignore-local-ssh-settings: true
strict-host-key-checking: true
```

### 6.3 Secret Delivery Requirements

1. Do not copy the private SSH key into the Docker image.
2. Do not commit private keys or actual credentials to Git.
3. Mount the private key only into the Config Server container.
4. Mount a verified GitHub host-key file at `/run/secrets/known_hosts`.
5. Keep strict SSH host-key verification enabled.
6. Use environment-specific secret delivery without changing the application configuration.
7. Use the planned secret-management integration for production deployments rather than relying on a developer's profile directory.

### 6.4 Completion Checklist

- [x] Identified the workstation-dependent `user.home` reference.
- [x] Selected a fixed container path for SSH host-key verification.
- [x] Selected Spring Boot configuration-tree import for private-key delivery.
- [ ] Created the Config Server Dockerfile.
- [ ] Added the Config Server service and secret mounts to Compose.
- [ ] Verified private repository access from the container.
- [ ] Verified the Config Server health endpoint.

**Step 6 status:** Container-independent configuration designed; container build and runtime verification remain pending.

**Next step:** Create the Config Server Dockerfile and add the service to `compose.yaml`.