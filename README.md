# spring-boot-postgres-embedded

[![Java CI with Maven](https://github.com/hendisantika/spring-boot-postgres-embedded/actions/workflows/maven.yml/badge.svg)](https://github.com/hendisantika/spring-boot-postgres-embedded/actions/workflows/maven.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18%20(embedded)-336791?logo=postgresql)

A Spring Boot application that starts its own **embedded PostgreSQL** server on boot, so you can run and test
against a real PostgreSQL database without installing one or running Docker.

## Tech Stack

| Component              | Version                                                                    |
|------------------------|----------------------------------------------------------------------------|
| Java                   | 25                                                                         |
| Spring Boot            | 4.1.2 (Web MVC, Data JPA, Actuator, DevTools)                              |
| Hibernate ORM          | 7.x (managed by Spring Boot)                                               |
| Embedded PostgreSQL    | [Zonky embedded-postgres](https://github.com/zonkyio/embedded-postgres) 2.2.2 with PostgreSQL 18.6 binaries |
| MapStruct              | 1.6.3                                                                      |
| Lombok                 | managed by Spring Boot                                                     |
| Build tool             | Maven 3.9.16 (via Maven Wrapper)                                           |

## How It Works

`EmbeddedPostgresConfig` starts a PostgreSQL server through Zonky's `EmbeddedPostgres` and registers it as the
application's `DataSource`. The server shuts down with the Spring context. The PostgreSQL binaries come from Maven
artifacts (`embedded-postgres-binaries-bom`), so they work on Linux, macOS (Intel and Apple Silicon) and Windows
without a separate download step.

```
src/main/java/id/my/hendisantika/springbootpostgresembedded
├── SpringBootPostgresEmbeddedApplication.java
└── config
    └── EmbeddedPostgresConfig.java   # starts embedded PostgreSQL & exposes the DataSource
```

## Prerequisites

- JDK 25 (e.g. [Eclipse Temurin](https://adoptium.net/))
- Nothing else. Maven comes from the wrapper, and PostgreSQL is embedded.

## Getting Started

```bash
git clone https://github.com/hendisantika/spring-boot-postgres-embedded.git
cd spring-boot-postgres-embedded

# build & run tests
./mvnw clean verify

# run the application
./mvnw spring-boot:run
```

Check that the app is up:

```bash
curl http://localhost:8080/actuator/health
```

## Configuration

`src/main/resources/application.properties`:

| Property                        | Default       | Description                                             |
|---------------------------------|---------------|---------------------------------------------------------|
| `embedded.postgres.port`        | `0`           | Port for the embedded PostgreSQL (`0` = random free port) |
| `spring.jpa.hibernate.ddl-auto` | `create-drop` | Schema is created on startup and dropped on shutdown    |
| `spring.jpa.show-sql`           | `true`        | Log generated SQL                                       |

Set a fixed port to connect with an external client such as `psql` or DBeaver while the app runs:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--embedded.postgres.port=5433
psql -h localhost -p 5433 -U postgres postgres
```

## CI/CD

- **GitHub Actions** (`.github/workflows/maven.yml`): builds and tests on Temurin JDK 25 for every push and pull
  request to `main`. It caches Maven dependencies and uploads the test reports and the application jar as artifacts.
- **Dependabot** (`.github/dependabot.yml`): checks Maven dependencies and GitHub Actions every week. It groups
  Spring Boot, embedded-postgres, MapStruct and other minor/patch updates into fewer PRs, skips pre-releases, and
  uses conventional commit prefixes.

## Author

**Hendi Santika**

- GitHub: [@hendisantika](https://github.com/hendisantika)
