# 6. Conclusiones y Reflexión

## a) Qué defectos se detectaron antes del despliegue
Gracias a la implementación exhaustiva de la suite de pruebas (Pirámide de Pruebas), logramos detectar defectos de forma temprana que, de haber llegado a producción, hubieran causado fallos graves. Entre los más relevantes se detectaron:
- Errores de validación en los payloads JSON (valores nulos que disparaban NullPointerExceptions en lugar de respuestas 400 Bad Request).
- Fallos de mapeo e inserción de entidades relacionadas (Categoría - Producto) a nivel de base de datos debido a configuraciones de cascada erróneas de JPA.
- Errores genéricos 500 para excepciones de negocio predecibles (como registros no encontrados) que fueron debidamente mapeados a 404 Not Found usando `@ControllerAdvice`.

## b) Qué desafíos se presentaron al probar múltiples capas
El principal desafío consistió en mantener la separación de responsabilidades que dicta la Arquitectura Hexagonal:
- Asegurarnos de que las **pruebas unitarias** (`Application`) fueran ultra rápidas y puramente sobre la lógica de negocio sin levantar ningún contexto de persistencia.
- Configurar adecuadamente `@DataJpaTest` para aislar el contexto en **pruebas de integración** (`Infrastructure`) sin levantar componentes del controlador REST, utilizando H2 como base de datos en memoria para verificar transacciones reales.
- Integrar la serialización correcta de JSON con Jackson y simular peticiones con `MockMvc` de Spring Boot para las **pruebas de sistema** (`Delivery`) sin necesidad de un entorno de red real, validando la propagación de los datos a lo largo de todas las capas.

## c) Cómo las pruebas de integración mejoran la confianza del sistema
Las pruebas de integración son fundamentales porque prueban los límites de nuestra aplicación. Mientras que las pruebas unitarias validan que las fórmulas matemáticas o validaciones en memoria sean correctas, las de integración nos aseguran de que el sistema "habla correctamente" con la base de datos real. Al usar un adaptador y aislar estas interacciones, obtenemos:
- Confianza en que las consultas (Queries), guardado (Saves) y mapeos de datos funcionarán cuando conectemos una base de datos de producción como PostgreSQL o MySQL.
- Resiliencia y estabilidad para realizar refactorizaciones de código en el futuro. Si realizamos un cambio en la estructura de una entidad, la prueba de integración fallará inmediatamente si las sentencias SQL no coinciden con las expectativas de mapeo, salvaguardando la integridad del sistema.
