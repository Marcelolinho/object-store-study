FROM ubuntu:latest
LABEL authors="marcelo"

ENTRYPOINT ["top", "-b"]

FROM maven:3.9.8-eclipse-temurin-21 AS build

COPY pom.xml ./target
COPY src ./src

RUN mvn clean install
RUN find / -name target

COPY --from=build target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]