# Build Stage: Sử dụng image Maven hỗ trợ JDK 21
FROM maven:3.9.0-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime Stage: Sử dụng OpenJDK 21 để chạy ứng dụng
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
