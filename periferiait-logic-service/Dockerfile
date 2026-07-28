# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Cache de dependencias: primero wrapper + archivos de build
COPY gradlew ./
COPY gradle ./gradle
COPY settings.gradle build.gradle ./
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies || true

# Código y empaquetado (sin tests dentro de la imagen)
COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
