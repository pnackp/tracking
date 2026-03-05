package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.sensor.Sensor;
import com.tracking.tracksystems.database.sensor.SensorRepo;
import com.tracking.tracksystems.dto.InterfaceManage;
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

    public void postSensor(String name , Boolean active){
        if(sensorRepo.findBySensorName(name).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT , "Sensor Already Exists");
        }
        sensorRepo.save(new Sensor(name , active));
    }

    public void putSensor(InterfaceManage.UpdateSensor sensor){
        Sensor sensors = sensorRepo.findById(sensor.Id()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor Not Found"));
        sensors.setActive(sensor.active());
        sensorRepo.save(sensors);
    }

    public void deleteSensor(Integer id){
        sensorRepo.delete(sensorRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor Not Found")));
    }
}
