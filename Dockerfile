# Base image: Debian 12 (Bookworm)
FROM debian:12

# Arguments for Tesseract version and URL
ARG TESSERACT_VERSION="main"
ARG TESSERACT_URL="https://api.github.com/repos/tesseract-ocr/tesseract/tarball/$TESSERACT_VERSION"

# Install dependencies for Tesseract and Java (OpenJDK 17)
RUN apt-get update && apt-get install --no-install-recommends --yes \
    apt-transport-https \
    ca-certificates \
    wget \
    git \
    automake \
    g++ \
    libleptonica-dev \
    libtool \
    libicu-dev \
    libpango1.0-dev \
    libcairo2-dev \
    make \
    pkg-config \
    openjdk-17-jdk \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory for Tesseract source
WORKDIR /src

# Download and extract Tesseract source code from the given URL
RUN wget -qO tesseract.tar.gz $TESSERACT_URL && \
    tar -xzf tesseract.tar.gz && \
    rm tesseract.tar.gz && \
    mv tesseract-* tesseract

# Build and install Tesseract from source
WORKDIR /src/tesseract
RUN ./autogen.sh && \
    ./configure && \
    make && \
    make install && \
    ldconfig

# Set the TESSDATA_PREFIX environment variable for Tesseract
ENV TESSDATA_PREFIX=/usr/local/share/tessdata/

# Create a directory for Tesseract traineddata
RUN mkdir -p /tesseract/tessdata

# Copy the Tesseract traineddata file (German) into the container
COPY src/main/resources/tessdata/deu.traineddata /tesseract/tessdata

# Copy your Spring Boot JAR (pre-built outside the container) into the container
COPY target/quisine-0.0.1-SNAPSHOT.jar /app.jar

# Set the ENTRYPOINT to run the Spring Boot application using Java
ENTRYPOINT ["java", "-jar", "/app.jar"]