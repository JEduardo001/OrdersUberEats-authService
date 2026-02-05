FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
COPY sql ./sql
EXPOSE 5010
ENTRYPOINT ["java", "-jar", "app.jar"]