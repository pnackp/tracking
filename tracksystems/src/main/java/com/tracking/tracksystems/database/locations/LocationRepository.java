package com.tracking.tracksystems.database.locations;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Locations, Long> {
    boolean existsByNameAndProvince(String name, String province);
}