# =============================================================================
# Multi-stage Dockerfile para inventory-management-service
# =============================================================================

# Etapa 1: Compilacion y empaquetado con Maven y Eclipse Temurin JDK 21
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar archivos de construccion para cachear dependencias
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B -q || true

# Copiar codigo fuente y compilar omitiendo pruebas para velocidad de imagen
COPY src src
RUN ./mvnw clean package -DskipTests -B -q

# Etapa 2: Imagen de ejecucion ligera optimizada con JRE 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario sin privilegios para seguridad en el contenedor
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
