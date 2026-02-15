FROM eclipse-temurin:20-jdk-alpine

WORKDIR /app

COPY target/taskmanager-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=production

CMD ["java","-jar","app.jar"]