# Sử dụng một image Maven chính thức với JDK 21 để build ứng dụng
FROM maven:3.9.1-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Sử dụng một image OpenJDK 21 runtime làm base image
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
