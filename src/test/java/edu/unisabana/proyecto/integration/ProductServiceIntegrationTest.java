package edu.unisabana.proyecto.integration;

import edu.unisabana.proyecto.application.CategoryService;
import edu.unisabana.proyecto.application.LocationService;
import edu.unisabana.proyecto.application.ProductService;
import edu.unisabana.proyecto.domain.Category;
import edu.unisabana.proyecto.domain.Location;
import edu.unisabana.proyecto.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocationService locationService;

    private Long categoryId;
    private Long locationId;

    @BeforeEach
    void setUp() {
        Category category = categoryService
                .createCategory(new Category(null, "PruebaCategoria", "DescripcionPruebaCategoria"));
        categoryId = category.getId();

        Location location = locationService
                .createLocation(new Location(null, "PruebaUbicacion", "DescripcionPruebaUbicacion"));
        locationId = location.getId();
    }

    @Test
    void testCreateAndGetProduct() {
        Product product = new Product(null, "Laptop", "PC", 1000.0, 10, categoryId, locationId);
        Product saved = productService.createProduct(product);

        assertNotNull(saved.getId());

        Product retrieved = productService.getProduct(saved.getId());
        assertEquals("Laptop", retrieved.getName());
    }

    @Test
    void testGetAllProducts() {
        productService.createProduct(
                new Product(null, "ProductoPrueba1", "DescripcionPrueba1", 10.0, 1, categoryId, locationId));
        productService.createProduct(
                new Product(null, "ProductoPrueba2", "DescripcionPrueba2", 20.0, 2, categoryId, locationId));

        List<Product> allProducts = productService.getAllProducts();
        assertTrue(allProducts.size() >= 2);
    }

    @Test
    void testUpdateProduct() {
        Product saved = productService.createProduct(
                new Product(null, "ProductoAntiguo", "DescripcionAntigua", 10.0, 1, categoryId, locationId));

        Product toUpdate = new Product(saved.getId(), "ProductoNuevo", "NuevaDescripcion", 15.0, 2, categoryId,
                locationId);
        Product updated = productService.updateProduct(saved.getId(), toUpdate);

        assertEquals("ProductoNuevo", updated.getName());
        assertEquals(15.0, updated.getPrice());
    }

    @Test
    void testDeleteProduct() {
        Product saved = productService.createProduct(new Product(null, "ProductoAEliminar",
                "DescripcionProductoAEliminar", 10.0, 1, categoryId, locationId));

        productService.deleteProduct(saved.getId());

        assertThrows(RuntimeException.class, () -> productService.getProduct(saved.getId()));
    }
}
