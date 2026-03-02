package com.tracking.tracksystems.database.assignments;

import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.database.trucks.Trucks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TruckAssignmentRepo extends JpaRepository<TruckAssignment, Long> {
    List<?> findByDriver(User driver);
    List<TruckAssignment> findByTruck(Trucks truck);
    List<TruckAssignment> findByDriverAndEndDateIsNull(User driver);

    List<TruckAssignment> findByTruckAndEndDateIsNull(Trucks truck);
}
