FROM openjdk:8-jdk-alpine
VOLUME /tmp
WORKDIR app
COPY ./* /
RUN ls
RUN ./mvnw clean package 

