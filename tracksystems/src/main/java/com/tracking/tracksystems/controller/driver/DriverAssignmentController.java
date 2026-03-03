package com.tracking.tracksystems.controller.driver;

import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/driver")
public class DriverAssignmentController {

    private final AssignmentService assignmentService;

    public DriverAssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/assignments/{truck_id}")
    public ResponseEntity<?> postAssign(@PathVariable String truck_id,
                                        Authentication authentication) {
        assignmentService.newAssignment(truck_id, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Assignment");
    }
    @GetMapping("/truck/free")
    public ResponseEntity<?> getFreeTrucks() {
        return ResponseEntity.ok(assignmentService.getTruckFree());
    }

    @PutMapping("/assignments/me/end")
    public ResponseEntity<?> endAssign(Authentication authentication){
        assignmentService.endAssignment(authentication.getName());
        return ResponseEntity.ok(new InterfaceManage.response("updated successfully"));
    }

    @GetMapping("/assignments/me")
    public ResponseEntity<?> getAssign(Authentication authentication){
        return ResponseEntity.ok(assignmentService.getAllMyAssign(authentication.getName()));
    }
}
