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
- Base de datos `units_db` creada en MySQL `127.0.0.1:3306` con codificación `utf8mb4_unicode_ci` y usuario `resi_dev` autorizado.

---

## Fase 2: Configuración (`application.properties`) y Migración Flyway (`V1__init_units.sql`)

- **[V1__init_units.sql](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/resources/db/migration/V1__init_units.sql):**
  - Define la tabla `units` con llave primaria `id VARCHAR(36)`, `number VARCHAR(20) NOT NULL UNIQUE`, `block_tower VARCHAR(50)`, `type VARCHAR(20) NOT NULL` (`RESIDENTIAL` o `COMMERCIAL`), campos exclusivos comerciales (`commercial_name`, `business_type`, `opening_hours`), `status VARCHAR(20) NOT NULL DEFAULT 'OCCUPIED'` y columnas de auditoría (`created_at`, `updated_at`, `deleted_at`).
  - Define los índices de consulta `idx_units_type` e `idx_units_status`.
- **[application.properties](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/resources/application.properties):**
  - Configura puerto `8083`, conexión JDBC a `units_db`, usuario `resi_dev`, contraseña parametrizada `${DB_PASSWORD}`, validación estricta de esquema `spring.jpa.hibernate.ddl-auto=validate`, `spring.jpa.show-sql=true`, `spring.jpa.open-in-view=false` y migraciones automáticas de Flyway.

---

## Fase 3: Capa de Modelo (`model/`) y Repositorio (`repository/`)

- **[UnitType.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/java/com/resicomplex/units/model/UnitType.java):**
  - Enum con los dos tipos de inmueble del conjunto mixto: `RESIDENTIAL` y `COMMERCIAL`.
- **[Unit.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/java/com/resicomplex/units/model/Unit.java):**
  - Entidad JPA (`@Entity`, `@Table(name = "units")`) con `@Getter`, `@Setter` y `@NoArgsConstructor` de Lombok.
  - Mapea cada columna exactamente con las longitudes y restricciones del script Flyway para superar la validación de Hibernate (`ddl-auto=validate`).
  - Usa `@Enumerated(EnumType.STRING)` sobre `UnitType type`.
- **[UnitRepository.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/java/com/resicomplex/units/repository/UnitRepository.java):**
  - Extiende `JpaRepository<Unit, String>`.
  - Incluye `List<Unit> findByType(UnitType type)` (para filtrar unidades residenciales o comerciales) y `boolean existsByNumber(String number)` (para validar unicidad del número de unidad).

---

## Fase 4: Capa de DTOs (`dto/`)

- **[CreateUnitRequest.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/java/com/resicomplex/units/dto/CreateUnitRequest.java):**
  - Implementado como `record` de Java inmutable.
  - Aplica validaciones Jakarta Bean Validation (`@NotBlank` en `number`, `@NotNull` en `type`, y `@Size` acorde a las longitudes de la base de datos).
- **[UnitResponse.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/java/com/resicomplex/units/dto/UnitResponse.java):**
  - Implementado como `record` de Java con todos los datos públicos de la unidad (`id`, `number`, `blockTower`, `type`, `commercialName`, `businessType`, `openingHours`, `status`, `createdAt`, `updatedAt`).

---

## Fase 5: Capa de Servicio (`service/`)

- **[IUnitService.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/java/com/resicomplex/units/service/IUnitService.java):**
  - Contrato de negocio con los métodos `createUnit`, `getAllUnits`, `getUnitsByType` y `getUnitById`.
- **[UnitService.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/java/com/resicomplex/units/service/UnitService.java):**
  - Valida que no exista otra unidad con el mismo `number` (lanza `409 CONFLICT` si se repite).
  - Valida que si el tipo es `COMMERCIAL`, el campo `commercialName` sea obligatorio (lanza `400 BAD_REQUEST` si falta), y si es `RESIDENTIAL`, limpia los campos comerciales a `null`.
  - Genera el identificador con `UUID.randomUUID().toString()` y asigna el estado inicial `OCCUPIED`.
  - Lanza `404 NOT_FOUND` cuando se consulta un ID inexistente.

---

## Fase 6: Capa Controlador (`controller/`)

- **[UnitController.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/main/java/com/resicomplex/units/controller/UnitController.java):**
  - Expone la ruta base `/api/v1/units`:
    - `POST /api/v1/units` (`201 Created`): Crea una unidad validando el JSON con `@Valid @RequestBody`.
    - `GET /api/v1/units` (`200 OK`): Lista todas las unidades o filtra por tipo si se envía `?type=RESIDENTIAL` o `?type=COMMERCIAL`.
    - `GET /api/v1/units/{id}` (`200 OK`): Devuelve el detalle de una unidad específica por su ID.

---

## Fase 7: Pruebas Unitarias y Verificación

- **[UnitServiceTest.java](file:///c:/Users/Alexa_Var/Documents/resi-complex-units-service/src/test/java/com/resicomplex/units/service/UnitServiceTest.java):**
  - Pruebas unitarias con JUnit 5 y Mockito (`@ExtendWith(MockitoExtension.class)`) verificando:
    1. Creación exitosa de unidad residencial.
    2. Creación exitosa de local comercial con datos de negocio.
    3. Rechazo por número de unidad duplicado (`409 CONFLICT`).
    4. Rechazo de local comercial sin nombre comercial (`400 BAD_REQUEST`).
    5. Filtrado por tipo de unidad (`findByType`).
    6. Error `404 NOT_FOUND` al buscar un ID inexistente.
