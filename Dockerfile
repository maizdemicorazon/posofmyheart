# Fase de construcción (usa Gradle para generar el JAR)
FROM gradle:8.4-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon  # Construye el JAR

# Fase de ejecución (usa JRE para ahorrar espacio)
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]