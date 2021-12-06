FROM openjdk:11.0.12-jdk-slim

ADD target/coinsbcp.jar coinsbcp.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "coinsbcp.jar"]