package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.domain.Category;
import edu.unisabana.proyecto.domain.Location;
import edu.unisabana.proyecto.domain.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelsTest {

    @Test
    void testProductModel() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Producto1");
        product1.setDescription("Desc1");
        product1.setPrice(10.0);
        product1.setStock(5);
        product1.setCategoryId(2L);
        product1.setLocationId(3L);

        Product product2 = new Product(1L, "Producto1", "Desc1", 10.0, 5, 2L, 3L);
        Product product3 = new Product(2L, "Producto2", "Desc2", 20.0, 10, 4L, 5L);

        assertEquals(1L, product1.getId());
        assertEquals("Producto1", product1.getName());
        assertEquals("Desc1", product1.getDescription());
        assertEquals(10.0, product1.getPrice());
        assertEquals(5, product1.getStock());
        assertEquals(2L, product1.getCategoryId());
        assertEquals(3L, product1.getLocationId());

        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotNull(product1.toString());
    }

    @Test
    void testCategoryModel() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Categoria1");
        category1.setDescription("Desc1");

        Category category2 = new Category(1L, "Categoria1", "Desc1");
        Category category3 = new Category(2L, "Categoria2", "Desc2");

        assertEquals(1L, category1.getId());
        assertEquals("Categoria1", category1.getName());
        assertEquals("Desc1", category1.getDescription());

        assertEquals(category1, category2);
        assertNotEquals(category1, category3);
        assertEquals(category1.hashCode(), category2.hashCode());
        assertNotNull(category1.toString());
    }

    @Test
    void testLocationModel() {
        Location location1 = new Location();
        location1.setId(1L);
        location1.setName("Ubicacion1");
        location1.setAddress("Direccion1");

        Location location2 = new Location(1L, "Ubicacion1", "Direccion1");
        Location location3 = new Location(2L, "Ubicacion2", "Direccion2");

        assertEquals(1L, location1.getId());
        assertEquals("Ubicacion1", location1.getName());
        assertEquals("Direccion1", location1.getAddress());

        assertEquals(location1, location2);
        assertNotEquals(location1, location3);
        assertEquals(location1.hashCode(), location2.hashCode());
        assertNotNull(location1.toString());
    }
}
