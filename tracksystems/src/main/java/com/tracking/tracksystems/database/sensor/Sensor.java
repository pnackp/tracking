package com.tracking.tracksystems.database.sensor;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor")
@Data
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sensor")
    private Integer idSensor;

    @Column(name = "sensor_name", nullable = false, length = 100)
    private String sensorName;

    @Column(name = "is_active" , nullable = false)
    private Boolean active = false;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    // Constructor
    public Sensor() {}

    public Sensor(String sensorName , Boolean active) {
        this.sensorName = sensorName;
        this.active = active;
    }
}