FROM openjdk:8-jdk-alpine
VOLUME /tmp
RUN ./mvnw clean package
ADD target/Outcome-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
