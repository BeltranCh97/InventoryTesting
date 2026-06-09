package edu.unisabana.proyecto.integration;

import edu.unisabana.proyecto.domain.Product;
import edu.unisabana.proyecto.infrastructure.ProductEntity;
import edu.unisabana.proyecto.infrastructure.ProductJpaRepository;
import edu.unisabana.proyecto.infrastructure.ProductRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryAdapterTest {

    @Autowired
    private ProductJpaRepository jpaRepository;

    @Test
    void save_ShouldPersistAndReturnDomain() {
        ProductRepositoryAdapter adapter = new ProductRepositoryAdapter(jpaRepository);
        Product product = new Product(null, "ProductoGuardar", "Desc", 15.0, 2, 1L, 1L);

        Product saved = adapter.save(product);

        assertNotNull(saved.getId());
        assertEquals("ProductoGuardar", saved.getName());

        Optional<ProductEntity> entity = jpaRepository.findById(saved.getId());
        assertTrue(entity.isPresent());
        assertEquals("ProductoGuardar", entity.get().getName());
    }

    @Test
    void findById_ShouldReturnDomain() {
        ProductEntity entity = new ProductEntity(null, "ProductoBuscar", "Desc", 10.0, 5, 1L, 1L);
        entity = jpaRepository.save(entity);

        ProductRepositoryAdapter adapter = new ProductRepositoryAdapter(jpaRepository);
        Optional<Product> found = adapter.findById(entity.getId());

        assertTrue(found.isPresent());
        assertEquals("ProductoBuscar", found.get().getName());
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        ProductEntity entity1 = new ProductEntity(null, "Producto1", "Desc1", 10.0, 5, 1L, 1L);
        ProductEntity entity2 = new ProductEntity(null, "Producto2", "Desc2", 20.0, 10, 1L, 1L);
        jpaRepository.save(entity1);
        jpaRepository.save(entity2);

        ProductRepositoryAdapter adapter = new ProductRepositoryAdapter(jpaRepository);
        List<Product> products = adapter.findAll();

        assertEquals(2, products.size());
    }

    @Test
    void deleteById_ShouldRemoveProduct() {
        ProductEntity entity = new ProductEntity(null, "ProductoEliminar", "Desc", 10.0, 5, 1L, 1L);
        entity = jpaRepository.save(entity);

        ProductRepositoryAdapter adapter = new ProductRepositoryAdapter(jpaRepository);
        adapter.deleteById(entity.getId());

        Optional<ProductEntity> found = jpaRepository.findById(entity.getId());
        assertFalse(found.isPresent());
    }
}
