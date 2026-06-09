package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.domain.Location;
import edu.unisabana.proyecto.infrastructure.LocationEntity;
import edu.unisabana.proyecto.infrastructure.LocationJpaRepository;
import edu.unisabana.proyecto.infrastructure.LocationRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationRepositoryAdapterUnitTest {

    @Mock
    private LocationJpaRepository jpaRepository;

    @InjectMocks
    private LocationRepositoryAdapter adapter;

    private Location domainLocation;
    private LocationEntity entityLocation;

    @BeforeEach
    void setUp() {
        domainLocation = new Location(1L, "SeccionA", "Gabinete 1");
        entityLocation = new LocationEntity(1L, "SeccionA", "Gabinete 1");
    }

    @Test
    void save_ConvertsAndSaves() {
        when(jpaRepository.save(any(LocationEntity.class))).thenReturn(entityLocation);

        Location result = adapter.save(domainLocation);

        assertEquals(domainLocation.getId(), result.getId());
        assertEquals(domainLocation.getName(), result.getName());
        verify(jpaRepository, times(1)).save(any(LocationEntity.class));
    }

    @Test
    void findById_ReturnsDomainObject() {
        when(jpaRepository.findById(anyLong())).thenReturn(Optional.of(entityLocation));

        Optional<Location> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(domainLocation.getId(), result.get().getId());
        verify(jpaRepository, times(1)).findById(1L);
    }

    @Test
    void findAll_ReturnsDomainList() {
        when(jpaRepository.findAll()).thenReturn(Arrays.asList(entityLocation));

        List<Location> result = adapter.findAll();

        assertEquals(1, result.size());
        assertEquals(domainLocation.getId(), result.get(0).getId());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void deleteById_DelegatesToJpa() {
        doNothing().when(jpaRepository).deleteById(anyLong());

        adapter.deleteById(1L);

        verify(jpaRepository, times(1)).deleteById(1L);
    }
}
