package com.tracking.tracksystems.database.maintenance;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance")
@Data
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "truck_id", nullable = false)
    private Long truckId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "maintenance_date", updatable = false)
    private LocalDateTime maintenanceDate;

    @PrePersist
    protected void onCreate() {
        this.maintenanceDate = LocalDateTime.now();
    }
}