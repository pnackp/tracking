package com.tracking.tracksystems.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracking.tracksystems.database.logs.Logs;
import com.tracking.tracksystems.database.logs.LogsRepo;
import com.tracking.tracksystems.database.sensor.SensorRepo;
import com.tracking.tracksystems.database.trucks.Trucks;
import com.tracking.tracksystems.database.trucks.TrucksRepo;
import com.tracking.tracksystems.dto.InterfaceManage;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
@Slf4j
public class MqttConfig {

    @Value("${mqtt.address}")
    private String serverUri;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.topic}")
    private String topic;

    private final SensorRepo sensorRepo;
    private final TrucksRepo trucksRepo;
    private final LogsRepo logsRepo;
    private final MyWebSocketHandler myWebSocketHandler;

    private MqttClient mqttClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public MqttConfig(SensorRepo sensorRepo, TrucksRepo trucksRepo, LogsRepo logsRepo, MyWebSocketHandler myWebSocketHandler) {
        this.sensorRepo = sensorRepo;
        this.trucksRepo = trucksRepo;
        this.logsRepo = logsRepo;
        this.myWebSocketHandler = myWebSocketHandler;
    }

    @PostConstruct
    public void connect() {
        try {
            String clientId = "Tracking-" + UUID.randomUUID();
            mqttClient = new MqttClient(serverUri, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    try {
                        String payload = new String(message.getPayload());
                        InterfaceManage.SensorUpdate update = objectMapper.readValue(payload, InterfaceManage.SensorUpdate.class);

                        Optional<Trucks> trucksOpt = trucksRepo.findById(update.truckId());
                        if (trucksOpt.isEmpty()) {
                            System.out.println("trucks not found :" + update.truckId());
                            return;
                        }

                        Trucks trucks = trucksOpt.get();
                        for (InterfaceManage.SensorReading reading : update.readings()) {
                            sensorRepo.findBySensorName(reading.SensorName()).ifPresent(sensor -> {
                                Logs log = new Logs(trucks, sensor, reading.Value());
                                logsRepo.save(log);
                            });
                        }

                        String json = objectMapper.writeValueAsString(update);
                        myWebSocketHandler.broadcast(json);
                    } catch (Exception e) {
                        System.out.println("Somethings went wrong " + e.getMessage());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) { }
            });

            System.out.println("Connecting to MQTT broker");
            mqttClient.connect(options);
            mqttClient.subscribe(topic, 1);
            System.out.println("Subscribed to topic: " + topic);

        } catch (MqttException e) {
            log.error("Failed to initialize MQTT client", e);
        }
    }

    @PreDestroy
    public void stop() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            log.error("Error while disconnecting", e);
        }
    }
}
