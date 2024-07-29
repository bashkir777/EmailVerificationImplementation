FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /workspace

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /workspace/target/mail-service.jar mail-service.jar

ENTRYPOINT ["java", "-jar", "mail-service.jar"]