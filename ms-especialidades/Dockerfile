FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/ms-especialidades-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]