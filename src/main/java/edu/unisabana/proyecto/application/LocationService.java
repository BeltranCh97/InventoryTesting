package edu.unisabana.proyecto.application;

import edu.unisabana.proyecto.domain.Location;
import edu.unisabana.proyecto.domain.LocationRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepositoryPort locationRepository;

    public LocationService(LocationRepositoryPort locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location getLocation(Long id) {
        return locationRepository.findById(id).orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location updateLocation(Long id, Location location) {
        Location existingLocation = getLocation(id);
        existingLocation.setName(location.getName());
        existingLocation.setAddress(location.getAddress());
        return locationRepository.save(existingLocation);
    }

    public void deleteLocation(Long id) {
        getLocation(id); // Validar si existe
        locationRepository.deleteById(id);
    }
}
