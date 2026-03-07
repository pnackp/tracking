package com.tracking.tracksystems.controller.driver;

import com.tracking.tracksystems.service.AssignmentService;
import com.tracking.tracksystems.service.LocationService;
import org.hibernate.query.assignment.Assignment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/driver")
public class DriverAssignmentController {

    private final AssignmentService assignmentService;
    private final LocationService locationService;

    public DriverAssignmentController(AssignmentService assignmentService, LocationService locationService) {
        this.assignmentService = assignmentService;
        this.locationService = locationService;
    }

    @GetMapping("/locations")
    public ResponseEntity<?> getLocations() {
        return ResponseEntity.ok(locationService.getLocations());
    }

    @GetMapping("/assignment")
    public ResponseEntity<?> getFreeAssignment(){
        return ResponseEntity.ok(assignmentService.getFreeAssignments());
    }

    @PutMapping("/assignment/{truck_id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long truck_id, Authentication authentication){
        assignmentService.takeAssignment(truck_id , authentication.getName());
        return ResponseEntity.ok("Assignment updated");
    }

    @GetMapping("/assignment/end")
    public ResponseEntity<?> endAssignment(Authentication authentication){
        assignmentService.endAssignment(authentication.getName());
        return ResponseEntity.ok("Assignment end");
    }
}
