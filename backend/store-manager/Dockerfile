# Use Eclipse Temurin 21 as the base image (more secure and maintained)
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better caching)
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Make mvnw executable and download dependencies
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Expose the port the app runs on
EXPOSE 8080

# Enable Spring Boot DevTools for development
ENV SPRING_DEVTOOLS_RESTART_ENABLED=true
ENV SPRING_DEVTOOLS_LIVERELOAD_ENABLED=true

# Run Spring Boot in development mode with auto-reload
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-Dspring.devtools.restart.enabled=true"]
