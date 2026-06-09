package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.domain.Product;
import edu.unisabana.proyecto.infrastructure.ProductEntity;
import edu.unisabana.proyecto.infrastructure.ProductJpaRepository;
import edu.unisabana.proyecto.infrastructure.ProductRepositoryAdapter;
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
public class ProductRepositoryAdapterUnitTest {

    @Mock
    private ProductJpaRepository jpaRepository;

    @InjectMocks
    private ProductRepositoryAdapter adapter;

    private Product domainProduct;
    private ProductEntity entityProduct;

    @BeforeEach
    void setUp() {
        domainProduct = new Product(1L, "Computador", "PC", 1000.0, 10, 1L, 1L);
        entityProduct = new ProductEntity(1L, "Computador", "PC", 1000.0, 10, 1L, 1L);
    }

    @Test
    void save_ConvertsAndSaves() {
        when(jpaRepository.save(any(ProductEntity.class))).thenReturn(entityProduct);

        Product result = adapter.save(domainProduct);

        assertEquals(domainProduct.getId(), result.getId());
        assertEquals(domainProduct.getName(), result.getName());
        verify(jpaRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void findById_ReturnsDomainObject() {
        when(jpaRepository.findById(anyLong())).thenReturn(Optional.of(entityProduct));

        Optional<Product> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(domainProduct.getId(), result.get().getId());
        verify(jpaRepository, times(1)).findById(1L);
    }

    @Test
    void findAll_ReturnsDomainList() {
        when(jpaRepository.findAll()).thenReturn(Arrays.asList(entityProduct));

        List<Product> result = adapter.findAll();

        assertEquals(1, result.size());
        assertEquals(domainProduct.getId(), result.get(0).getId());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void deleteById_DelegatesToJpa() {
        doNothing().when(jpaRepository).deleteById(anyLong());

        adapter.deleteById(1L);

        verify(jpaRepository, times(1)).deleteById(1L);
    }
}
