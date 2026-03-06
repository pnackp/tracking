package com.tracking.tracksystems.controller.truck_manager;

import com.tracking.tracksystems.database.locations.Locations;
import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/truckmanager")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/locations")
    public ResponseEntity<?> getLocations() {
        return ResponseEntity.ok(locationService.getLocations());
    }

    @PostMapping("/location")
    public ResponseEntity<?> postLocation(@Valid @RequestBody InterfaceManage.LocationCreate location) {
        locationService.postLocation(location);
        return ResponseEntity.ok("Location created");
    }

    @DeleteMapping("/location/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return  ResponseEntity.ok("Location deleted");
    }
}
