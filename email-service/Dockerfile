FROM openjdk:8-jdk-alpine
EXPOSE 8087
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} email-service.jar
ENTRYPOINT ["java","-jar","/email-service.jar"]