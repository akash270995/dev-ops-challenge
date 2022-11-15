FROM openjdk:8-jdk-alpine
VOLUME /tmp
WORKDIR app
COPY ./* app
RUN ./mvnw clean package 

