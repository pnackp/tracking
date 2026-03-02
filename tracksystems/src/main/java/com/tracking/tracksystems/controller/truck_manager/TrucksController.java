package com.tracking.tracksystems.controller.truck_manager;

import com.tracking.tracksystems.database.trucks.Trucks;
import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.AssignmentService;
import com.tracking.tracksystems.service.TruckService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/truckmanager")
public class TrucksController {

    private final TruckService truckService;
    private final AssignmentService  assignmentService;

    public TrucksController(TruckService truckService, AssignmentService assignmentService) {
        this.truckService = truckService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/trucks")
    public ResponseEntity<?> getTrucks() {
        return ResponseEntity.ok(truckService.getTruck());
    }

    @GetMapping("/trucks/{id}")
    public ResponseEntity<?> getTruck(@PathVariable String id) {
        return ResponseEntity.ok(truckService.getTruckById(id));
    }

    @PostMapping("/trucks")
    public ResponseEntity<?> postTrucks(@Valid @RequestBody InterfaceManage.TruckCreate payload) {
        truckService.createTruck(payload);
        return ResponseEntity.ok(new InterfaceManage.response("created successfully"));
    }

    @PutMapping("/trucks/{id}")
    public ResponseEntity<?> putTrucks(@PathVariable String id , @Valid @RequestBody InterfaceManage.TruckUpdate payload){
        truckService.updateTruck(id , payload);
        return ResponseEntity.ok(new InterfaceManage.response("updated successfully"));
    }

    @GetMapping("/assignments/truck/{truck_id}")
    public ResponseEntity<?> getAssign(@PathVariable String truck_id){
        return ResponseEntity.ok(assignmentService.getAssignById(truck_id));
    }
}
