# Stage 1: Build the application
FROM gradle:7.5-jdk17 AS builder

# Set the working directory
WORKDIR /app

# Copy only the necessary files for dependency resolution first to leverage Docker cache
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle gradle

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Now copy the source code and other project files
COPY src src

# Build the application
RUN ./gradlew build --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Define build argument for the version
ARG VERSION

# Copy the jar file from the builder stage
COPY --from=builder /app/build/libs/CollocateTrackerBE-*.jar app.jar


# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]



