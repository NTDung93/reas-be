# Build Stage: Use OpenJDK 21 and install Maven manually
FROM openjdk:21-jdk-slim AS build

# Install necessary tools (wget for downloading Maven)
RUN apt-get update && apt-get install -y wget

# Download and install Maven 3.9.6
RUN wget https://downloads.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz \
    && tar -xvf apache-maven-3.9.6-bin.tar.gz -C /opt \
    && ln -s /opt/apache-maven-3.9.6 /opt/maven

# Set Maven environment variables
ENV MAVEN_HOME /opt/maven
ENV PATH ${MAVEN_HOME}/bin:${PATH}

# Copy your project files and build the application
COPY . .
RUN mvn clean package -DskipTests

# Runtime Stage: Use OpenJDK 21 for running the application
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
