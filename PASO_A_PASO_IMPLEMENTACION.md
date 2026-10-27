# Guía Paso a Paso: Proceso de Implementación StockBridge

Una descripción general, clara y secuencial de cómo se estructuró y construyó la solución completa del sistema de inventarios y solicitudes internas.

---

## 1. Diseño Conceptual y Configuración Base (El Punto de Partida)
- **Modelado de Datos (DER)**:
  - Se estructuró el modelo relacional identificando las 4 entidades clave del negocio: Usuarios (`users`), Catálogo de Productos (`products`), Solicitudes de los Empleados (`requests`) e Historial de Movimientos (`inventory_movements`).
  - Se definieron las reglas e integridad referencial: roles de usuario, stock no negativo, precios mayores a cero y el flujo de estados de una solicitud.
- **Creación del Proyecto (Spring Initializr & Dependencias)**:
  - Generación del esqueleto en Java 21 y Spring Boot 4.
  - Se incorporaron las dependencias esenciales: Web, Spring Data JPA, Spring Security, Validation, Actuator, Flyway y el driver para PostgreSQL.
  - Se complementó el archivo de dependencias con soporte para tokens JWT (JJWT), mapeo automático de objetos (MapStruct) y documentación interactiva (OpenAPI / Swagger).

---

## 2. Versionamiento y Migraciones de Base de Datos (Flyway)
En lugar de dejar la creación de tablas a la autogeneración de Hibernate (`ddl-auto`), se optó por un control de versiones profesional de la base de datos:
- **Estructura DDL**: Creación automática y versionada de tablas, índices, llaves foráneas y restricciones de integridad (`CHECK`).
- **Semilla de Datos (Seeding)**: Inserción inicial de usuarios administradores, bodegueros y empleados con contraseñas encriptadas, junto con un inventario inicial de productos para pruebas inmediatas.

---

## 3. Seguridad, Autenticación y Control de Accesos (JWT + RBAC)
*(Se inició la implementación por el componente de seguridad antes del resto de la arquitectura)*
- **Autenticación sin estado (Stateless)**:
  - Configuración de Spring Security para no almacenar sesiones en el servidor, utilizando tokens JWT (JSON Web Tokens).
  - Cifrado seguro de contraseñas mediante el algoritmo BCrypt.
  - Creación de un filtro interceptor que valida el token en cada petición HTTP entrante y extrae la identidad y el rol del usuario autenticado.
- **Control de Acceso Basado en Roles (RBAC)**:
  - Se protegieron las rutas según responsabilidades:
    - **Administrador (`ROLE_ADMIN`)**: Control total sobre usuarios, aprobaciones y configuraciones.
    - **Bodeguero (`ROLE_BODEGA`)**: Gestión de inventario físico, recepción de mercancía y despacho/entrega de solicitudes.
    - **Empleado (`ROLE_EMPLEADO`)**: Creación y seguimiento de sus propias solicitudes internas.
- **Estandarización de Errores de Seguridad**:
  - Personalización de respuestas 401 (No autenticado) y 403 (Sin permisos) en formato JSON estandarizado (ProblemDetail RFC 7807) en lugar de páginas de error HTML por defecto.

---

## 4. Núcleo del Negocio y Arquitectura Hexagonal (Dominio Puro)
- **Aislamiento del Dominio**:
  - Se definieron los modelos y entidades de negocio puros, libres de anotaciones de frameworks o de base de datos, garantizando que la lógica no dependa de tecnologías externas.
  - Definición de excepciones de negocio claras (recurso no encontrado, stock insuficiente, transición de estado inválida, etc.).
- **Puertos de Entrada y Salida**:
  - Contratos e interfaces que establecen qué operaciones ofrece el sistema hacia el exterior (casos de uso) y qué herramientas necesita el sistema hacia la infraestructura (repositorios, clientes externos, generador de tokens).

---

## 5. Lógica de Aplicación y Casos de Uso
Implementación de los servicios que orquestan las operaciones del negocio:
- **Gestión de Productos**: Validaciones de unicidad de códigos, cálculo de stock y alertas de existencias bajas.
- **Ciclo de Vida de Solicitudes**: Lógica para crear (`PENDIENTE`), aprobar o rechazar solicitudes, permitiendo a los empleados cancelar únicamente sus propias solicitudes pendientes.
- **Transacción Atómica de Entrega**: En una sola operación protegida transaccionalmente (`@Transactional`), se valida el stock, se descuentan las unidades del producto, se cambia el estado a `ENTREGADA` y se registra el movimiento de `SALIDA`. Si cualquier paso falla, se deshace todo automáticamente (rollback).
- **Autenticación y Registro**: Verificación de credenciales y emisión de tokens con información contextual del usuario.

---

## 6. Persistencia Avanzada (Spring Data JPA)
- **Mapeo Relacional Optimizado**:
  - Mapeo de entidades con relaciones `@ManyToOne` y `@OneToMany` configuradas estrictamente con carga perezosa (`FetchType.LAZY`) para evitar traer datos innecesarios a memoria.
  - **Eliminación del Problema N+1**: Uso de grafos de entidad (`@EntityGraph`) en consultas de solicitudes y movimientos para resolver las relaciones en una sola consulta SQL (`JOIN`) eficiente.
  - **Consultas Personalizadas (JPQL)**: Consultas para filtrar productos bajo stock crítico y soporte para bloqueos de concurrencia.
  - **Paginación y Ordenamiento**: Capacidad para solicitar listados paginados de productos indicando página, tamaño y ordenamiento dinámico.
  - **Mapeo entre Capas**: Uso de conversores automáticos para transformar datos entre la base de datos y los modelos de dominio.

---

## 7. Exposición de la API REST y Manejo Global de Errores
- **Controladores y Validación de Entradas**:
  - Exposición de endpoints REST organizados por recursos (`/auth`, `/api/products`, `/api/requests`, `/api/movements`, `/api/suppliers`).
  - Uso de objetos inmutables de transferencia de datos (Java Records) protegidos con validaciones de formulario (campos obligatorios, números positivos, correos válidos).
- **Manejo Centralizado de Excepciones**:
  - Un manejador global que intercepta cualquier error del sistema y responde siguiendo el estándar internacional **RFC 7807 (ProblemDetail)**, devolviendo mensajes legibles, tipo de error y código HTTP apropiado (400, 401, 403, 404, 409, 503).
- **Documentación Interactiva (Swagger / OpenAPI)**:
  - Configuración de la interfaz interactiva para explorar y probar todos los endpoints desde el navegador con soporte para autenticación Bearer Token.

---

## 8. Integración con Microservicio Externo (Resiliencia HTTP)
- **Segundo Microservicio (Simulador de Proveedor)**:
  - Creación de un microservicio independiente y liviano que simula un catálogo externo de proveedores con disponibilidad y tiempos de entrega estimados.
- **Cliente HTTP Resiliente**:
  - Conexión desde el servicio principal mediante un cliente HTTP moderno (`RestClient`).
  - Configuración de tiempos de espera máximos (timeouts de conexión y lectura) para evitar que el sistema se congele si el proveedor no responde.
  - Captura y traducción de fallos externos a errores 503 controlados.

---

## 9. Observabilidad y Métricas de Rendimiento (Actuator + Micrometer)
- **Monitoreo en Tiempo Real**:
  - Activación de Spring Boot Actuator para consultar la salud del sistema (`/actuator/health`).
  - **Indicador de Salud Personalizado**: Verificación automática de la disponibilidad del microservicio de proveedores.
  - **Métricas de Negocio con Micrometer**:
    - Contadores del ciclo de vida de solicitudes (creadas, aprobadas, rechazadas, entregadas).
    - Medidor de tiempo (Timer) para monitorear la latencia de las consultas al proveedor.
    - Indicador de alerta (Gauge) para conocer en tiempo real la cantidad de productos con stock crítico.

---

## 10. Pruebas Automatizadas (Garantía de Calidad)
- **Pruebas Unitarias**:
  - Validación de la lógica pura y reglas de negocio de los servicios (descuento de inventario, rechazo de solicitudes sin stock, entregas atómicas) aislando dependencias mediante simuladores (Mocks).
- **Pruebas de Integración Web**:
  - Simulación de peticiones HTTP a los controladores REST verificando códigos de respuesta, serialización JSON y permisos de acceso por rol con usuarios simulados.
- Todas las pruebas de la suite automatizada se ejecutan con 100% de éxito.

---

## 11. Contenerización y Despliegue con Docker Compose
- **Empaquetado Multi-Etapa**:
  - Creación de imágenes Docker optimizadas que compilan la aplicación con el JDK y ejecutan en un entorno ligero (Alpine Linux) con usuario sin privilegios por seguridad.
- **Orquestación Centralizada**:
  - Un único archivo `docker-compose.yml` que levanta la base de datos PostgreSQL (con chequeo de salud activo), el microservicio de proveedores y el servicio de inventario en una red privada compartida.
  - Archivo de variables de entorno para parametrizar puertos, credenciales y secretos de forma segura.