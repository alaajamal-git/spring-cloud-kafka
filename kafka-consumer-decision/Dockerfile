FROM openjdk:11-jre-slim-buster
WORKDIR /app
COPY /target/kafka-api-consumer-decision-0.0.1-SNAPSHOT.jar /app/decision.jar
ENV KAFKA_ADDRESS ${KAFKA_ADDRESS}
ENV KAFKA_PORT ${KAFKA_PORT}
ENV AWS_ACCESS_KEY ${AWS_ACCESS_KEY}
ENV AWS_SECRET_KEY ${AWS_SECRET_KEY}

ENTRYPOINT  ["java","-jar","decision.jar"]