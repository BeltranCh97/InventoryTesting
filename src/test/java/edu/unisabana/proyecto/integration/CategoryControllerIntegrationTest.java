package edu.unisabana.proyecto.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unisabana.proyecto.domain.Category;
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
public class CategoryControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void testCreateAndGetCategory() throws Exception {
                Category category = new Category(null, "Tecnologia", "Productos de tecnologia");

                String response = mockMvc.perform(post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(category)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value("Tecnologia"))
                                .andReturn().getResponse().getContentAsString();

                Category saved = objectMapper.readValue(response, Category.class);

                mockMvc.perform(get("/api/categories/" + saved.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Tecnologia"));
        }

        @Test
        void testGetAllCategories() throws Exception {
                mockMvc.perform(post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new Category(null, "C1", "D1"))))
                                .andExpect(status().isCreated());

                mockMvc.perform(get("/api/categories"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$[0].name").exists());
        }

        @Test
        void testUpdateCategory() throws Exception {
                Category category = new Category(null, "Antigua", "Antigua descripcion");
                String response = mockMvc.perform(post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(category)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                Category saved = objectMapper.readValue(response, Category.class);

                Category toUpdate = new Category(saved.getId(), "Nueva", "Nueva descripcion");
                mockMvc.perform(put("/api/categories/" + saved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(toUpdate)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Nueva"));
        }

        @Test
        void testDeleteCategory() throws Exception {
                Category category = new Category(null, "Eliminar", "Eliminar descripcion");
                String response = mockMvc.perform(post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(category)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                Category saved = objectMapper.readValue(response, Category.class);

                mockMvc.perform(delete("/api/categories/" + saved.getId()))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/api/categories/" + saved.getId()))
                                .andExpect(status().isNotFound());
        }
}
