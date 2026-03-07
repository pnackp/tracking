package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.assignments.Assignments;
import com.tracking.tracksystems.database.assignments.AssignmentsRepository;
import com.tracking.tracksystems.database.locations.LocationRepository;
import com.tracking.tracksystems.database.locations.Locations;
import com.tracking.tracksystems.database.trucks.TrucksRepository;
import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.database.users.UsersRepo;
import com.tracking.tracksystems.dto.InterfaceManage;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentsRepository assignmentsRepository;
    private final TrucksRepository trucksRepository;
    private final LocationRepository locationRepository;
    private final UsersRepo usersRepo;

    public AssignmentService(
            AssignmentsRepository assignmentsRepository,
            TrucksRepository trucksRepository,
            LocationRepository locationRepository,
            UsersRepo usersRepo
    ) {
        this.assignmentsRepository = assignmentsRepository;
        this.trucksRepository = trucksRepository;
        this.locationRepository = locationRepository;
        this.usersRepo = usersRepo;
    }

    public List<Assignments> getAssignments() {
        return assignmentsRepository.findAll();
    }

    @Transactional
    public void postAssignment(InterfaceManage.AssignmentCreate assignments, String name) {

        User user = usersRepo.findById(name)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        trucksRepository.findByTruckCode(assignments.truckCode())
                .ifPresentOrElse(truck -> {

                    assignmentsRepository.findByTruckIdAndEndTimeIsNull(truck.getId())
                            .ifPresentOrElse(existing -> {
                                throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Assignment already exists");
                            }, () -> {

                                Locations startLocation = locationRepository
                                        .findById(assignments.startLocation())
                                        .orElseThrow(() ->
                                                new ResponseStatusException(
                                                        HttpStatus.BAD_REQUEST,
                                                        "Start location not found"));

                                Locations endLocation = locationRepository
                                        .findById(assignments.endLocation())
                                        .orElseThrow(() ->
                                                new ResponseStatusException(
                                                        HttpStatus.BAD_REQUEST,
                                                        "End location not found"));

                                Assignments assignment = new Assignments();
                                assignment.setTruckId(truck.getId());
                                assignment.setProduct(assignments.product());
                                assignment.setStartLocation(startLocation.getId());
                                assignment.setEndLocation(endLocation.getId());
                                assignment.setCreatedBy(user.getEmployeeId());

                                assignmentsRepository.save(assignment);
                            });

                }, () -> {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Invalid truck code");
                });
    }

    @Transactional
    public void cancelAssignment(Long truckId , String id) {

        Assignments assignment = assignmentsRepository
                .findByTruckIdAndEndTimeIsNull(truckId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Active assignment not found"));

        assignment.setEndTime(LocalDateTime.now());
        if(assignment.getStartTime() == null) {
            assignment.setStartTime(LocalDateTime.now());
        }
        if(assignment.getDriverId() == null) {
            assignment.setDriverId(id);
        }
        assignmentsRepository.save(assignment);
    }

    public List<Assignments> getFreeAssignments(){
        return assignmentsRepository.findByDriverIdIsNullAndStartTimeIsNull();
    }
    //fix here tmr
    @Transactional
    public void takeAssignment(Long truckId , String id){

        usersRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Assignments assignment = assignmentsRepository
                .findByTruckIdAndEndTimeIsNull(truckId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Assignment not found"
                ));

        assignment.setDriverId(id);
        assignment.setStartTime(LocalDateTime.now());
    }

    @Transactional
    public void endAssignment(String id){

        Assignments assignment = assignmentsRepository
                .findByDriverIdAndEndTimeIsNull(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Assignment not found"));

        assignment.setEndTime(LocalDateTime.now());
    }
}