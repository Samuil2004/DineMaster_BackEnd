# Use the Gradle image as the base image for building the app
FROM gradle:7.5.0-jdk17 AS build

# Set working directory inside the container
WORKDIR /app

# Copy Gradle files and project source code
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
COPY src src

# Disable Gradle daemon for CI builds
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"

# Run Gradle to build the application
RUN chmod +x ./gradlew && ./gradlew assemble

# Second stage: use a lightweight image for running the app
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /opt/app


# Copy the built JAR file from the previous stage
COPY --from=build /app/build/libs/DineMasterBackEnd-0.0.1-SNAPSHOT.jar ./DineMasterBackEnd-0.0.1-SNAPSHOT.jar

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar DineMasterBackEnd-0.0.1-SNAPSHOT.jar"]
