# resi-complex-units-service

Microservicio de Inventario de Unidades Residenciales y Establecimientos Comerciales para **resi-complex** (Core Domain: diferenciador del negocio).

## Configuración
- **Puerto:** `8083`
- **Base de datos MySQL:** `units_db`
- **Migraciones Flyway:** `src/main/resources/db/migration/V1__init_units.sql`
- **Swagger UI:** `http://localhost:8083/swagger-ui/index.html`

## Endpoints principales (`/api/v1/units`)
- `POST /api/v1/units` — Crear unidad residencial o establecimiento comercial (`201 Created`)
- `GET /api/v1/units` — Listar todas las unidades (filtro opcional `?type=RESIDENTIAL|COMMERCIAL`)
- `GET /api/v1/units/{id}` — Obtener detalle de una unidad por ID (`200 OK`)
