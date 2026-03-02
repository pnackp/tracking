package com.tracking.tracksystems.database.sensor;

import jakarta.persistence.*;
import lombok.Data;

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

    // Constructor
    public Sensor() {}

    public Sensor(String sensorName) {
        this.sensorName = sensorName;
    }
}