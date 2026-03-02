package com.tracking.tracksystems.database.trucks;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "trucks")
@Data
public class Trucks {

    @Id
    @Column(name = "id_truck", length = 50)
    private String idTruck;

    @Column(name = "plate_number", nullable = false, length = 50)
    private String plateNumber;

    @Column(name = "product", nullable = false, length = 100)
    private String product;

    @Column(name = "fuel", nullable = false, length = 50)
    private String fuel;

    @Column(name = "weight_trucks", nullable = false, length = 50)
    private String weightTrucks;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Auto set createdAt ตอน insert
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Trucks(){}
    // ===== Constructor =====
    public Trucks(String truck_id ,String plate_number, String product, String fuel, String weight_trucks) {
        this.idTruck = truck_id;
        this.plateNumber = plate_number;
        this.product = product;
        this.fuel = fuel;
        this.weightTrucks = weight_trucks;
    }
}