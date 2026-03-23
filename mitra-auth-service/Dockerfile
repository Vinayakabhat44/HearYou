# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
# Copy parent pom
COPY pom.xml .
# Copy service pom and src
COPY mitra-auth-service/pom.xml mitra-auth-service/
COPY mitra-auth-service/src mitra-auth-service/src
RUN mvn -f mitra-auth-service/pom.xml clean package -Dmaven.test.skip=true

# Run stage
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache wget
WORKDIR /app
RUN wget -O elastic-apm-agent.jar https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/1.45.0/elastic-apm-agent-1.45.0.jar
COPY --from=build /app/mitra-auth-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-javaagent:/app/elastic-apm-agent.jar", "-jar", "app.jar"]
