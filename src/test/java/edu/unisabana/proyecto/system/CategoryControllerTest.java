package edu.unisabana.proyecto.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unisabana.proyecto.domain.Category;
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
class CategoryControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void shouldCreateAndRetrieveCategory() throws Exception {
                // 1. Crea una categoria
                Category categoryToCreate = new Category(null, "CategoriaPrueba", "DescripcionPrueba");
                String categoryJson = objectMapper.writeValueAsString(categoryToCreate);

                MvcResult result = mockMvc.perform(post("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(categoryJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.name").value("CategoriaPrueba"))
                                .andReturn();

                String responseString = result.getResponse().getContentAsString();
                Category createdCategory = objectMapper.readValue(responseString, Category.class);
                assertNotNull(createdCategory.getId());

                // 2. Obtiene la categoria creada
                mockMvc.perform(get("/api/categories/" + createdCategory.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("CategoriaPrueba"));

                // 3. Actualiza la categoria
                createdCategory.setName("CategoriaActualizada");
                mockMvc.perform(put("/api/categories/" + createdCategory.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createdCategory)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("CategoriaActualizada"));

                // 4. Elimina la categoria
                mockMvc.perform(delete("/api/categories/" + createdCategory.getId()))
                                .andExpect(status().isNoContent());

                // 5. Verifica la eliminacion
                mockMvc.perform(get("/api/categories/" + createdCategory.getId()))
                                .andExpect(status().isNotFound()); // Como el servicio lanza RuntimeException y el
                                                                   // controlador lo maneja
        }

        @Test
        void shouldGetAllCategories() throws Exception {
                mockMvc.perform(get("/api/categories"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
}
