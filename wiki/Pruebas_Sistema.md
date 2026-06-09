# 3. Pruebas de Sistema

Las pruebas de sistema validan el flujo completo de la aplicación, levantando el contexto web y simulando llamadas HTTP a la API REST.

## a) Pruebas end-to-end con MockMvc u otra herramienta HTTP
Usamos `MockMvc` integrado con `@SpringBootTest` y `@AutoConfigureMockMvc` para enviar peticiones a los controladores sin levantar un servidor Tomcat real, pero simulando toda la pila (Controller -> Service -> Adapter -> H2 DB).

## b) Validación de respuestas, estados HTTP y mensajes JSON
Durante las pruebas de sistema, se validan:
- **Estados HTTP**: `200 OK`, `201 Created`, `204 No Content`, `404 Not Found`, etc.
- **Validación de JSON**: Uso de `jsonPath` para asegurar que las respuestas devuelven la estructura correcta.
- **Excepciones y Mensajes**: Asegurar que cuando se falla una validación o no se encuentra un recurso, la API responde con el código adecuado.

## c) Ejemplo de prueba shouldReturnValidWhenPostRequest()

Aquí mostramos un ejemplo equivalente usando `MockMvc` para la creación de un producto:

```java
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnValidWhenPostRequest() throws Exception {
        // GIVEN
        Product productToCreate = new Product(null, "SystemTestProduct", "Desc", 100.0, 20, 1L, 1L);
        String productJson = objectMapper.writeValueAsString(productToCreate);

        // WHEN & THEN
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isCreated()) // Valida HTTP 201
                .andExpect(jsonPath("$.id").exists()) // Valida JSON
                .andExpect(jsonPath("$.name").value("SystemTestProduct"));
    }
}
```
