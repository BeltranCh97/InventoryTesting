package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.application.LocationService;
import edu.unisabana.proyecto.domain.Location;
import edu.unisabana.proyecto.domain.LocationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LocationServiceTest {

    @Mock
    private LocationRepositoryPort locationRepository;

    @InjectMocks
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLocation_ShouldReturnSavedLocation() {
        Location location = new Location(null, "Seccion A", "Gabinete 1");
        Location savedLocation = new Location(1L, "Seccion A", "Gabinete 1");
        when(locationRepository.save(any(Location.class))).thenReturn(savedLocation);

        Location result = locationService.createLocation(location);

        assertNotNull(result.getId());
        assertEquals("Seccion A", result.getName());
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    void getLocation_WhenExists_ShouldReturnLocation() {
        Location location = new Location(1L, "Seccion A", "Gabinete 1");
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Location result = locationService.getLocation(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getLocation_WhenNotExists_ShouldThrowException() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> locationService.getLocation(1L));
    }

    @Test
    void getAllLocations_ShouldReturnList() {
        when(locationRepository.findAll()).thenReturn(Arrays.asList(new Location(1L, "Seccion A", "Gabinete 1"),
                new Location(2L, "Seccion B", "Gabinete 2")));

        List<Location> results = locationService.getAllLocations();

        assertEquals(2, results.size());
    }

    @Test
    void updateLocation_ShouldUpdateAndReturn() {
        Location existing = new Location(1L, "Seccion A", "Gabinete 1");
        Location toUpdate = new Location(1L, "Seccion B", "Gabinete 2");
        when(locationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(locationRepository.save(any(Location.class))).thenReturn(toUpdate);

        Location result = locationService.updateLocation(1L, toUpdate);

        assertEquals("Seccion B", result.getName());
    }

    @Test
    void deleteLocation_ShouldCallDelete() {
        Location existing = new Location(1L, "Seccion A", "Gabinete 1");
        when(locationRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(locationRepository).deleteById(1L);

        locationService.deleteLocation(1L);

        verify(locationRepository, times(1)).deleteById(1L);
    }
}
