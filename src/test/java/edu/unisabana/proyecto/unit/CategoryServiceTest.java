package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.application.CategoryService;
import edu.unisabana.proyecto.domain.Category;
import edu.unisabana.proyecto.domain.CategoryRepositoryPort;
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

class CategoryServiceTest {

    @Mock
    private CategoryRepositoryPort categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_ShouldReturnSavedCategory() {
        Category category = new Category(null, "CategoriaPruebaService", "DescripcionPruebaService");
        Category savedCategory = new Category(1L, "CategoriaPruebaService", "DescripcionPruebaService");
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        Category result = categoryService.createCategory(category);

        assertNotNull(result.getId());
        assertEquals("CategoriaPruebaService", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void getCategory_WhenExists_ShouldReturnCategory() {
        Category category = new Category(1L, "CategoriaPruebaService", "DescripcionPruebaService");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategory(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCategory_WhenNotExists_ShouldThrowException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.getCategory(1L));
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(new Category(), new Category()));

        List<Category> results = categoryService.getAllCategories();

        assertEquals(2, results.size());
    }

    @Test
    void updateCategory_ShouldUpdateAndReturn() {
        Category existing = new Category(1L, "CategoriaPruebaService", "DescripcionPruebaService");
        Category toUpdate = new Category(1L, "CategoriaActualizadaService", "DescripcionActualizadaService");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(toUpdate);

        Category result = categoryService.updateCategory(1L, toUpdate);

        assertEquals("CategoriaActualizadaService", result.getName());
    }

    @Test
    void deleteCategory_ShouldCallDelete() {
        Category existing = new Category(1L, "CategoriaPruebaService", "DescripcionPruebaService");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
