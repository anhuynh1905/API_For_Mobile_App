#------------------------------------------------------------------
# Stage 1: Build the application using Gradle
#------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files FROM the Mobile_API subfolder
# Adjust these paths if gradlew/gradle folder are outside Mobile_API
# Assuming gradlew and the gradle folder are *inside* Mobile_API:
COPY Mobile_API/gradlew .
COPY Mobile_API/gradle ./gradle
RUN chmod +x ./gradlew

# Copy the build configuration files FROM the Mobile_API subfolder
COPY Mobile_API/build.gradle .
# Copy settings.gradle if it exists inside Mobile_API
# COPY Mobile_API/settings.gradle . # Uncomment if needed

# Copy the source code FROM the Mobile_API subfolder
COPY Mobile_API/src ./src

# Build the application JAR file
# Gradle will run inside /app, using the copied project files
RUN ./gradlew bootJar --no-daemon

#------------------------------------------------------------------
# Stage 2: Create the final runtime image
#------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the built JAR file from the builder stage's /app/build/libs/ directory
# The path inside the builder stage remains the same because we ran gradle within /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Optional: Add health check instruction
# HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1
