# REST API Test Automation Framework

Test automation framework for the FakeRestAPI bookstore endpoints. Written in Java using TestNG and RestAssured.

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Configuration](#configuration)
- [How to Run Tests](#how-to-run-tests)
- [Viewing Reports](#viewing-reports)
- [Project Structure](#project-structure)
- [Test Coverage](#test-coverage)
- [CI/CD](#cicd)
- [Contributing](#contributing)

## Overview

Tests Books and Authors APIs. Uses RestAssured for HTTP calls and TestNG for test execution. Includes data-driven tests, JSON schema validation, and ExtentReports for reporting.

## Tech Stack

- **Java**: 11 (OpenJDK Temurin)
- **Build Tool**: Apache Maven 3.9+
- **Test Framework**: TestNG 7.8.0
- **HTTP Client**: RestAssured 5.3.2
- **Reporting**: ExtentReports 5.1.1
- **Logging**: Log4j2 2.20.0
- **Data Binding**: Jackson 2.15.2
- **Test Data**: JavaFaker 1.0.2
- **Utilities**: Lombok 1.18.40
- **Containerization**: Docker
- **CI/CD**: GitHub Actions

## Prerequisites

Requirements:
- JDK 11+
- Maven 3.6+
- Docker (optional)

Check installation:
```bash
java -version
mvn -version
docker --version
```

## Quick Start

```bash
# Clone and build
git clone <repository-url>
cd api
mvn clean install
```

## Configuration

Configuration files in `src/main/resources/config/`. Default is `qa.properties`.

Run against different environment:
```bash
mvn test -Denv=qa
```

Override config at runtime:
```bash
mvn test -DBASE_URL=https://api.example.com
```

## How to Run Tests

### Maven
```bash
# Run all tests
mvn clean test

# Run specific test
mvn test -Dtest=BooksApiTest
```

### Docker
```bash
# Build and run
docker build -t bookstore-tests .
docker run --rm bookstore-tests

# With custom config
docker run --rm -e BASE_URL=https://api.example.com bookstore-tests
```

## Viewing Reports

Reports:
- ExtentReports: `test-output/extent-report.html`
- TestNG reports: `test-output/`
- Logs: `logs/api-tests.log`

From GitHub Actions: download the artifact from Actions tab.

## Test Coverage

### Books API (15 tests)
- GET all books, GET by ID
- POST create, PUT update, DELETE
- Invalid ID, non-existent resource
- Minimal data, special characters
- Partial update
- Data-driven tests (3 test cases)

### Authors API (11 tests)
- Full CRUD operations
- Edge cases, data validation

**Total**: 26 tests

## CI/CD

GitHub Actions workflow at `.github/workflows/ci.yml`:
- Build and test on push to main/master/develop
- Matrix testing with Java 11 and 17
- Docker build
- Test reports published as artifacts

## Contributing

Fork, create feature branch, commit changes, push and open PR.
