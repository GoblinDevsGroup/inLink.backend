FROM openjdk:21-jdk AS builder
WORKDIR /app
RUN apt-get update && apt-get install -y xargs
COPY gradle/ gradle/
COPY build.gradle settings.gradle gradlew ./
RUN ./gradlew dependencies
COPY src ./src
RUN ./gradlew clean build

FROM openjdk:21-jdk
WORKDIR /app
COPY build/libs/Adds-0.0.1-SNAPSHOT.jar myapp.jar
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "myapp.jar"]