# Stage 1: Build the application
FROM docker.repo1.uhc.com/devopscoe/eclipse-temurin:17-jdk-alpine AS build

# Install necessary tools (e.g., Maven, Git)
RUN apk add --no-cache maven git

# Set the working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (to leverage cache)
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy remaining source code
COPY . .

# Print Maven version and settings for debugging purposes
RUN mvn --version && mvn help:effective-settings

# Build the application with detailed output for debugging purposes
# RUN mvn clean package -e -X || cat /app/target/surefire-reports/*.txt

# Stage 2: Set up the runtime environment
FROM docker.repo1.uhc.com/devopscoe/eclipse-temurin:17-jdk-alpine

# Set environment variables
ENV LANG C.UTF-8 \
    MIN_HEAP_SIZE='-Xms1536m' \
    MAX_HEAP_SIZE='-Xmx1536m' \
    THREADSTACK_SIZE='-Xss512k' \
    JAVA_OPTS=' -server -Duser.timezone=America/Chicago -DDefaultTimeZone=America/Chicago -Djava.security.egd=file:/dev/./urandom -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/logs' \
    JAVA_GC_ARGS='  -XX:+UseConcMarkSweepGC -XX:GCLogFileSize=10M -XX:+UseGCLogFileRotation -XX:+PrintGCTimeStamps -XX:NumberOfGCLogFiles=7 ' \
    JAVA_OPTS_APPEND='' \
    DOCKER_HOST=tcp://<env>uscacr.azurecr.io DOCKER_TLS_VERIFY=1

# Add repositories if needed (optional, based on your setup)
RUN echo 'https://repo1.uhc.com/artifactory/dl-cdn/v3.18/community' > /etc/apk/repositories && \
    echo 'https://repo1.uhc.com/artifactory/dl-cdn/v3.18/main' >> /etc/apk/repositories

# Create necessary directories and set permissions
RUN mkdir -p /logs && chmod 777 /logs && mkdir scripts

# Expose ports required by your application
EXPOSE 4317 8951

# Copy the built jar from the build stage to runtime stage
COPY --from=build /app/target/predictive-engagement-reason-engine-0.0.1-SNAPSHOT.jar /api.jar

# Change to non-root privilege if applicable (replace ACQ with an actual user if needed)
USER ACQ 

# Set the entry point for running the application with specified options and parameters
ENTRYPOINT ["java", "-Xms512M", "-Xmx1024M", "-Xss256k", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:MaxGCPauseMillis=500", "-XX:NativeMemoryTracking=summary", "-Dspring.profiles.active=<envprofile>", "-Dlogging.file=/logs/<service_name>.log", "-Dserver.port=8951", "-jar", "/api.jar"]
