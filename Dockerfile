# Build stage
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk maven -y
COPY . .

# Build the application using Maven
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

EXPOSE 8080

# Copy the built jar file from the build stage
COPY --from=build /target/Visit-1.0-SNAPSHOT.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
