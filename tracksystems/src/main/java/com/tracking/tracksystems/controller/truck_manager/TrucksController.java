package com.tracking.tracksystems.controller.truck_manager;


import com.tracking.tracksystems.database.trucks.Trucks;
import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.TruckService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/truckmanager")
public class TrucksController {

    private final TruckService truckService;

    public TrucksController(TruckService truckService) {
        this.truckService = truckService;
    }

    @GetMapping("/trucks")
    public ResponseEntity<?> getTrucks(){
        return ResponseEntity.ok(truckService.getTrucks());
    }

    @PostMapping("/trucks")
    public ResponseEntity<?> addTruck(@Valid @RequestBody InterfaceManage.TrucksCreate trucks){
        truckService.postTruck(trucks);
        return ResponseEntity.ok("Created truck");
    }

    @DeleteMapping("/trucks/{code}")
    public ResponseEntity<?> deleteTruck(@PathVariable String code){
        truckService.deleteTruck(code);
        return ResponseEntity.ok("Deleted truck");
    }

    @PutMapping("/trucks/{code}")
    public ResponseEntity<?> putTruck(@PathVariable String code , @Valid @RequestBody InterfaceManage.TruckUpdate trucks){
        truckService.putTruck(code , trucks);
        return ResponseEntity.ok("Updated truck");
    }

    @GetMapping("/trucks/logs/{code}")
    public ResponseEntity<?> getTruckLogs(@PathVariable Long code){
        return ResponseEntity.ok(truckService.getLogs(code));
    }

    @PutMapping("/trucks/{code}/maintenance")
    public ResponseEntity<?> updateTruck(@PathVariable String code , @Valid @RequestBody InterfaceManage.Maintenance trucks){
        truckService.postMaintenance(code, trucks);
        return ResponseEntity.ok("Updated maintenance");
    }

    @GetMapping("/trucks/{code}/maintenance")
    public ResponseEntity<?> getTruckMaintenance(@PathVariable Long code){
        return ResponseEntity.ok(truckService.getTruckMaintenance(code));
    }
}
