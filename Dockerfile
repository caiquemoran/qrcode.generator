FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ARG MINIO_ACCESS_KEY
ARG MINIO_SECRET_KEY

ENV MINIO_ACCESS_KEY=$MINIO_ACCESS_KEY
ENV MINIO_SECRET_KEY=$MINIO_SECRET_KEY

ENTRYPOINT ["java", "-jar", "app.jar"]