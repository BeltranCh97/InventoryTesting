# 2. Pruebas de Integración

Las pruebas de integración en este proyecto validan que el adaptador de infraestructura (`ProductRepositoryAdapter`, etc.) traduzca correctamente los datos del dominio a entidades de base de datos (`ProductEntity`) y se comunique sin fallos con Spring Data JPA.

## a) Escenarios de validación entre capas (service ↔ repository)
Se validan los siguientes escenarios:
- Guardado y persistencia real de un objeto de dominio a la base de datos a través de los adaptadores (Entity a Model y viceversa).
- Búsqueda por ID verificando que se recupere la información desde el contexto de persistencia.
- Verificación del comportamiento cuando un recurso no existe (Optional vacío).
- Eliminación correcta de entidades y la propagación a la base de datos subyacente.

## b) Uso de H2 o Mockito para aislar dependencias
- **Pruebas Unitarias (Service):** Se utiliza **Mockito** para crear _mocks_ de los puertos de repositorio (`@Mock ProductRepositoryPort`) aislando la lógica de negocio de la base de datos real.
- **Pruebas de Integración (Repository/Adapter):** Se utiliza **H2 Database** (base de datos en memoria) para levantar el contexto real de base de datos, validar sentencias SQL generadas por Hibernate y asegurar que las transacciones guarden y eliminen datos correctamente.

## c) Ejemplo de configuración de @SpringBootTest o @DataJpaTest

Ejemplo de cómo se configura `@DataJpaTest` para aislar el entorno de bases de datos:

```java
@DataJpaTest // Configura el contexto de JPA y base de datos en memoria
class ProductRepositoryAdapterTest {

    @Autowired
    private ProductJpaRepository jpaRepository; // Repositorio JPA real

    @Test
    void save_ShouldPersistAndReturnDomain() {
        // GIVEN: El adaptador y una entidad de dominio a guardar
        ProductRepositoryAdapter adapter = new ProductRepositoryAdapter(jpaRepository);
        Product product = new Product(null, "IntegrationTest", "Desc", 15.0, 2, 1L, 1L);

        // WHEN: Guardado a través de adaptador
        Product saved = adapter.save(product);

        // THEN: Validar que el objeto de dominio retornado tenga ID
        assertNotNull(saved.getId());
        assertEquals("IntegrationTest", saved.getName());
    }
}
```

## d) Capturas de resultados de ejecución y reportes JaCoCo
![Captura de Reporte Pruebas de Integración](/src/img/ResultadoPruebasIntegracion.png)

## e) Reflexiones Finales

**1. ¿Qué diferencias encontraste entre las pruebas unitarias y de integración?**
Las pruebas unitarias se enfocaron en validar de forma aislada la lógica de negocio (por ejemplo, en los casos de uso o servicios), reemplazando las dependencias con simulaciones (_mocks_). Por otro lado, las pruebas de integración verificaron el correcto funcionamiento conjunto de distintos módulos (como la comunicación entre el adaptador de persistencia y la base de datos), asegurando que se ejecuten correctamente las sentencias SQL y se manejen las transacciones de Spring Data JPA.

**2. ¿Qué capa presentó más desafíos al validar los datos?**
La capa REST (presentación) presentó bastantes desafíos, ya que se debía garantizar que las validaciones de los DTOs y el manejo de excepciones (`@ExceptionHandler`) mapearan correctamente a los códigos de estado HTTP adecuados (como devolver un `404 Not Found` en lugar de un `500 Internal Server Error`). Además, a nivel de persistencia también hubo el reto de comprobar que la conversión entre objetos de dominio y entidades JPA fuera exacta.

**3. ¿Cómo se simularon los componentes externos (mocking o base de datos en memoria)?**
En las pruebas unitarias, utilizamos la librería **Mockito** para crear _mocks_ (objetos simulados) e inyectarlos, lo que permitió probar los servicios sin ejecutar código de infraestructura. Para las pruebas de integración, dependimos de la base de datos en memoria **H2** utilizando anotaciones como `@DataJpaTest`. Esto levantó un contexto real pero ligero, ideal para ejecutar transacciones SQL de prueba sin necesidad de infraestructura pesada.

**4. ¿Qué defectos se detectaron en la capa REST?**
Durante las pruebas de la capa REST, comúnmente se detectaron defectos relacionados con las respuestas HTTP y el manejo de excepciones. Por ejemplo, flujos que lanzaban errores genéricos en lugar de respuestas controladas, o rutas que no interceptaban correctamente ciertos estados inválidos de los _payloads_ (como datos faltantes). Implementar pruebas aquí ayudó a consolidar la robustez de la API pública.

**5. ¿Qué aprendiste al ejecutar pruebas de sistema completas?**
Aprendí que, si bien las pruebas unitarias y de integración dan mucha seguridad a nivel de métodos y componentes, las pruebas de sistema completas son vitales para asegurar que Spring Boot (configuraciones, perfiles, seguridad, manejo de beans y base de datos) funcione de manera armoniosa en conjunto. Permite ver el viaje completo de una petición y simular el comportamiento real del usuario, lo que incrementa notablemente la confianza en el software desarrollado.
