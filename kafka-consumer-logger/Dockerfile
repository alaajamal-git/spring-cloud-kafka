FROM openjdk:11-jre-slim-buster
WORKDIR /app
COPY /target/kafka-consumer-logger-0.0.1-SNAPSHOT.jar /app/logger.jar
ENV KAFKA_ADDRESS ${KAFKA_ADDRESS}
ENV KAFKA_PORT ${KAFKA_PORT}
ENV MONGO_STRING ${MONGO_STRING}

ENTRYPOINT  ["java","-jar","logger.jar"]