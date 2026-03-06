package com.tracking.tracksystems.database.truckstatus;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TruckStatusHistoryRepository
        extends JpaRepository<TruckStatusHistory, Long> {
}
