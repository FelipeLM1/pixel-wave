FROM amazoncorretto:21-alpine-jdk
VOLUME /tmp
ARG JAR_FILE
COPY target/config-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
