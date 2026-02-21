# Build stage with Maven and Java 25
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
# Fix: -Dmaven.test.skip=true skips both test compilation AND execution.
# -DskipTests only skips execution but still compiles tests, which fails
# when the app container doesn't need test dependencies at runtime.
RUN mvn clean package -Dmaven.test.skip=true

# Run stage with Java 25
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
