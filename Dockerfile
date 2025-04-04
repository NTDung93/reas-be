# Use an official Maven image with Java 21 to build the application
FROM maven:3.9.6-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Use an official OpenJDK 21 runtime as a base image
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
