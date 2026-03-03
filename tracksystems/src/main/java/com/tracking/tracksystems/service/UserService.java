package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.database.users.UsersRepo;
import com.tracking.tracksystems.dto.InterfaceManage;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UsersRepo usersRepo;
    private final PasswordEncoder encoder;

    public UserService(UsersRepo usersRepo, PasswordEncoder encoder) {
        this.usersRepo = usersRepo;
        this.encoder = encoder;
    }

    public List<User> getAllUsers() {
        return usersRepo.findAll();
    }

    public User getUserById(String id) {
        return usersRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User putUserById(String id , InterfaceManage.AdminUpdate payload){
        User user = usersRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setRole(payload.role());
        user.setIsActive(payload.is_active());
        return usersRepo.save(user);
    }

    public void putPassword(InterfaceManage.UserUpdatePassword request, String id){
        User user = usersRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!encoder.matches(request.oldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Old password does not match");
        }
        user.setPassword(encoder.encode(request.newPassword()));
        usersRepo.save(user);
    }

    public void putEmployee(InterfaceManage.UserUpdateRequest updatedEmployee , String id){
        User user = usersRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setEmail(updatedEmployee.email());
        user.setPhone(updatedEmployee.phone());
        usersRepo.save(user);
    }
}