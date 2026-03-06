package com.tracking.tracksystems.database.trucks;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trucks")
@Data
public class Trucks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "truck_code",
            nullable = false,
            unique = true,
            insertable = false,
            updatable = false
    ) String truckCode;

    @Column(name = "plate_number", nullable = false, unique = true, length = 50)
    private String plateNumber;

    @Column(name = "vin", unique = true, length = 50)
    private String vin;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "model", length = 50)
    private String model;

    @Column(name = "year")
    private Integer year;

    @Column(name = "truck_type", length = 50)
    private String truckType;

    @Column(name = "max_weight", precision = 10, scale = 2)
    private BigDecimal maxWeight;

    @Column(name = "status", length = 20)
    private String status = "PENDING";

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}