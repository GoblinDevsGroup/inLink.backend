FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
COPY gradle/ gradle/
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x ./gradlew && ./gradlew dependencies --no-daemon
COPY src/ ./src/
RUN ./gradlew clean build --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY build/libs/Adds-0.0.1-SNAPSHOT.jar myapp.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "myapp.jar"]