FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# Copy gradle files first for better layer caching
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./

# Download dependencies first (cached if no changes to build.gradle)
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ ./src/

# Build the application
RUN ./gradlew clean build --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the jar file from builder stage
COPY --from=builder /app/build/libs/Adds-0.0.1-SNAPSHOT.jar myapp.jar

EXPOSE 9000
ENTRYPOINT ["java", "-jar", "myapp.jar"]