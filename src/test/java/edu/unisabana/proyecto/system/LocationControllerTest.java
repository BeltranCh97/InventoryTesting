package edu.unisabana.proyecto.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unisabana.proyecto.domain.Location;
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
class LocationControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void shouldCreateAndRetrieveLocation() throws Exception {
                // 1. Crea la ubicacion
                Location locationToCreate = new Location(null, "UbicacionPrueba", "DireccionPrueba");
                String locationJson = objectMapper.writeValueAsString(locationToCreate);

                MvcResult result = mockMvc.perform(post("/api/locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(locationJson))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.name").value("UbicacionPrueba"))
                                .andReturn();

                String responseString = result.getResponse().getContentAsString();
                Location createdLocation = objectMapper.readValue(responseString, Location.class);
                assertNotNull(createdLocation.getId());

                // 2. Obtiene la ubicacion creada
                mockMvc.perform(get("/api/locations/" + createdLocation.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("UbicacionPrueba"));

                // 3. Actualiza la ubicacion
                createdLocation.setName("UbicacionActualizada");
                mockMvc.perform(put("/api/locations/" + createdLocation.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(createdLocation)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("UbicacionActualizada"));

                // 4. Elimina la ubicacion
                mockMvc.perform(delete("/api/locations/" + createdLocation.getId()))
                                .andExpect(status().isNoContent());

                // 5. Verifica la eliminacion
                mockMvc.perform(get("/api/locations/" + createdLocation.getId()))
                                .andExpect(status().isNotFound());
        }

        @Test
        void shouldGetAllLocations() throws Exception {
                mockMvc.perform(get("/api/locations"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
}
