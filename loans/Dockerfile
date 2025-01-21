#Start with a base image containing Java runtime
FROM openjdk:21-jdk-slim

# MAINTAINER instruction is deprecated in favor of using label
# MAINTAINER Rafe Nakhuda
#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="Rafe Nakhuda"

# Add the application's jar to the image
COPY target/loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "loans-0.0.1-SNAPSHOT.jar"]