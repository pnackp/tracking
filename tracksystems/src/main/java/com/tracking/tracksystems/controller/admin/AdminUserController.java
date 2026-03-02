package com.tracking.tracksystems.controller.admin;

import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/employees")
    public ResponseEntity<?> getEmployees() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("employees/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable String id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("employees/{id}")
    public ResponseEntity<?> putEmployee(@PathVariable String id , @Valid @RequestBody InterfaceManage.AdminUpdate payload) {
        return ResponseEntity.ok(userService.putUserById(id , payload));
    }
}
