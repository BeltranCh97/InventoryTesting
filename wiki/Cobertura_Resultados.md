# 4. Cobertura y Resultados

Para lograr una cobertura de código precisa y confiable, el proyecto se basó en el plugin de **JaCoCo**.

## a) Captura del reporte de JaCoCo por tipo de prueba (target/site/jacoco/)

![Captura de Reporte Pruebas Unitarias](/src/img/ResultadoPruebasUnitarias.png)

![Captura de Reporte Pruebas de Integración](/src/img/ResultadoPruebasIntegracion.png)

![Captura de Reporte Pruebas de Sistema](/src/img/ResultadoPruebasSistema.png)

## b) Mínimo 80% cobertura global y evidencia de ejecución
El proyecto ha logrado un **100% de cobertura de código ejecutable (instrucciones analizadas)** en las clases de servicio, controladores y adaptadores gracias a la implementación de pruebas unitarias, de integración y de sistema.
Para excluir métodos sin valor de negocio (getters, setters, equals, toString generados por Lombok), se configuró `lombok.addLombokGeneratedAnnotation = true` en el archivo `lombok.config`.

## c) Identificación de las líneas no cubiertas y justificación
Actualmente no hay líneas de lógica de negocio o de infraestructura explícita (escrita manualmente) no cubiertas. Las líneas no cubiertas por defecto eran aquellas autogeneradas por Lombok, las cuales fueron debidamente excluidas usando la configuración mencionada. Toda la lógica de control, casos de error y recuperación de la base de datos se encuentran probados en su totalidad.
