package com.tracking.tracksystems.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class InterfaceManage {
    public record Login(
            @NotBlank(message = "Please specify your email OR employee_id")
            String identifier,

            @NotBlank(message = "Please specify your account password")
            @Size(min = 6, message = "password too short")
            String password
    ){}

    public record LoginResponse(
            String first_name ,
            String last_name ,
            String phone ,
            String role ,
            String email
    ){}

    public record Register(
            @NotBlank(message = "Please specify employee ID")
            @Size(min = 6, message = "not corrected format")
            String employee_id,

            @NotBlank(message = "Please specify your first name.")
            String first_name,

            @NotBlank(message = "Please specify your last name.")
            String last_name,

            @NotBlank(message = "Please specify your account password")
            @Size(min = 6, message = "password too short")
            String password,

            @NotBlank(message = "Please specify your email")
            @Email(message = "Invalid email format")
            String email,

            @NotBlank(message = "Please specify your phone number")
            @Pattern(regexp = "^0[0-9]{9}$", message = "0000000000")
            String phone_number
    ) { }

    public record response(String msg){}

    public record UserUpdateRequest(
            @NotBlank(message = "Missing email")
            @Email(message = "Invalid email format")
            String email,

            @NotBlank(message = "Missing phone_number")
            @Pattern(regexp = "^0[0-9]{9}$", message = "0000000000")
            String phone

            ) {
    }

    public record UserUpdatePassword(
            @NotBlank(message = "Missing password")
            String oldPassword,
            @NotBlank(message = "Missing password")
            String newPassword
    ){ }


    public record AdminUpdate(
            Boolean is_active ,
            String role

    ) { }

    public record Sensor(
            @NotBlank(message = "Missing sensor name")
            String name,
            Boolean active
    ){}

    public record SensorUpdate(String TruckCode , List<SensorReading> readings){}

    public record SensorReading(String SensorName , double Value){}

    public record  UpdateSensor(Integer Id , Boolean active){}

    public record TrucksCreate(

            @NotBlank(message = "Plate number is required")
            String plateNumber,

            @NotBlank(message = "VIN is required")
            String vin,

            @NotBlank(message = "Brand is required")
            String brand,

            @NotBlank(message = "Model is required")
            String model,

            @NotNull(message = "Year is required")
            Integer year,

            @NotBlank(message = "Truck type is required")
            String truck_truck_type,

            @NotNull(message = "Max weight is required")
            @PositiveOrZero(message = "Max weight must be >= 0")
            BigDecimal max_weight,

            @NotBlank(message = "Status is required")
            String status,

            @NotNull(message = "Active flag is required")
            Boolean is_active

    ) {}

    public record TruckUpdate(
            @NotBlank(message = "Plate number is required")
            String plateNumber,

            @NotBlank(message = "Status is required")
            String status,

            @NotNull(message = "Active flag is required")
            Boolean is_active
    ){ }

    public record LocationCreate(

            @NotBlank(message = "Location name is required")
            @Size(max = 50, message = "Location name must not exceed 50 characters")
            String name,

            @NotBlank(message = "Province is required")
            @Size(max = 50, message = "Province must not exceed 50 characters")
            String province,

            @NotNull(message = "Latitude is required")
            Double latitude,

            @NotNull(message = "Longitude is required")
            Double longitude

    ) {}

    public record Maintenance(
            @NotBlank(message = "Required Description")
            String Description ,
            @NotNull(message = "Cost is required")
            @PositiveOrZero(message = "Cost must be >= 0")
            BigDecimal cost
    ){}

    public record AssignmentCreate(

            @NotNull(message = "Truck Code is required")
            String truckCode,

            @NotBlank(message = "Product is required")
            @Size(max = 50, message = "Product must not exceed 50 characters")
            String product,

            @NotNull(message = "Start location is required")
            Long startLocation,

            @NotNull(message = "End location is required")
            Long endLocation
    ) {}
}
