package com.tracking.tracksystems.database.logs;

import com.tracking.tracksystems.database.trucks.Trucks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepo extends JpaRepository<Logs, Integer> {
    List<Logs> findByTruck(Trucks truck);

    Trucks truck(Trucks truck);
}
