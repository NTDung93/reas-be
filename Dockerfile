# Build Stage
FROM openjdk:21-jdk-slim AS build

RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
