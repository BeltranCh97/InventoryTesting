package edu.unisabana.proyecto.integration;

import edu.unisabana.proyecto.application.LocationService;
import edu.unisabana.proyecto.domain.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class LocationServiceIntegrationTest {

    @Autowired
    private LocationService locationService;

    @Test
    void testCreateAndGetLocation() {
        Location location = new Location(null, "Integracion", "Direccion");
        Location saved = locationService.createLocation(location);

        assertNotNull(saved.getId());

        Location retrieved = locationService.getLocation(saved.getId());
        assertEquals("Integracion", retrieved.getName());
    }

    @Test
    void testGetAllLocations() {
        locationService.createLocation(new Location(null, "Integracion1", "Direccion1"));
        locationService.createLocation(new Location(null, "Integracion2", "Direccion2"));

        List<Location> allLocations = locationService.getAllLocations();
        assertTrue(allLocations.size() >= 2);
    }

    @Test
    void testUpdateLocation() {
        Location saved = locationService.createLocation(new Location(null, "Antigua", "DireccionAntigua"));

        Location toUpdate = new Location(saved.getId(), "Nueva", "DireccionNueva");
        Location updated = locationService.updateLocation(saved.getId(), toUpdate);

        assertEquals("Nueva", updated.getName());
        assertEquals("DireccionNueva", updated.getAddress());
    }

    @Test
    void testDeleteLocation() {
        Location saved = locationService.createLocation(new Location(null, "EliminarUbicacion", "DireccionEliminar"));

        locationService.deleteLocation(saved.getId());

        assertThrows(RuntimeException.class, () -> locationService.getLocation(saved.getId()));
    }
}
