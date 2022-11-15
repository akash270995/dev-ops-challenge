FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./* ./
RUN ls
#RUN ./mvnw clean package 

