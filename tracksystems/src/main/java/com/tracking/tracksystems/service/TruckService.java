package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.trucks.Trucks;
import com.tracking.tracksystems.database.trucks.TrucksRepo;
import com.tracking.tracksystems.dto.InterfaceManage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TruckService {

    public final TrucksRepo  trucksRepo;

    public TruckService(TrucksRepo trucksRepo) {
        this.trucksRepo = trucksRepo;
    }

    public List<Trucks> getTruck(){
        return trucksRepo.findAll();
    }

    public Trucks getTruckById(String id){
        return trucksRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND , "Trucks Not found"));
    }

    public void createTruck(InterfaceManage.TruckCreate payload){
        if (trucksRepo.findById(payload.truck_id()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "Truck already exists");
        }
        Trucks newTruck = new Trucks(payload.truck_id() , payload.plate_number(), payload.product(), payload.fuel(), payload.weight_trucks());
        trucksRepo.save(newTruck);
        return;
    }

    public void updateTruck(String id,InterfaceManage.TruckUpdate payload){
        Trucks truck = trucksRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND , "Trucks Not found"));
        truck.setWeightTrucks(payload.weight_trucks());
        truck.setFuel(payload.fuel());
        truck.setProduct(payload.product());
        trucksRepo.save(truck);
        return;
    }
}
