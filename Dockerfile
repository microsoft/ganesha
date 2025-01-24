FROM maven:3-eclipse-temurin-17 AS build

WORKDIR /.

COPY . .

RUN mvn package

FROM eclipse-temurin:17-jdk-alpine

COPY --from=build ./target/ganesha-0.0.1-SNAPSHOT.jar ./api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/api.jar"]