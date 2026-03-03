package com.tracking.tracksystems.database.logs;

import com.tracking.tracksystems.database.sensor.Sensor;
import com.tracking.tracksystems.database.trucks.Trucks;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
@Data
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Integer idLog;

    @ManyToOne
    @JoinColumn(name = "truck_id", nullable = false)
    private Trucks truck;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @Column(nullable = false)
    private Double value;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    public Logs() {}

    public Logs(Trucks truck, Sensor sensor, Double value) {
        this.truck = truck;
        this.sensor = sensor;
        this.value = value;
    }
}