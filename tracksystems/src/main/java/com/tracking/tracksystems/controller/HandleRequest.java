package com.tracking.tracksystems.controller;

import com.tracking.tracksystems.database.assignments.TruckAssignment;
import com.tracking.tracksystems.database.assignments.TruckAssignmentRepo;
import com.tracking.tracksystems.database.sensor.Sensor;
import com.tracking.tracksystems.database.sensor.SensorRepo;
import com.tracking.tracksystems.database.trucks.Trucks;
import com.tracking.tracksystems.database.trucks.TrucksRepo;
import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.database.users.UsersRepo;
import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.CookieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@RestController
public class HandleRequest {
    private final UsersRepo Users;
    private final PasswordEncoder encoder;
    private final CookieService cookieService;
    private final TrucksRepo Truck;
    private final TruckAssignmentRepo TruckAssignment;
    private final SensorRepo Sensors;


    public HandleRequest(UsersRepo user, PasswordEncoder encoder, CookieService cookieService, TrucksRepo truck, TruckAssignmentRepo truckAssignment, SensorRepo sensors) {
        this.Users = user;
        this.encoder = encoder;
        this.cookieService = cookieService;
        Truck = truck;
        TruckAssignment = truckAssignment;
        Sensors = sensors;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody InterfaceManage.Login payload ) {
        Optional<User> result = Users.findByEmployeeIdOrEmail(payload.identifier() , payload.identifier());
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new InterfaceManage.response("Invalid Account"));
        }
        User user = result.get();
        if (!encoder.matches(payload.password() ,user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new InterfaceManage.response("Invalid Account"));
        }
        if (user.getIsActive() == false){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new InterfaceManage.response("Waiting for Admin verify"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookieService.createAccessCookie(user.getEmployeeId() , user.getRole()).toString())
                .header(HttpHeaders.SET_COOKIE, cookieService.createRefreshCookie(user.getEmployeeId() , user.getRole()).toString())
                .body(new InterfaceManage.LoginResponse(user.getFirstName(),user.getLastName(),user.getPhone(),user.getRole()));
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody InterfaceManage.Register payload) {
        if (Users.findByEmployeeIdOrEmail(payload.employee_id(),payload.email()).isPresent() || Users.findByPhone(payload.phone_number()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new InterfaceManage.response("already registered"));
        }
        User result = new User(payload.employee_id() , payload.first_name() , payload.last_name() , encoder.encode(payload.password()),
                payload.phone_number(),payload.email());
        Users.save(result);
        return ResponseEntity.ok(new InterfaceManage.response("registered successful"));
    }

    @GetMapping("/api/admin/employees")
    public ResponseEntity<?> getEmployees() {

        return ResponseEntity.ok(Users.findAll());
    }

    @GetMapping("/api/admin/employees/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable String id){
        return ResponseEntity.ok(Users.findById(id));
    }

    @PutMapping("/api/admin/employees/{id}")
    public ResponseEntity<?> putEmployee(@PathVariable String id , @Valid @RequestBody InterfaceManage.AdminUpdate payload) {
        Optional<User> optionalUser = Users.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        user.setRole(payload.role());
        user.setIsActive(payload.is_active());
        Users.save(user);
        return ResponseEntity.ok(Users.findById(id));
    }

    @PutMapping("/api/employees/me/password")
    public ResponseEntity<?> putEmployeePassword(
            @Valid @RequestBody InterfaceManage.UserUpdatePassword request,
            Authentication authentication) {

        String id = authentication.getName();

        Optional<User> optionalUser = Users.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = optionalUser.get();

        if (!encoder.matches(request.oldPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Old password incorrect");
        }

        existingUser.setPassword(encoder.encode(request.newPassword()));
        Users.save(existingUser);

        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/api/employees/me")
    public ResponseEntity<?> putEmployee(
            @Valid @RequestBody InterfaceManage.UserUpdateRequest updatedEmployee ,
            Authentication authentication) {
       String id = authentication.getName();

       Optional<User> optionalUser = Users.findById(id);
       if (optionalUser.isEmpty()) {
           return ResponseEntity.status(HttpStatus.CONFLICT).build();
       }

       User existingUser = optionalUser.get();

       existingUser.setEmail(updatedEmployee.email());
       existingUser.setPhone(updatedEmployee.phone());

       User savedUser = Users.save(existingUser);

       return ResponseEntity.ok(savedUser);
   }

    @GetMapping("/api/trucks")
    public ResponseEntity<?> getTrucks() {
        return ResponseEntity.ok(Truck.findAll());
    }

    @GetMapping("/api/trucks/{id}")
    public ResponseEntity<?> getTruck(@PathVariable String id) {
        return ResponseEntity.ok(Truck.findById(id));
    }

    @PostMapping("/api/trucks")
    public ResponseEntity<?> postTrucks(@Valid @RequestBody InterfaceManage.TruckCreate payload) {
        Optional<Trucks> truck = Truck.findById(payload.truck_id());
        if (truck.isPresent()) {return ResponseEntity.notFound().build();}
        Trucks newTruck = new Trucks(payload.truck_id() , payload.plate_number(), payload.product(), payload.fuel(), payload.weight_trucks());
        Truck.save(newTruck);
        return ResponseEntity.ok(new InterfaceManage.response("created successfully"));
    }

    @PutMapping("/api/trucks/{id}")
    public ResponseEntity<?> putTrucks(@PathVariable String id , @Valid @RequestBody InterfaceManage.TruckUpdate payload){
        Optional<Trucks> truck = Truck.findById(id);
        if (truck.isEmpty()) {return ResponseEntity.status(HttpStatus.CONFLICT).build();}
        Trucks newTruck = truck.get();
        newTruck.setFuel(payload.fuel());
        newTruck.setProduct(payload.product());
        newTruck.setWeightTrucks(payload.weight_trucks());
        Truck.save(newTruck);
        return ResponseEntity.ok(new InterfaceManage.response("updated successfully"));
    }

    @DeleteMapping("/api/admin/trucks/{id}")
    public ResponseEntity<?> deleteTrucks(@PathVariable String id) {
        Truck.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/driver/assignments/{truck_id}")
    public ResponseEntity<?> postAssign(@PathVariable String truck_id,
                                        Authentication authentication) {

        String id = authentication.getName();

        User user = Users.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Trucks truck = Truck.findById(truck_id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!TruckAssignment.findByDriverAndEndDateIsNull(user).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Driver already assigned");
        }

        if (!TruckAssignment.findByTruckAndEndDateIsNull(truck).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Truck already assigned");
        }

        TruckAssignment assignment = new TruckAssignment();
        assignment.setDriver(user);
        assignment.setTruck(truck);

        TruckAssignment.save(assignment);

        return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
    }

    @GetMapping("/api/admin/assignments/truck/{truck_id}")
    public ResponseEntity<?> getAssign(@PathVariable String truck_id){
        Optional<Trucks> optionalTruck = Truck.findById(truck_id);
        if  (optionalTruck.isEmpty()) {return ResponseEntity.notFound().build();}
        Trucks truck = optionalTruck.get();
        return ResponseEntity.ok(TruckAssignment.findByTruck(truck));
    }

    @PutMapping("/api/driver/assignments/{assignmentId}/end")
    public ResponseEntity<?> endAssign(@PathVariable Long assignmentId , Authentication authentication){
        String id = authentication.getName();
        Optional<TruckAssignment> optionalTruck = TruckAssignment.findById(assignmentId);
        if (optionalTruck.isEmpty()) {return ResponseEntity.notFound().build();}
        TruckAssignment truckAssignment = optionalTruck.get();
        User result = truckAssignment.getDriver();
        if (!Objects.equals(result.getEmployeeId(), id)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(truckAssignment.getEndDate() != null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        truckAssignment.setEndDate(LocalDateTime.now());
        TruckAssignment.save(truckAssignment);
        return ResponseEntity.ok(new InterfaceManage.response("updated successfully"));
    }

    @GetMapping("/api/driver/assignments/me")
    public ResponseEntity<?> getMyAssign(Authentication authentication){
        String id = authentication.getName();
        Optional<User> optionalUser = Users.findById(id);
        if (optionalUser.isEmpty()) {return ResponseEntity.ok().build();}
        return ResponseEntity.ok(TruckAssignment.findByDriver(optionalUser.get()));
    }

    @GetMapping("/api/admin/sensors")
    public ResponseEntity<?> getSensors(){
        return ResponseEntity.ok(Sensors.findAll());
    }

    @PostMapping("/api/admin/sensors")
    public ResponseEntity<?> postSensors(@Valid @RequestBody InterfaceManage.Sensor sensor){
        Sensors.save(new Sensor(sensor.name()));
        return ResponseEntity.status(HttpStatus.CREATED).body(sensor);
    }
}
