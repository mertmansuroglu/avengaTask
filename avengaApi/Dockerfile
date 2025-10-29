# Multi-stage build for API test automation

# Stage 1: Builder
FROM maven:3.9-eclipse-temurin-11 AS builder

WORKDIR /app

# Copy pom.xml first for better layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src
COPY testng.xml .

# Build the project
RUN mvn clean compile test-compile -B

# Stage 2: Runtime (minimal JRE)
FROM maven:3.9-eclipse-temurin-11

WORKDIR /app

# Copy pom.xml and testng.xml
COPY pom.xml .
COPY testng.xml .

# Copy source code
COPY src ./src

# Copy built classes from builder stage
COPY --from=builder /app/target/classes ./target/classes
COPY --from=builder /app/target/test-classes ./target/test-classes

# Download dependencies
RUN mvn dependency:go-offline -B

# Create non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser -m -d /home/appuser appuser

# Set ownership to non-root user
RUN chown -R appuser:appuser /app && chown -R appuser:appuser /home/appuser

# Switch to non-root user
USER appuser

# Environment variables with defaults
ENV BASE_URL=https://fakerestapi.azurewebsites.net
ENV ENV=docker
ENV USERNAME=default_user
ENV PASSWORD=default_pass
ENV API_KEY=default_key

# Run tests when container starts
CMD mvn clean test -DBASE_URL=${BASE_URL} -DENV=${ENV} -DUSERNAME=${USERNAME} -DPASSWORD=${PASSWORD} -DAPI_KEY=${API_KEY}

