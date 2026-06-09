package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.infrastructure.CategoryEntity;
import edu.unisabana.proyecto.infrastructure.LocationEntity;
import edu.unisabana.proyecto.infrastructure.ProductEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityModelsTest {

    @Test
    void testProductEntityModel() {
        ProductEntity entity1 = new ProductEntity();
        entity1.setId(1L);
        entity1.setName("Producto1");
        entity1.setDescription("Desc1");
        entity1.setPrice(10.0);
        entity1.setStock(5);
        entity1.setCategoryId(2L);
        entity1.setLocationId(3L);

        ProductEntity entity2 = new ProductEntity(1L, "Producto1", "Desc1", 10.0, 5, 2L, 3L);
        ProductEntity entity3 = new ProductEntity(2L, "Producto2", "Desc2", 20.0, 10, 4L, 5L);

        assertEquals(1L, entity1.getId());
        assertEquals("Producto1", entity1.getName());
        assertEquals("Desc1", entity1.getDescription());
        assertEquals(10.0, entity1.getPrice());
        assertEquals(5, entity1.getStock());
        assertEquals(2L, entity1.getCategoryId());
        assertEquals(3L, entity1.getLocationId());

        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotNull(entity1.toString());
    }

    @Test
    void testCategoryEntityModel() {
        CategoryEntity entity1 = new CategoryEntity();
        entity1.setId(1L);
        entity1.setName("Categoria1");
        entity1.setDescription("Desc1");

        CategoryEntity entity2 = new CategoryEntity(1L, "Categoria1", "Desc1");
        CategoryEntity entity3 = new CategoryEntity(2L, "Categoria2", "Desc2");

        assertEquals(1L, entity1.getId());
        assertEquals("Categoria1", entity1.getName());
        assertEquals("Desc1", entity1.getDescription());

        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotNull(entity1.toString());
    }

    @Test
    void testLocationEntityModel() {
        LocationEntity entity1 = new LocationEntity();
        entity1.setId(1L);
        entity1.setName("Ubicacion1");
        entity1.setAddress("Direccion1");

        LocationEntity entity2 = new LocationEntity(1L, "Ubicacion1", "Direccion1");
        LocationEntity entity3 = new LocationEntity(2L, "Ubicacion2", "Direccion2");

        assertEquals(1L, entity1.getId());
        assertEquals("Ubicacion1", entity1.getName());
        assertEquals("Direccion1", entity1.getAddress());

        assertEquals(entity1, entity2);
        assertNotEquals(entity1, entity3);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotNull(entity1.toString());
    }
}
