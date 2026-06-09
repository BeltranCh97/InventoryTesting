package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.application.LocationService;
import edu.unisabana.proyecto.delivery.rest.LocationController;
import edu.unisabana.proyecto.domain.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationControllerUnitTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private Location location;

    @BeforeEach
    void setUp() {
        location = new Location(1L, "SeccionA", "Gabinete 1");
    }

    @Test
    void createLocation_ReturnsCreatedLocation() {
        when(locationService.createLocation(any(Location.class))).thenReturn(location);

        ResponseEntity<Location> response = locationController.createLocation(location);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(location, response.getBody());
        verify(locationService, times(1)).createLocation(any(Location.class));
    }

    @Test
    void getLocation_ReturnsLocation() {
        when(locationService.getLocation(anyLong())).thenReturn(location);

        ResponseEntity<Location> response = locationController.getLocation(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(location, response.getBody());
        verify(locationService, times(1)).getLocation(1L);
    }

    @Test
    void getAllLocations_ReturnsListOfLocations() {
        List<Location> locations = Arrays.asList(location, new Location(2L, "SeccionB", "Gabinete 2"));
        when(locationService.getAllLocations()).thenReturn(locations);

        ResponseEntity<List<Location>> response = locationController.getAllLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locations, response.getBody());
        verify(locationService, times(1)).getAllLocations();
    }

    @Test
    void updateLocation_ReturnsUpdatedLocation() {
        when(locationService.updateLocation(anyLong(), any(Location.class))).thenReturn(location);

        ResponseEntity<Location> response = locationController.updateLocation(1L, location);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(location, response.getBody());
        verify(locationService, times(1)).updateLocation(eq(1L), any(Location.class));
    }

    @Test
    void deleteLocation_ReturnsNoContent() {
        doNothing().when(locationService).deleteLocation(anyLong());

        ResponseEntity<Void> response = locationController.deleteLocation(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(locationService, times(1)).deleteLocation(1L);
    }

    @Test
    void handleRuntimeException_ReturnsNotFound() {
        RuntimeException ex = new RuntimeException("Location not found");
        ResponseEntity<String> response = locationController.handleRuntimeException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Location not found", response.getBody());
    }
}
