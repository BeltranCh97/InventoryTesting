package edu.unisabana.proyecto.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unisabana.proyecto.domain.Category;
import edu.unisabana.proyecto.domain.Location;
import edu.unisabana.proyecto.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        private Long categoryId;
        private Long locationId;

        @BeforeEach
        void setUp() throws Exception {
                // Pre-create category and location for product constraints
                Category cat = new Category(null, "CategoriaPrueba", "DireccionPrueba");
                String catRes = mockMvc.perform(post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cat)))
                                .andReturn().getResponse().getContentAsString();
                categoryId = objectMapper.readValue(catRes, Category.class).getId();

                Location loc = new Location(null, "UbicacionPrueba", "DireccionPrueba");
                String locRes = mockMvc.perform(post("/api/locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loc)))
                                .andReturn().getResponse().getContentAsString();
                locationId = objectMapper.readValue(locRes, Location.class).getId();
        }

        @Test
        void testCreateAndGetProduct() throws Exception {
                Product product = new Product(null, "TelefonoPrueba", "DescripcionTelefono", 500.0, 20, categoryId,
                                locationId);

                String response = mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value("TelefonoPrueba"))
                                .andReturn().getResponse().getContentAsString();

                Product saved = objectMapper.readValue(response, Product.class);

                mockMvc.perform(get("/api/products/" + saved.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("TelefonoPrueba"));
        }

        @Test
        void testGetAllProducts() throws Exception {
                Product p = new Product(null, "ProductoPrueba1", "DescripcionProducto1", 10.0, 1, categoryId,
                                locationId);
                mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(p)))
                                .andExpect(status().isCreated());

                mockMvc.perform(get("/api/products"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$[0].name").exists());
        }

        @Test
        void testUpdateProduct() throws Exception {
                Product p = new Product(null, "ProductoAntiguo", "DescripcionAntigua", 10.0, 1, categoryId, locationId);
                String response = mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(p)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                Product saved = objectMapper.readValue(response, Product.class);

                Product toUpdate = new Product(saved.getId(), "ProductoAntiguo", "DescripcionAntigua", 20.0, 2,
                                categoryId, locationId);
                mockMvc.perform(put("/api/products/" + saved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(toUpdate)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("ProductoAntiguo"));
        }

        @Test
        void testDeleteProduct() throws Exception {
                Product p = new Product(null, "EliminarProducto", "DescripcionEliminar", 10.0, 1, categoryId,
                                locationId);
                String response = mockMvc.perform(post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(p)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                Product saved = objectMapper.readValue(response, Product.class);

                mockMvc.perform(delete("/api/products/" + saved.getId()))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/api/products/" + saved.getId()))
                                .andExpect(status().isNotFound());
        }
}
