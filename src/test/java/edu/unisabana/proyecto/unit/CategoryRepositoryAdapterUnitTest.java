package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.domain.Category;
import edu.unisabana.proyecto.infrastructure.CategoryEntity;
import edu.unisabana.proyecto.infrastructure.CategoryJpaRepository;
import edu.unisabana.proyecto.infrastructure.CategoryRepositoryAdapter;
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
public class CategoryRepositoryAdapterUnitTest {

    @Mock
    private CategoryJpaRepository jpaRepository;

    @InjectMocks
    private CategoryRepositoryAdapter adapter;

    private Category domainCategory;
    private CategoryEntity entityCategory;

    @BeforeEach
    void setUp() {
        domainCategory = new Category(1L, "CategoriaPruebaAdapter", "DescripcionPruebaAdapter");
        entityCategory = new CategoryEntity(1L, "CategoriaPruebaAdapter", "DescripcionPruebaAdapter");
    }

    @Test
    void save_ConvertsAndSaves() {
        when(jpaRepository.save(any(CategoryEntity.class))).thenReturn(entityCategory);

        Category result = adapter.save(domainCategory);

        assertEquals(domainCategory.getId(), result.getId());
        assertEquals(domainCategory.getName(), result.getName());
        verify(jpaRepository, times(1)).save(any(CategoryEntity.class));
    }

    @Test
    void findById_ReturnsDomainObject() {
        when(jpaRepository.findById(anyLong())).thenReturn(Optional.of(entityCategory));

        Optional<Category> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(domainCategory.getId(), result.get().getId());
        verify(jpaRepository, times(1)).findById(1L);
    }

    @Test
    void findAll_ReturnsDomainList() {
        when(jpaRepository.findAll()).thenReturn(Arrays.asList(entityCategory));

        List<Category> result = adapter.findAll();

        assertEquals(1, result.size());
        assertEquals(domainCategory.getId(), result.get(0).getId());
        verify(jpaRepository, times(1)).findAll();
    }

    @Test
    void deleteById_DelegatesToJpa() {
        doNothing().when(jpaRepository).deleteById(anyLong());

        adapter.deleteById(1L);

        verify(jpaRepository, times(1)).deleteById(1L);
    }
}
