FROM ghcr.io/gkedu/openjdk:8u212-jdk-alpine
COPY target/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-Dlogging.config=/conf/logback.xml","-jar","/app.jar","--spring.profiles.active=docker"]
