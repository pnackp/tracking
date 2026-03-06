package com.tracking.tracksystems.database.assignments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentsRepository extends JpaRepository<Assignments, Long> {
    Optional<Assignments> findByTruckIdAndEndTimeIsNull(Long id);
    List<Assignments> findByDriverIdIsNullAndStartTimeIsNull();
    Optional<Assignments> findByDriverIdAndEndTimeIsNull(String id);
}
