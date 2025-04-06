# Build Stage: Use OpenJDK 21 and install Maven manually
FROM openjdk:21-jdk-slim AS build

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the project files to the container
COPY . .

# Build the project using Maven
RUN mvn clean package -DskipTests

# Runtime Stage: Use OpenJDK 21 for running the application
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
