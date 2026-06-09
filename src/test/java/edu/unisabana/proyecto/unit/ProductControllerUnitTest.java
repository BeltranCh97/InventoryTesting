package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.application.ProductService;
import edu.unisabana.proyecto.delivery.rest.ProductController;
import edu.unisabana.proyecto.domain.Product;
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
public class ProductControllerUnitTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Computador", "Gaming PC", 1500.0, 10, null, null);
    }

    @Test
    void createProduct_ReturnsCreatedProduct() {
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(product);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void getProduct_ReturnsProduct() {
        when(productService.getProduct(anyLong())).thenReturn(product);

        ResponseEntity<Product> response = productController.getProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).getProduct(1L);
    }

    @Test
    void getAllProducts_ReturnsListOfProducts() {
        List<Product> products = Arrays.asList(product,
                new Product(2L, "Teclado", "Mechanical Keyboard", 25.0, 50, null, null));
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void updateProduct_ReturnsUpdatedProduct() {
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(product);

        ResponseEntity<Product> response = productController.updateProduct(1L, product);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void deleteProduct_ReturnsNoContent() {
        doNothing().when(productService).deleteProduct(anyLong());

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void handleRuntimeException_ReturnsNotFound() {
        RuntimeException ex = new RuntimeException("Producto no encontrado");
        ResponseEntity<String> response = productController.handleRuntimeException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Producto no encontrado", response.getBody());
    }
}
