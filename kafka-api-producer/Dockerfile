FROM openjdk:11-jre-slim-buster
WORKDIR /app
COPY /target/kafka-api-producer-0.0.1-SNAPSHOT.jar /app/producer.jar
ENV KAFKA_ADDRESS ${KAFKA_ADDRESS}
ENV KAFKA_PORT ${KAFKA_PORT}

ENTRYPOINT  ["java","-jar","producer.jar"]