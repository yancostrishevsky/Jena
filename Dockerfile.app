FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml ./
COPY src ./src
COPY data ./data
COPY queries ./queries
COPY rules ./rules

RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/jena-lab6.jar /app/app.jar
COPY data ./data
COPY queries ./queries
COPY rules ./rules

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
CMD ["io"]
