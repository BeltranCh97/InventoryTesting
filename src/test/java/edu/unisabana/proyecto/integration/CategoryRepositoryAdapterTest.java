package edu.unisabana.proyecto.integration;

import edu.unisabana.proyecto.domain.Category;
import edu.unisabana.proyecto.infrastructure.CategoryEntity;
import edu.unisabana.proyecto.infrastructure.CategoryJpaRepository;
import edu.unisabana.proyecto.infrastructure.CategoryRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryAdapterTest {

    @Autowired
    private CategoryJpaRepository jpaRepository;

    @Test
    void save_ShouldPersistAndReturnDomain() {
        CategoryRepositoryAdapter adapter = new CategoryRepositoryAdapter(jpaRepository);
        Category category = new Category(null, "IntegracionCategoria", "Descripcion");

        Category saved = adapter.save(category);

        assertNotNull(saved.getId());
        assertEquals("IntegracionCategoria", saved.getName());

        Optional<CategoryEntity> entity = jpaRepository.findById(saved.getId());
        assertTrue(entity.isPresent());
        assertEquals("IntegracionCategoria", entity.get().getName());
    }

    @Test
    void findById_ShouldReturnDomain() {
        CategoryEntity entity = new CategoryEntity(null, "CategoriaPruebaDB", "Descripcion");
        entity = jpaRepository.save(entity);

        CategoryRepositoryAdapter adapter = new CategoryRepositoryAdapter(jpaRepository);
        Optional<Category> found = adapter.findById(entity.getId());

        assertTrue(found.isPresent());
        assertEquals("CategoriaPruebaDB", found.get().getName());
    }

    @Test
    void findAll_ShouldReturnAllCategories() {
        CategoryEntity entity1 = new CategoryEntity(null, "Categoria1", "Descripcion1");
        CategoryEntity entity2 = new CategoryEntity(null, "Categoria2", "Descripcion2");
        jpaRepository.save(entity1);
        jpaRepository.save(entity2);

        CategoryRepositoryAdapter adapter = new CategoryRepositoryAdapter(jpaRepository);
        List<Category> categories = adapter.findAll();

        assertEquals(2, categories.size());
    }

    @Test
    void deleteById_ShouldRemoveCategory() {
        CategoryEntity entity = new CategoryEntity(null, "EliminarCategoria", "EliminarDescripcion");
        entity = jpaRepository.save(entity);

        CategoryRepositoryAdapter adapter = new CategoryRepositoryAdapter(jpaRepository);
        adapter.deleteById(entity.getId());

        Optional<CategoryEntity> found = jpaRepository.findById(entity.getId());
        assertFalse(found.isPresent());
    }
}
