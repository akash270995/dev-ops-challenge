FROM openjdk:8-jdk-alpine
VOLUME /tmp
WORKDIR app
COPY ./* app/
RUN ls
RUN ./mvnw clean package 

