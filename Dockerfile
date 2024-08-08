# Sample
FROM eclipse-temurin:21-jdk-alpine

LABEL authors="gimjunseong"

WORKDIR build/lib/*.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "/app/myapp.jar"]
