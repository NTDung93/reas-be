FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

COPY --from=build /app/target/reas-be-0.0.1-SNAPSHOT.jar reas-be.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "reas-be.jar"]
