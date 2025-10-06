# --- ETAPA 1: Build ---
# Usa una imagen de Maven con JDK 17 para compilar la aplicación
FROM maven:3.8.5-openjdk-17 AS builder

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia primero el pom.xml para aprovechar el cache de Docker si las dependencias no cambian
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia el resto del código fuente
COPY src ./src

# Compila la aplicación y empaquétala en un JAR
RUN mvn package -DskipTests

# --- ETAPA 2: Run ---
# Usa una imagen base de JRE mucho más ligera para ejecutar la aplicación
FROM eclipse-temurin:17-jre-jammy

# Establece el directorio de trabajo
WORKDIR /app

# Copia únicamente el JAR compilado de la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto en el que correrá la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]