FROM docker.repo1.uhc.com/devopscoe/eclipse-temurin:17-jdk-alpine
ENV LANG C.UTF-8
ENV MIN_HEAP_SIZE='-Xms1536m' 
ENV MAX_HEAP_SIZE='-Xmx1536m' 
ENV THREADSTACK_SIZE='-Xss512k'
    
ENV JAVA_OPTS=' -server -Duser.timezone=America/Chicago -DDefaultTimeZone America/Chicago -Djava.security.egd=file:/dev/./urandom -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/logs'
ENV JAVA_GC_ARGS='  -XX:+UseConcMarkSweepGC -XX:GCLogFileSize=10M -XX:+UseGCLogFileRotation -XX:+PrintGCTimeStamps -XX:NumberOfGCLogFiles=7 '
ENV JAVA_OPTS_APPEND=''
ENV DOCKER_HOST=tcp://<env>uscacr.azurecr.io DOCKER_TLS_VERIFY=1

RUN echo 'https://repo1.uhc.com/artifactory/dl-cdn/v3.18/community' > /etc/apk/repositories
RUN echo 'https://repo1.uhc.com/artifactory/dl-cdn/v3.18/main' >> /etc/apk/repositories
# RUN apt-get update && apt-get install -yy  openssl zlib1g expat libc6 libc-bin libgnutls30 libtirpc3 libpcre2-8-0 libc6 libc-bin libgnutls30 libtirpc3 libpcre2-8-0 libkrb5-3 libtasn1-6 libcurl3-gnutls libcurl4 git-man libexpat1 libtirpc-common dpkg libssl1.1 libk5crypto3 bash sed grep coreutils git openssh-client curl file rsync openssh-client tar wget iputils-ping procps
RUN apk --no-cache add curl

# RUN curl -L https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent.jar -o splunk-otel-javaagent.jar

VOLUME /tmp
RUN mkdir -p /logs
RUN mkdir scripts
EXPOSE 4317
RUN chmod 777 /logs
EXPOSE 4317
EXPOSE 8951
EXPOSE 4317
# RUN useradd -u 5678 ACQ
# Change to non-root privilege
USER ACQ
ADD ./*.jar <service_name>.jar
ENTRYPOINT ["java","-Xms512M", "-Xmx1024M", "-Xss256k","-XX:+HeapDumpOnOutOfMemoryError", "-XX:MaxGCPauseMillis=500", "-XX:NativeMemoryTracking=summary","-Dspring.profiles.active=<envprofile>","-javaagent:./splunk-otel-javaagent.jar","-Dsplunk.profiler.enabled=true","-Dsplunk.profiler.directory=/tmp","-Dsplunk.metrics.enabled=true","-Dlogging.file=/logs/<service_name>.log","-Dserver.port=8951","-jar","<service_name>.jar"]
