FROM openjdk:17-jdk-slim
# Install Tesseract
RUN apt-get update && apt-get install -y tesseract-ocr \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /tesseract/tessdata

COPY src/main/resources/tessdata/deu.traineddata tesseract/tessdata
ARG JAR_FILE=target/quisine-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]