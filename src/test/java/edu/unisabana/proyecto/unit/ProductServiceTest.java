package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.application.ProductService;
import edu.unisabana.proyecto.domain.Product;
import edu.unisabana.proyecto.domain.ProductRepositoryPort;
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

class ProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ShouldReturnSavedProduct() {
        Product product = new Product(null, "Computador", "PC", 10.0, 5, 1L, 1L);
        Product savedProduct = new Product(1L, "Computador", "PC", 10.0, 5, 1L, 1L);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.createProduct(product);

        assertNotNull(result.getId());
        assertEquals("Computador", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProduct_WhenExists_ShouldReturnProduct() {
        Product product = new Product(1L, "Computador", "PC", 10.0, 5, 1L, 1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProduct(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getProduct_WhenNotExists_ShouldThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProduct(1L));
    }

    @Test
    void getAllProducts_ShouldReturnList() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));

        List<Product> results = productService.getAllProducts();

        assertEquals(2, results.size());
    }

    @Test
    void updateProduct_ShouldUpdateAndReturn() {
        Product existing = new Product(1L, "Computador", "PC", 10.0, 5, 1L, 1L);
        Product toUpdate = new Product(1L, "Teclado", "Mecánico", 20.0, 10, 2L, 2L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(toUpdate);

        Product result = productService.updateProduct(1L, toUpdate);

        assertEquals("Teclado", result.getName());
        assertEquals(20.0, result.getPrice());
    }

    @Test
    void deleteProduct_ShouldCallDelete() {
        Product existing = new Product(1L, "Computador", "PC", 10.0, 5, 1L, 1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}
