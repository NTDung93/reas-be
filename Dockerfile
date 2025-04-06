# Build Stage: Use OpenJDK 21 and install Maven manually
FROM openjdk:21-jdk-slim AS build
COPY . .
RUN mvn clean package -DskipTests

# Runtime Stage: Use OpenJDK 21 for running the application
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
