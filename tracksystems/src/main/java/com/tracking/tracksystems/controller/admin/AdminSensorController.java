package com.tracking.tracksystems.controller.admin;

import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.SensorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminSensorController {

    private final SensorService  sensorService;

    public AdminSensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/sensor")
    public ResponseEntity<?> getSensor() {
        return ResponseEntity.ok(sensorService.getSensor());
    }

    @PostMapping("/sensor")
    public ResponseEntity<?> postSensors(@Valid @RequestBody InterfaceManage.Sensor sensor){
        sensorService.postSensor(sensor.name() , sensor.active());
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Sensor");
    }

    @PutMapping("/sensor")
    public ResponseEntity<?> putSensor(@Valid @RequestBody InterfaceManage.UpdateSensor sensor){
        sensorService.putSensor(sensor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Updated Sensor");
    }

    @DeleteMapping("/sensor/{id}")
    public ResponseEntity<?> deleteSensor(@PathVariable Integer id){
        sensorService.deleteSensor(id);
        return ResponseEntity.ok("deleted Sensor");
    }
}
