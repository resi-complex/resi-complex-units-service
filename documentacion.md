# Documentación de Desarrollo — `resi-complex-units-service`

**Responsable:** Alexandra Garzón  
**Rol:** Backend Developer & UX Specialist (Inmuebles, Personas y Mantenimiento)  
**Microservicio:** `resi-complex-units-service` (Puerto `8083` | Base de datos: `units_db`)

---

## Fase 1: Base de Datos y Estructura Base Maven (Scaffolding)

### 1. Archivos de Configuración Base Creados
Siguiendo el estándar exacto de los demás microservicios de la organización `resi-complex`:

- **[pom.xml](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/pom.xml):**
  - Parent: `org.springframework.boot:spring-boot-starter-parent:4.0.8`
  - Versión de Java: `25`
  - `groupId`: `com.resicomplex`
  - `artifactId`: `units-service`
  - Dependencias: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-flyway`, `spring-boot-starter-validation`, `flyway-mysql`, `mysql-connector-j`, `lombok` (con `annotationProcessorPaths` para compilación y tests), `springdoc-openapi-starter-webmvc-ui:3.0.3` y los starters de pruebas (`*-test`).
- **[.gitignore](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/.gitignore) y [.gitattributes](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/.gitattributes):**
  - Exclusión de `target/`, configuraciones de IDE (`.idea/`, `.vscode/`, `*.iml`), archivos `.env` y `application-local.properties`.
- **Maven Wrapper ([mvnw](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/mvnw), [mvnw.cmd](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/mvnw.cmd), [.mvn/wrapper/maven-wrapper.properties](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/.mvn/wrapper/maven-wrapper.properties)):**
  - Configurado con `wrapperVersion=3.3.4` y Apache Maven `3.10.0`.
- **[README.md](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/README.md):**
  - Documentación general del servicio, puerto `8083`, base de datos `units_db`, ruta de migración Flyway y resumen de endpoints `/api/v1/units`.
- **[UnitsServiceApplication.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/java/com/resicomplex/units/UnitsServiceApplication.java):**
  - Clase principal `@SpringBootApplication` en el paquete base `com.resicomplex.units`.

### 2. Base de Datos MySQL (`units_db`)
- Script SQL requerido en el servidor MySQL local:
  ```sql
  CREATE DATABASE IF NOT EXISTS units_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
  ```
