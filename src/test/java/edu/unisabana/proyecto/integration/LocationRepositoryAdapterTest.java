package edu.unisabana.proyecto.integration;

import edu.unisabana.proyecto.domain.Location;
import edu.unisabana.proyecto.infrastructure.LocationEntity;
import edu.unisabana.proyecto.infrastructure.LocationJpaRepository;
import edu.unisabana.proyecto.infrastructure.LocationRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LocationRepositoryAdapterTest {

    @Autowired
    private LocationJpaRepository jpaRepository;

    @Test
    void save_ShouldPersistAndReturnDomain() {
        LocationRepositoryAdapter adapter = new LocationRepositoryAdapter(jpaRepository);
        Location location = new Location(null, "Integracion", "Direccion");

        Location saved = adapter.save(location);

        assertNotNull(saved.getId());
        assertEquals("Integracion", saved.getName());

        Optional<LocationEntity> entity = jpaRepository.findById(saved.getId());
        assertTrue(entity.isPresent());
        assertEquals("Integracion", entity.get().getName());
    }

    @Test
    void findById_ShouldReturnDomain() {
        LocationEntity entity = new LocationEntity(null, "IntegracionTest", "DireccionTest");
        entity = jpaRepository.save(entity);

        LocationRepositoryAdapter adapter = new LocationRepositoryAdapter(jpaRepository);
        Optional<Location> found = adapter.findById(entity.getId());

        assertTrue(found.isPresent());
        assertEquals("IntegracionTest", found.get().getName());
    }

    @Test
    void findAll_ShouldReturnAllLocations() {
        LocationEntity entity1 = new LocationEntity(null, "Integracion1", "Direccion1");
        LocationEntity entity2 = new LocationEntity(null, "Integracion2", "Direccion2");
        jpaRepository.save(entity1);
        jpaRepository.save(entity2);

        LocationRepositoryAdapter adapter = new LocationRepositoryAdapter(jpaRepository);
        List<Location> locations = adapter.findAll();

        assertEquals(2, locations.size());
    }

    @Test
    void deleteById_ShouldRemoveLocation() {
        LocationEntity entity = new LocationEntity(null, "UbicacionEliminar", "DireccionEliminar");
        entity = jpaRepository.save(entity);

        LocationRepositoryAdapter adapter = new LocationRepositoryAdapter(jpaRepository);
        adapter.deleteById(entity.getId());

        Optional<LocationEntity> found = jpaRepository.findById(entity.getId());
        assertFalse(found.isPresent());
    }
}
