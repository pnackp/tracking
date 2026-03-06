package com.tracking.tracksystems.database.trucks;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrucksRepository extends JpaRepository<Trucks, Long> {
    Optional<Trucks> findByVin(String vin);
    Optional<Trucks> findByTruckCode(String truckCode);
}
