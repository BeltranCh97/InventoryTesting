# Gestión de Defectos - Pruebas de Rendimiento

A continuación se listan los defectos y hallazgos encontrados durante las ejecuciones de las pruebas de rendimiento (carga y estrés) realizadas sobre el sistema de inventario.

## Defecto 1: Agotamiento del Pool de Conexiones a Base de Datos (Timeout)

- **ID del Defecto:** DEF-PERF-01
- **Fecha de Detección:** 2026-06-15
- **Estado:** Abierto
- **Componente:** Capa de acceso a datos (H2/JPA) / Endpoint `POST /api/products`
- **Escenario Detectado:** Prueba de Estrés (Stress) con 600 VUs concurrentes.
- **Descripción del Fallo:**
  Durante la prueba de estrés, una vez que la carga de usuarios virtuales supera los 300 VUs y se acerca a la meta de 600, las peticiones HTTP comienzan a responder con código de estado HTTP 500 y/o demorar más de 2000 ms. El registro del servidor Spring Boot muestra la excepción de que no hay conexiones disponibles en el pool de HikariCP para procesar la transacción JPA que inserta la entidad `Product`.
- **Evidencia:**
  Al ejecutar `k6 run` para el escenario de estrés, la métrica `http_req_failed` supera el 5% (rompiendo el SLO del < 1%).
- **Solución Propuesta:**
  1. Aumentar el tamaño máximo del pool de HikariCP en `application.properties` (ej: `spring.datasource.hikari.maximum-pool-size=50`).
  2. Reducir la duración máxima de la transacción.
  3. Si la carga sigue siendo inmanejable, encolar las peticiones de registro usando RabbitMQ o Kafka para procesamiento asíncrono.
