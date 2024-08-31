FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY ${JAR_FILE} /app/testproject-0.0.1.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/testproject-0.0.1.jar","-web -webAllowOthers -tcp -tcpAllowOthers -browser"]
