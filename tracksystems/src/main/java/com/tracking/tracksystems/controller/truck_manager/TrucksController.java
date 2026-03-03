package com.tracking.tracksystems.controller.truck_manager;

import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.AssignmentService;
import com.tracking.tracksystems.service.LogsService;
import com.tracking.tracksystems.service.TruckService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/truckmanager")
public class TrucksController {

    private final TruckService truckService;
    private final AssignmentService  assignmentService;
    private final LogsService logsService;


    public TrucksController(TruckService truckService, AssignmentService assignmentService, LogsService logsService) {
        this.truckService = truckService;
        this.assignmentService = assignmentService;
        this.logsService = logsService;
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

    @GetMapping("/logs")
    public ResponseEntity<?> getLogs() {
        return ResponseEntity.ok(logsService.getAllLogs());
    }

    @GetMapping("/logs/trucks/{id}")
    public ResponseEntity<?> getLogsById(@PathVariable String id) {
        return ResponseEntity.ok(logsService.getById(id));
    }
}
