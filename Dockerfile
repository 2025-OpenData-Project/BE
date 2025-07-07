FROM openjdk:21-jdk-slim
COPY ./build/libs/*SNAPSHOT.jar project.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "project.jar"]