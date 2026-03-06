package com.tracking.tracksystems.database.logs;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "truck_id", nullable = false)
    private Long truckId;

    @Column(name = "sensor_id")
    private Long sensorId;

    @Column(name = "value")
    private Double value;

    @Column(name = "created_at" , updatable = false , insertable = false )
    private LocalDateTime createdAt;
}