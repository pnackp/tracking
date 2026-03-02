package com.tracking.tracksystems.controller.employee;

import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final UserService userService;

    public EmployeeController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping("/me/password")
    public ResponseEntity<?> putEmployeePassword(
            @Valid @RequestBody InterfaceManage.UserUpdatePassword request,
            Authentication authentication) {

        userService.putPassword(request, authentication.getName());

        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/me")
    public ResponseEntity<?> putEmployee(@Valid @RequestBody InterfaceManage.UserUpdateRequest updatedEmployee , Authentication authentication) {
        userService.putEmployee(updatedEmployee, authentication.getName());
        return ResponseEntity.ok("Employee updated successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserById(authentication.getName()));
    }
}
