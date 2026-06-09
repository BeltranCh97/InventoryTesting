package edu.unisabana.proyecto.integration;

import edu.unisabana.proyecto.application.CategoryService;
import edu.unisabana.proyecto.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CategoryServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void testCreateAndGetCategory() {
        Category category = new Category(null, "CategoriaPruebaServicio", "Descripcion");
        Category saved = categoryService.createCategory(category);

        assertNotNull(saved.getId());

        Category retrieved = categoryService.getCategory(saved.getId());
        assertEquals("CategoriaPruebaServicio", retrieved.getName());
    }

    @Test
    void testGetAllCategories() {
        categoryService.createCategory(new Category(null, "Categoria1", "Descripcion1"));
        categoryService.createCategory(new Category(null, "Categoria2", "Descripcion2"));

        List<Category> allCategories = categoryService.getAllCategories();
        assertTrue(allCategories.size() >= 2);
    }

    @Test
    void testUpdateCategory() {
        Category saved = categoryService.createCategory(new Category(null, "NombreAntiguo", "DescripcionAntigua"));

        Category toUpdate = new Category(saved.getId(), "NombreNuevo", "NuevaDescripcion");
        Category updated = categoryService.updateCategory(saved.getId(), toUpdate);

        assertEquals("NombreNuevo", updated.getName());
        assertEquals("NuevaDescripcion", updated.getDescription());
    }

    @Test
    void testDeleteCategory() {
        Category saved = categoryService.createCategory(new Category(null, "EliminarCategoria", "EliminarDescripcion"));

        categoryService.deleteCategory(saved.getId());

        assertThrows(RuntimeException.class, () -> categoryService.getCategory(saved.getId()));
    }
}
