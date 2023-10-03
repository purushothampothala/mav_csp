# Fetching latest version of Java
FROM openjdk:17

# Setting up work directory
WORKDIR /app

# Copy the jar file into our app
COPY ./target/server-0.0.1-SNAPSHOT.jar /app

# Exposing port 8761
EXPOSE 8761

# Starting the application
CMD ["java", "-jar", "server-0.0.1-SNAPSHOT.jar"]
