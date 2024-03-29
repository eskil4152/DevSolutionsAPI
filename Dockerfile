# Stage 1: Build
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY src/main/resources/application-production.yaml application.yaml
COPY --from=builder /app/target/DevSolutionsAPI-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "DevSolutionsAPI-0.0.1-SNAPSHOT.jar"]
