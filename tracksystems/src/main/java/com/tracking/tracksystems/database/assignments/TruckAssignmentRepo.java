package com.tracking.tracksystems.database.assignments;

import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.database.trucks.Trucks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TruckAssignmentRepo extends JpaRepository<TruckAssignment, Long> {
    List<TruckAssignment> findByDriver(User driver);
    List<TruckAssignment> findByTruck(Trucks truck);
    Optional<TruckAssignment> findByDriverAndEndDateIsNull(User driver);

    Optional<TruckAssignment> findByTruckAndEndDateIsNull(Trucks truck);

    List<TruckAssignment> findAllByEndDateIsNull();

}
