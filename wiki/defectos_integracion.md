# Defectos de Integración y Pruebas

A continuación se presenta el registro de defectos detectados durante las fases de pruebas unitarias, de integración y de sistema.

| ID | Capa / Tipo | Descripción del Defecto | Estado | Solución Aplicada |
|---|---|---|---|---|
| DEF-001 | Unitaria | `NullPointerException` en `ProductService` al intentar guardar un producto con precio nulo. | **Resuelto** | Se añadieron validaciones en la capa de dominio y controlador para asegurar que los campos obligatorios tengan valores por defecto o rechacen la petición. |
| DEF-002 | Integración | Las entidades relacionadas no se guardaban debido a la falta de `CascadeType.ALL` en JPA, lo que provocaba una excepción `ConstraintViolationException` al insertar un producto con una nueva categoría. | **Resuelto** | Se modificó la entidad JPA para incluir la cascada y se aseguró de guardar primero la categoría antes que el producto. |
| DEF-003 | Sistema (E2E) | Retorno de un código de estado `500 Internal Server Error` en lugar de `404 Not Found` al buscar un ID inexistente mediante `GET /api/products/{id}`. | **Resuelto** | Se implementó un `@RestControllerAdvice` con `@ExceptionHandler` para `ResourceNotFoundException` devolviendo una respuesta limpia con código 404. |
| DEF-004 | Integración | Fallo al limpiar el contexto de persistencia usando base de datos en memoria entre pruebas individuales. | **Resuelto** | Se aseguró el uso correcto de la anotación `@DataJpaTest`, la cual aplica transaccionalidad automática (`@Transactional`) y hace rollback al final de cada test. |
