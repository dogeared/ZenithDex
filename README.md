# ZenithDex

![CI](https://github.com/dogeared/zenithdex/actions/workflows/ci.yml/badge.svg)
![Version](https://img.shields.io/badge/version-0.2.0-blue)
![Frontend coverage](https://img.shields.io/badge/frontend%20coverage-98%25-brightgreen)
![Backend coverage](https://img.shields.io/badge/backend%20coverage-97%25-brightgreen)

ZenithDex is a card browser for the Zenith tabletop game. It is built with
Spring Boot and Vaadin and serves a responsive, filterable card viewer from a
single Java application.

## Stack

- Java 21
- Spring Boot 4.1
- Vaadin 25.3
- Maven
- JUnit 6, AssertJ, and Vaadin Browserless Testing
- JaCoCo for coverage

## Run locally

```sh
./mvnw spring-boot:run
```

Open <http://localhost:8080>.

## Test

```sh
./mvnw verify
```

The test suite includes unit tests for the model and services, Spring context
and security tests, and Vaadin browserless tests that exercise the frontend view
without a browser. JaCoCo writes the coverage report to
`target/site/jacoco/index.html` and fails the build if overall line coverage is
below 90%.

## Production build

```sh
./mvnw clean package -Pproduction
```

The Docker image builds the same way; see `Dockerfile` and `docker-compose.yml`.

---

made with ❤️ by [dogeared](https://github.com/dogeared).
