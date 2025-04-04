# Stage 1: Build the application
FROM maven:3.9.1-eclipse-temurin-21 AS build

# Set working directory and copy project files
WORKDIR /app
COPY . .

# Build the application (skip tests to speed up build)
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-jdk

# Set working directory
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar

# Expose port 8080 and run the application
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
