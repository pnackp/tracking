package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.logs.Logs;
import com.tracking.tracksystems.database.logs.LogsRepo;
import com.tracking.tracksystems.database.sensor.SensorRepo;
import com.tracking.tracksystems.database.trucks.TrucksRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LogsService {

    private final LogsRepo logsRepo;
    private final TrucksRepo trucksRepo;

    public LogsService(LogsRepo logsRepo, TrucksRepo trucksRepo) {
        this.logsRepo = logsRepo;
        this.trucksRepo = trucksRepo;
    }

    public List<Logs> getAllLogs() {
        return logsRepo.findAll();
    }

    public List<Logs> getById(String truckId){
        return logsRepo.findByTruck(trucksRepo.findById(truckId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Truck not found")));
    }
}
