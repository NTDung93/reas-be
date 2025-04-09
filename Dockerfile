# Build Stage
FROM openjdk:21-jdk-slim AS build

RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime Stage
FROM openjdk:21-jdk-slim
WORKDIR /app

# Cài PostgreSQL client và netcat để hỗ trợ kiểm tra DB
RUN apt-get update && apt-get install -y postgresql-client netcat

# Copy JAR từ build stage
COPY --from=build /app/target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar

# Copy Firebase config nếu cần dùng
COPY --from=build /app/src/main/resources/firebase/reas-app-f46cc-firebase-adminsdk-fbsvc-94587df6b3.json /app/firebase/reas-app-f46cc-firebase-adminsdk-fbsvc-94587df6b3.json

# Copy các file script khởi động
COPY docker/entrypoint.sh /entrypoint.sh
COPY docker/wait-for-it.sh /wait-for-it.sh

# Cấp quyền thực thi
RUN chmod +x /entrypoint.sh /wait-for-it.sh

EXPOSE 8080

# Thiết lập điểm khởi động là entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]
