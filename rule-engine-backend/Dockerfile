# Use the official Maven image to build the application
FROM maven:3.8.6-openjdk-8 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml ./
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight JRE for running the application
FROM openjdk:8-jre-slim

# Set the working directory in the final image
WORKDIR /app

# Copy the built JAR file from the previous build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application on port 9090
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

