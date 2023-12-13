FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/NewFeed.backend-0.0.1-SNAPSHOT.jar NewFeed.backend-0.0.1-SNAPSHOT.jar
EXPOSE 8086
CMD ["java" ,"-jar","NewFeed.backend-0.0.1-SNAPSHOT.jar"]