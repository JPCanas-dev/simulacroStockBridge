# StockBridge - Sistema de Gestión de Inventario y Solicitudes Internas

Solución backend basada en Arquitectura Hexagonal, Spring Boot 4, JPA Avanzado, Seguridad JWT, Observabilidad y Docker Compose.

---

## 1. Dónde configurar tus credenciales (Base de Datos, JWT, etc.)

Tienes dos formas de cambiar el nombre de la BD, usuario y contraseña:

### Opción A (Recomendada para Docker Compose): Archivo `.env`
En la raíz del proyecto existe el archivo `.env`:
```env
DB_NAME=stockbridge_inventory
DB_USER=postgres
DB_PASSWORD=postgres
DB_PORT=5432
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION_MS=86400000
```
Si cambias los valores en este archivo, `docker compose up` tomará automáticamente tus nuevas credenciales sin tener que tocar código.

### Opción B (Para ejecución local directa desde IntelliJ/IDE): `application.properties`
Ubicado en `inventory/src/main/resources/application.properties`:
```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/stockbridge_inventory}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
```
Aquí puedes cambiar el valor por defecto que viene después de los dos puntos `:`.

---

## 2. Instrucciones de Ejecución

### Con Docker Compose (Un solo comando):
Desde la carpeta raíz del proyecto (`simulacroStockBridge`):
```bash
docker compose up --build
```
Esto levantará:
- **PostgreSQL 16**: `localhost:5432`
- **supplier-availability-service**: `http://localhost:8081`
- **inventory-management-service**: `http://localhost:8080` (con Flyway aplicando automáticamente `V1` y `V2`).

---

## 3. Endpoints Principales y Documentación

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Métricas Prometheus**: `http://localhost:8080/actuator/prometheus`
- **Salud del Sistema (incluyendo proveedor externo)**: `http://localhost:8080/actuator/health`

### Usuarios de Prueba (Seed Data `V2`):
| Usuario | Contraseña | Rol | Permisos |
|---|---|---|---|
| `admin` | `admin123` | `ROLE_ADMIN` | Gestión total de catálogo, usuarios y solicitudes |
| `bodeguero` | `bodega123` | `ROLE_BODEGA` | Aprobar, rechazar, entregar solicitudes y ver movimientos |
| `empleado1` | `empleado123` | `ROLE_EMPLEADO` | Crear, consultar y cancelar sus propias solicitudes |
