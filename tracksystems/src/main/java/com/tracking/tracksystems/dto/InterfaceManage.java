package com.tracking.tracksystems.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    public record TruckCreate(
            @NotBlank(message = "Missing truck_id")
            String truck_id,
            @NotBlank(message = "Missing plate_number")
            String plate_number,
            @NotBlank(message = "Missing product")
            String product,
            @NotBlank(message = "Missing fuel")
            String fuel,
            @NotBlank(message = "Missing weight_trucks")
            String weight_trucks
    ){}

    public record TruckUpdate(
            @NotBlank(message = "Missing product")
            String product,
            @NotBlank(message = "Missing fuel")
            String fuel,
            @NotBlank(message = "Missing weight_trucks")
            String weight_trucks
    ){}

    public record Sensor(
            @NotBlank(message = "Missing sensor name")
            String name,
            Boolean active
    ){}

    public record SensorUpdate(String truckId , List<SensorReading> readings){}

    public record SensorReading(String SensorName , double Value){}

    public record  UpdateSensor(Integer Id , Boolean active){}
}
