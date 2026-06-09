package edu.unisabana.proyecto.application;

import edu.unisabana.proyecto.domain.Category;
import edu.unisabana.proyecto.domain.CategoryRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepositoryPort categoryRepository;

    public CategoryService(CategoryRepositoryPort categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategory(id);
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        getCategory(id); // Validar si existe
        categoryRepository.deleteById(id);
    }
}
