package com.tracking.tracksystems.database.assignments;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Data
public class Assignments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "truck_id", nullable = false)
    private Long truckId;

    @Column(nullable = false, length = 50)
    private String product;

    @Column(name = "start_location", nullable = false)
    private Long startLocation;

    @Column(name = "end_location", nullable = false)
    private Long endLocation;

    @Column(name = "driver_id", nullable = true, length = 50)
    private String driverId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}