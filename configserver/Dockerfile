#Start with a base image containing Java runtime
FROM openjdk:21-jdk-slim

# MAINTAINER instruction is deprecated in favor of using label
# MAINTAINER Rafe Nakhuda
#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="Rafe Nakhuda"

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Add the application's jar to the image
COPY target/configserver-0.0.1-SNAPSHOT.jar configserver-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "configserver-0.0.1-SNAPSHOT.jar"]