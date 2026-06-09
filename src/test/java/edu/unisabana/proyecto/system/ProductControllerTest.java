package edu.unisabana.proyecto.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unisabana.proyecto.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void shouldCreateAndRetrieveProduct() throws Exception {
                // 1. Crea el producto
                Product productToCreate = new Product(null, "ProductoSystem", "Descripcion", 100.0, 20, 1L, 1L);
                String productJson = objectMapper.writeValueAsString(productToCreate);

                MvcResult result = mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(productJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.name").value("ProductoSystem"))
                                .andReturn();

                String responseString = result.getResponse().getContentAsString();
                Product createdProduct = objectMapper.readValue(responseString, Product.class);
                assertNotNull(createdProduct.getId());

                // 2. Obtiene el producto creado
                mockMvc.perform(get("/api/products/" + createdProduct.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("ProductoSystem"));

                // 3. Actualiza el producto
                createdProduct.setName("ProductoActualizado");
                mockMvc.perform(put("/api/products/" + createdProduct.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createdProduct)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("ProductoActualizado"));

                // 4. Elimina el producto
                mockMvc.perform(delete("/api/products/" + createdProduct.getId()))
                                .andExpect(status().isNoContent());

                // 5. Verifica la eliminacion
                mockMvc.perform(get("/api/products/" + createdProduct.getId()))
                                .andExpect(status().isNotFound());
        }

        @Test
        void shouldGetAllProducts() throws Exception {
                mockMvc.perform(get("/api/products"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
}
