FROM openjdk:8-jdk-alpine
EXPOSE 8086
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} subscription-service.jar
ENTRYPOINT ["java","-jar","/subscription-service.jar"]