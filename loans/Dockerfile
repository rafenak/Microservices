# Use a multi-stage build to reduce the final image size
FROM openjdk:21-jdk-slim AS build

# Add the application's jar to the image
COPY target/loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar

# Create a new stage with a smaller base image
FROM openjdk:21-jdk-slim

# Information around who maintains the image
LABEL "org.opencontainers.image.authors"="Rafe Nakhuda"

# Copy the jar from the previous stage
COPY --from=build loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar

# Execute the application
ENTRYPOINT ["java", "-jar", "loans-0.0.1-SNAPSHOT.jar"]