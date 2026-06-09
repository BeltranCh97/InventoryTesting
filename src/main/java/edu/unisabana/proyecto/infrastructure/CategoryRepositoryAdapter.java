package edu.unisabana.proyecto.infrastructure;

import edu.unisabana.proyecto.domain.Category;
import edu.unisabana.proyecto.domain.CategoryRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository jpaRepository;

    public CategoryRepositoryAdapter(CategoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = toEntity(category);
        CategoryEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private CategoryEntity toEntity(Category domain) {
        return new CategoryEntity(
                domain.getId(),
                domain.getName(),
                domain.getDescription()
        );
    }

    private Category toDomain(CategoryEntity entity) {
        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
