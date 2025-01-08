# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files
COPY pom.xml ./
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Create a lightweight image with the application
FROM eclipse-temurin:17-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the built application JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]