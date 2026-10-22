docker ps -a --filter "name=epims-mongodb"# EPIMS Pharmacy System — EPS Core Backend

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

### 1.3 Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Application programming language |
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
      uri: ${MONGODB_URI:mongodb://localhost:27017/epims_eps}

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
docker volume create epims-mongodb-data
```

The named volume preserves MongoDB data when the container is stopped or recreated, provided the volume itself is retained.

### 6.5 Start MongoDB

For the initial unauthenticated setup, the container was created with:

```powershell
docker run -d `
  --name epims-mongodb `
  -p 127.0.0.1:27017:27017 `
  -v epims-mongodb-data:/data/db `
  mongo:8
```

**Security note:** This command starts MongoDB without authentication. It is only an initial local-development configuration. Authentication is addressed in Step 6.4.

The port binding restricts host access to the local computer rather than all network interfaces.

### 6.6 Verify the container

```powershell
docker ps -a --filter "name=epims-mongodb"
docker volume ls --filter "name=epims-mongodb-data"
docker inspect epims-mongodb --format '{{json .Mounts}}'
```

Confirm that:

- The container is named `epims-mongodb`.
- The image is `mongo:8`.
- Port `127.0.0.1:27017` is mapped to container port `27017`.
- `epims-mongodb-data` is mounted at `/data/db`.

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

**Important:** Do not use `docker rm -v` or delete `epims-mongodb-data` when reconfiguring MongoDB.

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
3. Enter database name `epims_eps`.
4. Enter collection name `orders`.
5. Click **Create database**.

The initial database structure will be:

```text
epims_eps
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
use epims_eps
show collections
exit
```

Expected: `epims_eps` appears after it contains data, and `orders` appears if the collection has been created.

---

## Step 6.2 — Inspect Existing MongoDB Users and Storage

Before enabling authentication, inspect the existing container and volume to avoid accidental data loss.

```powershell
docker ps -a --filter "name=epims-mongodb"
docker volume ls --filter "name=epims-mongodb-data"
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

**Status: Pending**

The administrator account exists, but authentication has not yet been enabled.

The next task is to reconfigure the MongoDB container with authentication enabled, preserve `epims-mongodb-data`, and verify that unauthenticated connections are rejected and authenticated connections succeed.

Do not remove the existing data volume or recreate the database from scratch.

After authentication is enabled, create a dedicated application user with permissions restricted to the `epims_eps` database.

The EPS application's MongoDB URI must then be configured through the `MONGODB_URI` environment variable or another secure secret-management mechanism. Do not commit credentials to the repository.

---

## Step 6.4 — Enable MongoDB Authentication

### Objective

Enable MongoDB access control while retaining the existing EPIMS database and administrator account.

### 6.4.1 Stop and remove the old container

```powershell
docker stop epims-mongodb
docker rm epims-mongodb
```

The container is removed, but the persistent `epims-mongodb-data` volume is retained.

**Never use `docker rm -v` or delete the data volume during this procedure.**

### 6.4.2 Recreate MongoDB with authentication enabled

```powershell
docker run -d `
  --name epims-mongodb `
  --restart unless-stopped `
  -p 127.0.0.1:27017:27017 `
  -v epims-mongodb-data:/data/db `
  mongo:8 mongod --auth
```

**Configuration explanation:**

- `--name epims-mongodb`: Sets the container name.
- `--restart unless-stopped`: Restarts the container automatically unless it has been explicitly stopped.
- `127.0.0.1:27017:27017`: Restricts the published port to the local computer.
- `epims-mongodb-data:/data/db`: Reuses the existing database files.
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
| Database | `epims_eps` |
| Authentication database | `epims_eps` |
| Role | `readWrite` |
| Permission scope | `epims_eps` database only |

### 6.5.3 Connect as the MongoDB Administrator

Open PowerShell and run:

```powershell
docker exec -it epims-mongodb mongosh --username epimsAdmin --authenticationDatabase admin --password
```

Enter the administrator password when prompted.

Select the EPS database:

```javascript
use epims_eps
```

Expected output:

```text
switched to db epims_eps
```

### 6.5.4 Create the EPS Application User

Execute:

```javascript
db.createUser({
  user: "epsAppUser",
  pwd: passwordPrompt(),
  roles: [
    { role: "readWrite", db: "epims_eps" }
  ]
})
```

Enter a strong, unique password when prompted. Store it securely in a password manager.

Expected result:

```javascript
{ ok: 1 }
```

### 6.5.5 Verify the Application User

While still in the `epims_eps` database, run:

```javascript
db.getUsers()
```

Verify that `epsAppUser` exists with the `readWrite` role on `epims_eps`.

Exit the administrator session:

```javascript
exit
```

### 6.5.6 Test Application User Authentication

Connect using the application account:

```powershell
docker exec -it epims-mongodb mongosh --username epsAppUser --authenticationDatabase epims_eps --password
```

Enter the application user's password when prompted.

Select the EPS database explicitly:

```javascript
use epims_eps
```

Expected output:

```text
switched to db epims_eps
```

### 6.5.7 Verify the Authenticated Identity

Execute:

```javascript
db.runCommand({ connectionStatus: 1 })
```

Verify that the output contains:

```javascript
authenticatedUsers: [
  { user: "epsAppUser", db: "epims_eps" }
]
```

The authenticated role should include `readWrite` on `epims_eps`.

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
- The `readWrite` role is limited to the `epims_eps` database and does not grant MongoDB server-administration privileges.

**Completion criteria:** The application user authenticates successfully, has the expected `readWrite` role, and can insert, query, and delete documents in `epims_eps`.

---

## Step 6.6 — Connect MongoDB Compass with Authentication

**Status: Pending**

Configure MongoDB Compass to connect to the authenticated MongoDB container using `epsAppUser` and `epims_eps` as the authentication database. Then verify that the database and collections are accessible.

## Step 6.6 — Connect MongoDB Compass with Authentication

### 6.6.1 Objective

Connect MongoDB Compass to the authenticated MongoDB Docker container using the restricted EPS application account.

### 6.6.2 Open MongoDB Compass

Install and open MongoDB Compass:

https://www.mongodb.com/products/tools/compass

### 6.6.3 Configure the connection

Create a new connection and use:

```text
mongodb://epsAppUser@127.0.0.1:27017/epims_eps?authSource=epims_eps
```

Enter the `epsAppUser` password when prompted.

**Connection parameters:**

| Parameter | Value |
|---|---|
| Username | `epsAppUser` |
| Host | `127.0.0.1` |
| Port | `27017` |
| Default database | `epims_eps` |
| Authentication database | `epims_eps` |
| Authentication mechanism | Negotiated by MongoDB driver |

### 6.6.4 Verify the connection

1. Click **Connect**.
2. Confirm that the connection succeeds.
3. Locate the `epims_eps` database.
4. Expand the database and verify its collections.
5. If the `orders` collection was previously created, verify that it is listed.

### 6.6.5 Security considerations

- Use `epsAppUser` for normal database access.
- Do not use the `epimsAdmin` account for routine application access.
- Do not commit passwords or credential-bearing connection strings to Git.
- The connection uses local MongoDB access; production deployments require appropriate network restrictions, TLS, and secure credential management.

**Completion criteria:** Compass connects successfully using `epsAppUser` and can access the `epims_eps` database.

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

# Step 8 — Initialize the Centralized Configuration Repository

**Status: Pending**

## 8.1 Objective

Create a separate private Git repository named `epims-config` to manage shared and service-specific, non-sensitive configuration for EPIMS microservices.

The configuration repository will eventually contain files such as:

```text
epims-config/
├── README.md
├── application.yml
├── epims-eps-core.yml
├── epims-epr-core.yml
├── epims-sbmo-core.yml
└── epims-notification-service.yml
```

These files will follow Spring Cloud Config naming conventions.

## 8.2 Planned Configuration Architecture

- **`epims-config`:** Shared and service-specific non-sensitive application properties.
- **Spring Cloud Config Server:** Retrieves configuration from the Git repository and serves it to Spring Boot services.
- **Secret management:** Stores MongoDB passwords, API tokens, signing keys, and other sensitive credentials outside Git.
- **Credential management API:** Allows authorized administrators to update secrets through a secured API backed by the secret-management system.
- **`epims-infrastructure`:** Holds infrastructure and deployment configuration.

## 8.3 Security Requirements

- Never store database passwords or other secrets in Git.
- Keep the configuration repository private and restrict access.
- Use a dedicated secret-management solution for sensitive values.
- Secure administrative configuration and credential update operations with authentication, authorization, auditing, and TLS.

## 8.4 Planned Implementation

1. Create the private `epims-config` repository on GitHub.
2. Initialize it with a `README.md`.
3. Clone it locally.
4. Verify the Git remote and working-tree status.
5. Create shared and service-specific configuration files.
6. Commit and push the initial configuration structure.

**Completion criteria:** The private configuration repository exists on GitHub, is cloned locally, and is ready for configuration files.

**Step 8 status: Pending.**
