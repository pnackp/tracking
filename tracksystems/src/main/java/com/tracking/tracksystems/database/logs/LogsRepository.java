package com.tracking.tracksystems.database.logs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogsRepository extends JpaRepository<Logs, Long> {

    List<Logs> findByTruckId(Long truckId);

    List<Logs> findBySensorId(Long sensorId);

}
