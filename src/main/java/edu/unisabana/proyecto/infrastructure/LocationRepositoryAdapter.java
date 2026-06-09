package edu.unisabana.proyecto.infrastructure;

import edu.unisabana.proyecto.domain.Location;
import edu.unisabana.proyecto.domain.LocationRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LocationRepositoryAdapter implements LocationRepositoryPort {

    private final LocationJpaRepository jpaRepository;

    public LocationRepositoryAdapter(LocationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Location save(Location location) {
        LocationEntity entity = toEntity(location);
        LocationEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Location> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Location> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private LocationEntity toEntity(Location domain) {
        return new LocationEntity(
                domain.getId(),
                domain.getName(),
                domain.getAddress()
        );
    }

    private Location toDomain(LocationEntity entity) {
        return new Location(
                entity.getId(),
                entity.getName(),
                entity.getAddress()
        );
    }
}
