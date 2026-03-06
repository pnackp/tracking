package com.tracking.tracksystems.controller.truck_manager;

import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/truckmanager")
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping("/assignments")
    public ResponseEntity<?> getAssignments() {
        return ResponseEntity.ok(assignmentService.getAssignments());
    }

    @PostMapping("/assignments")
    public ResponseEntity<?> createAssignment(@Valid @RequestBody InterfaceManage.AssignmentCreate assignments,
                                              Authentication authentication) {
        assignmentService.postAssignment(assignments , authentication.getName());
        return ResponseEntity.ok("Create Assignment");
    }

    @GetMapping("/assignments/{truck_id}")
    public ResponseEntity<?> cancelAssignment(@PathVariable Long truck_id , Authentication authentication){
        assignmentService.cancelAssignment(truck_id , authentication.getName());
        return ResponseEntity.ok("Cancel Assignment");
    }
}
