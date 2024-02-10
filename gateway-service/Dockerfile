FROM amazoncorretto:21-alpine-jdk
VOLUME /tmp
COPY target/gateway-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
