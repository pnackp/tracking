package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.assignments.TruckAssignment;
import com.tracking.tracksystems.database.assignments.TruckAssignmentRepo;
import com.tracking.tracksystems.database.trucks.Trucks;
import com.tracking.tracksystems.database.trucks.TrucksRepo;
import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.database.users.UsersRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class AssignmentService {

    private final UsersRepo usersRepo;
    private final TrucksRepo trucksRepo;
    private final TruckAssignmentRepo truckAssignmentRepo;

    public AssignmentService(UsersRepo usersRepo, TrucksRepo truck, TruckAssignmentRepo truckAssignment) {
        this.usersRepo = usersRepo;
        trucksRepo = truck;
        truckAssignmentRepo = truckAssignment;
    }

    public void newAssignment(String TruckId , String id) {
        User user = usersRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND , "User not found"));
        Trucks truck = trucksRepo.findById(TruckId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND , "Truck not found"));

        if (!truckAssignmentRepo.findByDriverAndEndDateIsNull(user).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Driver already assigned to another truck");
        }

        if (!truckAssignmentRepo.findByTruckAndEndDateIsNull(truck).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Truck already assigned to another driver");
        }

        TruckAssignment assignment = new TruckAssignment();
        assignment.setDriver(user);
        assignment.setTruck(truck);

        truckAssignmentRepo.save(assignment);
    }

    public List<Trucks> getTruckFree() {

        List<TruckAssignment> activeAssignments = truckAssignmentRepo.findAllByEndDateIsNull();

        Set<String> busyTruckIds = activeAssignments.stream().map(a -> a.getTruck().getIdTruck()).collect(Collectors.toSet());

        List<Trucks> allTrucks = trucksRepo.findAll();
        return allTrucks.stream().filter(truck -> !busyTruckIds.contains(truck.getIdTruck())).toList();
    }

    public void endAssignment(String id){
        User user = usersRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND , "User not found"));
        TruckAssignment result = truckAssignmentRepo.findByDriverAndEndDateIsNull(user).orElseThrow(()->  new ResponseStatusException(HttpStatus.NOT_FOUND,"Assign not found"));
        result.setEndDate(LocalDateTime.now());
        truckAssignmentRepo.save(result);
        return;
    }

    public List<TruckAssignment> getAllMyAssign(String id){
        User user = usersRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND , "User not found"));
        return truckAssignmentRepo.findByDriver(user);
    }

    public List<TruckAssignment> getAssignById(String truck_id){
        Trucks truck = trucksRepo.findById(truck_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND , "Truck not found"));
        return truckAssignmentRepo.findByTruck(truck);
    }
}
