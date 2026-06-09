package edu.unisabana.proyecto.domain;

import java.util.List;
import java.util.Optional;

public interface LocationRepositoryPort {
    Location save(Location location);
    Optional<Location> findById(Long id);
    List<Location> findAll();
    void deleteById(Long id);
}
