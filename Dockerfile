# Sử dụng Temurin JDK 21 làm base image
FROM eclipse-temurin:21-jdk AS build

# Cài đặt Maven
RUN apt-get update && apt-get install -y \
    wget \
    unzip && \
    wget https://dlcdn.apache.org/maven/maven-3/3.9.1/binaries/apache-maven-3.9.1-bin.tar.gz && \
    tar -xvzf apache-maven-3.9.1-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.1 /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn

# Sao chép mã nguồn và build ứng dụng
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Sử dụng OpenJDK 21 làm runtime base image
FROM eclipse-temurin:21-jdk-slim
WORKDIR /app

# Sao chép file jar đã build từ image build vào image runtime
COPY --from=build /app/target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar

# Expose port 8080 và chạy ứng dụng
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
