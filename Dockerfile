FROM ubuntu:latest
LABEL authors="marcelo"

ENTRYPOINT ["top", "-b"]

FROM maven:3.9.8-eclipse-temurin-21 AS build

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

COPY target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]