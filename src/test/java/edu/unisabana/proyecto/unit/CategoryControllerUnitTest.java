package edu.unisabana.proyecto.unit;

import edu.unisabana.proyecto.application.CategoryService;
import edu.unisabana.proyecto.delivery.rest.CategoryController;
import edu.unisabana.proyecto.domain.Category;
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
public class CategoryControllerUnitTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "CategoriaPruebaUnit", "DescripcionPrueba");
    }

    @Test
    void createCategory_ReturnsCreatedCategory() {
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        ResponseEntity<Category> response = categoryController.createCategory(category);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void getCategory_ReturnsCategory() {
        when(categoryService.getCategory(anyLong())).thenReturn(category);

        ResponseEntity<Category> response = categoryController.getCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).getCategory(1L);
    }

    @Test
    void getAllCategories_ReturnsListOfCategories() {
        List<Category> categories = Arrays.asList(category, new Category(2L, "Libros", "Material de lectura"));
        when(categoryService.getAllCategories()).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void updateCategory_ReturnsUpdatedCategory() {
        when(categoryService.updateCategory(anyLong(), any(Category.class))).thenReturn(category);

        ResponseEntity<Category> response = categoryController.updateCategory(1L, category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void deleteCategory_ReturnsNoContent() {
        doNothing().when(categoryService).deleteCategory(anyLong());

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void handleRuntimeException_ReturnsNotFound() {
        RuntimeException ex = new RuntimeException("Category not found");
        ResponseEntity<String> response = categoryController.handleRuntimeException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Category not found", response.getBody());
    }
}
