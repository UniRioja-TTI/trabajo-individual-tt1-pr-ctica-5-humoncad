# este es el dockerfile que he usado para la p6
FROM eclipse-temurin:17 
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

# imagen base con java17, defino la ubicacion del jar, lo copio al contenedor y ENTRYPOINT como comando para arrancar la app
