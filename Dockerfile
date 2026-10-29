# syntax=docker/dockerfile:1
# Multi-stage Dockerfile for kanagat-beken-student-api
# Author: Kanagat Beken
# Build:  docker build -t kanagat-beken-student-api:v1.0-kanagat-beken .

# ---------- Stage 1: build with Gradle on an official JDK image ----------
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

# Copy the Gradle wrapper and build scripts first to cache dependencies
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

# Copy sources and build the executable JAR (tests run in the Jenkins "Test" stage)
COPY src ./src
RUN ./gradlew --no-daemon bootJar -x test

# ---------- Stage 2: small runtime image with only the JRE ----------
FROM eclipse-temurin:17-jre-jammy
LABEL maintainer="Kanagat Beken" \
      org.opencontainers.image.title="kanagat-beken-student-api" \
      org.opencontainers.image.description="Spring Boot Student CRUD API by Kanagat Beken"

WORKDIR /app
RUN useradd --system --create-home --shell /usr/sbin/nologin appuser
COPY --from=build /workspace/build/libs/app.jar /app/app.jar
USER appuser

ENV STUDENT_NAME="Kanagat Beken"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
