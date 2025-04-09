# ---------- Build Stage ----------
FROM eclipse-temurin:21-jdk AS build

# Cài Maven để build source
RUN apt-get update && apt-get install -y maven

# Làm việc trong thư mục /app
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build ứng dụng mà không chạy test
RUN mvn clean package -DskipTests


# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jdk

# Cài PostgreSQL client và netcat để hỗ trợ entrypoint kiểm tra kết nối DB
RUN apt-get update && apt-get install -y postgresql-client netcat-openbsd && apt-get clean

# Tạo thư mục làm việc
WORKDIR /app

# Copy file JAR từ build stage
COPY --from=build /app/target/reas-be-0.0.1-SNAPSHOT.jar /app/reas-be.jar

# Copy Firebase config nếu cần
COPY --from=build /app/src/main/resources/firebase/reas-app-f46cc-firebase-adminsdk-fbsvc-94587df6b3.json /app/firebase/reas-app-firebase-adminsdk.json

# Copy các script khởi động
COPY docker/entrypoint.sh /entrypoint.sh
COPY docker/wait-for-it.sh /wait-for-it.sh

# Cấp quyền thực thi cho các script
RUN chmod +x /entrypoint.sh /wait-for-it.sh

# Mở cổng ứng dụng
EXPOSE 8080

# Thiết lập entrypoint
ENTRYPOINT ["/entrypoint.sh"]
