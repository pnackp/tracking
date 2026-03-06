package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.locations.LocationRepository;
import com.tracking.tracksystems.database.locations.Locations;
import com.tracking.tracksystems.dto.InterfaceManage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Locations> getLocations() {
        return locationRepository.findAll();
    }

    public void postLocation(InterfaceManage.LocationCreate payload) {
        if (locationRepository.existsByNameAndProvince(payload.name(), payload.province())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Location already exists");
        }

        Locations location = new Locations();
        location.setName(payload.name());
        location.setProvince(payload.province());
        location.setLatitude(payload.latitude());
        location.setLongitude(payload.longitude());

        locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        locationRepository.findById(id).ifPresent(locationRepository::delete);
    }
}
