package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.maintenance.Maintenance;
import com.tracking.tracksystems.database.maintenance.MaintenanceRepository;
import com.tracking.tracksystems.database.trucks.Trucks;
import com.tracking.tracksystems.database.trucks.TrucksRepository;
import com.tracking.tracksystems.database.truckstatus.TruckStatusHistory;
import com.tracking.tracksystems.database.truckstatus.TruckStatusHistoryRepository;
import com.tracking.tracksystems.dto.InterfaceManage;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TruckService {

    private final TrucksRepository trucksRepo;
    private final TruckStatusHistoryRepository truckStatusHistoryRepo;
    private final MaintenanceRepository maintenanceRepo;

    public TruckService(
            TrucksRepository trucksRepo,
            TruckStatusHistoryRepository truckStatusHistoryRepo,
            MaintenanceRepository maintenanceRepo
    ) {
        this.trucksRepo = trucksRepo;
        this.truckStatusHistoryRepo = truckStatusHistoryRepo;
        this.maintenanceRepo = maintenanceRepo;
    }


    private void updateStatus(Trucks truck, String newStatus) {
        if (!truck.getStatus().equals(newStatus)) {

            TruckStatusHistory history = new TruckStatusHistory();
            history.setTruckId(truck.getId());
            history.setStatus(newStatus);

            truckStatusHistoryRepo.save(history);

            truck.setStatus(newStatus);
        }
    }

    public List<Trucks> getTrucks() {
        return trucksRepo.findAll();
    }


    public void postTruck(InterfaceManage.TrucksCreate payload) {

        trucksRepo.findByVin(payload.vin())
                .ifPresent(truck -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Truck already exists"
                    );
                });

        Trucks entity = new Trucks();
        entity.setPlateNumber(payload.plateNumber());
        entity.setVin(payload.vin());
        entity.setYear(payload.year());
        entity.setBrand(payload.brand());
        entity.setModel(payload.model());
        entity.setStatus(payload.status());
        entity.setIsActive(payload.is_active());
        entity.setMaxWeight(payload.max_weight());
        entity.setTruckType(payload.truck_truck_type());

        trucksRepo.save(entity);
    }

    public void deleteTruck(String code) {
        Trucks truck = trucksRepo.findByTruckCode(code)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Truck not found"
                        ));

        trucksRepo.delete(truck);
    }


    public void putTruck(String code, InterfaceManage.TruckUpdate payload) {

        Trucks truck = trucksRepo.findByTruckCode(code)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Truck not found"
                        ));

        updateStatus(truck, payload.status());

        truck.setPlateNumber(payload.plateNumber());
        truck.setIsActive(payload.is_active());

        trucksRepo.save(truck);
    }

    // ---------------- STATUS LOG ----------------

    public TruckStatusHistory getLogs(Long id) {
        return truckStatusHistoryRepo
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Log not found"
                        ));
    }


    public void postMaintenance(String truckCode, InterfaceManage.Maintenance payload) {

        Trucks truck = trucksRepo.findByTruckCode(truckCode)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Truck not found"
                        ));

        Maintenance entity = new Maintenance();
        entity.setTruckId(truck.getId());
        entity.setDescription(payload.Description());
        entity.setCost(payload.cost());

        maintenanceRepo.save(entity);

        updateStatus(truck, "MAINTENANCE");

        trucksRepo.save(truck);
    }

    public List<Maintenance> getTruckMaintenance(Long id) {
        return maintenanceRepo.findByTruckId(id);
    }
}
