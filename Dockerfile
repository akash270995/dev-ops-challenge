FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./* ./
#RUN ls
RUN chmod 775 mvnw
RUN ./mvnw clean package 

