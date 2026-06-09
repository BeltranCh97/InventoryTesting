# 5. Registro de Defectos

A lo largo del desarrollo y ejecución de pruebas (unitarias, integración, y sistema), se identificaron algunos problemas que requirieron corrección antes del despliegue en ambientes productivos.

## a) Archivo defectos_integracion.md con defectos encontrados
El listado completo y detallado de los defectos encontrados puede consultarse en el archivo anexo: [Defectos de Integración](defectos_integracion.md).

## b) Clasificación según tipo (unitaria, integración, sistema)
Los defectos se categorizan en el archivo según las distintas capas en las que fueron descubiertos:
- **Unitarias:** Problemas de lógica de negocio o validación de reglas.
- **Integración:** Problemas en el guardado de JPA, transacciones o mapeo de entidades.
- **Sistema:** Problemas de serialización de JSON, códigos de error HTTP incorrectos, o problemas en cascada (End-to-End).

## c) Estados: Abierto / En progreso / Resuelto
Cada defecto cuenta con un estado definido para su seguimiento en el ciclo de vida del desarrollo.
