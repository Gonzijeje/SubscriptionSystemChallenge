FROM openjdk:8-jdk-alpine
RUN apk update && apk add --no-cache gcompat
EXPOSE 8084
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} public-service.jar
ENTRYPOINT ["java","-jar","/public-service.jar"]