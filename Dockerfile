FROM openjdk:17-jdk-slim

LABEL maintainer="healthcare-team"
LABEL application="Patient Health Record System"
LABEL version="1.0"

WORKDIR /app

# Install maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/patient-health-record-1.0-SNAPSHOT.jar"]
