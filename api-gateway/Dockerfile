# Fetching latest version of Java
FROM openjdk:17

# Setting up work directory
WORKDIR /app

# Copy the jar file into our app
COPY ./target/api-gateway-0.0.1-SNAPSHOT.jar /app

# Exposing port 8082
EXPOSE 8082

# Starting the application
CMD ["java", "-jar", "api-gateway-0.0.1-SNAPSHOT.jar"]
