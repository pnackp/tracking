package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.sensor.Sensor;
import com.tracking.tracksystems.database.sensor.SensorRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SensorService {

    private final SensorRepo  sensorRepo;

    public SensorService(SensorRepo sensorRepo) {
        this.sensorRepo = sensorRepo;
    }

    public List<Sensor> getSensor(){
        return sensorRepo.findAll();
    }

    public void postSensor(String name){
        sensorRepo.findBySensorName(name).orElseThrow(()-> new ResponseStatusException(HttpStatus.CONFLICT , "Sensor Not Found"));
        sensorRepo.save(new Sensor(name));
    }
}
