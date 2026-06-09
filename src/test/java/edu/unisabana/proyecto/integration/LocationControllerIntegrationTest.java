package edu.unisabana.proyecto.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unisabana.proyecto.domain.Location;
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
public class LocationControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void testCreateAndGetLocation() throws Exception {
                Location location = new Location(null, "AlmacenA", "Calle Principal");

                String response = mockMvc.perform(post("/api/locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(location)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value("AlmacenA"))
                                .andReturn().getResponse().getContentAsString();

                Location saved = objectMapper.readValue(response, Location.class);

                mockMvc.perform(get("/api/locations/" + saved.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("AlmacenA"));
        }

        @Test
        void testGetAllLocations() throws Exception {
                mockMvc.perform(post("/api/locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper
                                                .writeValueAsString(new Location(null, "Ubicacion1", "Direccion1"))))
                                .andExpect(status().isCreated());

                mockMvc.perform(get("/api/locations"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isArray())
                                .andExpect(jsonPath("$[0].name").exists());
        }

        @Test
        void testUpdateLocation() throws Exception {
                Location location = new Location(null, "UbicacionAntigua", "DireccionAntigua");
                String response = mockMvc.perform(post("/api/locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(location)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                Location saved = objectMapper.readValue(response, Location.class);

                Location toUpdate = new Location(saved.getId(), "NuevaUbicacion", "NuevaDireccion");
                mockMvc.perform(put("/api/locations/" + saved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(toUpdate)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("NuevaUbicacion"));
        }

        @Test
        void testDeleteLocation() throws Exception {
                Location location = new Location(null, "EliminarUbicacion", "EliminarDireccion");
                String response = mockMvc.perform(post("/api/locations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(location)))
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

                Location saved = objectMapper.readValue(response, Location.class);

                mockMvc.perform(delete("/api/locations/" + saved.getId()))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/api/locations/" + saved.getId()))
                                .andExpect(status().isNotFound());
        }
}
