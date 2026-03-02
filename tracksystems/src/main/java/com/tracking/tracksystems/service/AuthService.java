package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.database.users.UsersRepo;
import com.tracking.tracksystems.dto.InterfaceManage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsersRepo usersRepo;
    private final PasswordEncoder encoder;

    public AuthService(UsersRepo usersRepo, PasswordEncoder encoder) {
        this.usersRepo = usersRepo;
        this.encoder = encoder;
    }

    public User login(InterfaceManage.Login payload) {
        User user = usersRepo.findByEmployeeIdOrEmail(payload.identifier(), payload.identifier()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid Account"));

        if (!encoder.matches(payload.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Account");
        }

        if (!user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Waiting for Admin verify");
        }

        return user;
    }

    public void register(InterfaceManage.Register payload) {

        if (usersRepo.findByEmployeeIdOrEmail(payload.employee_id(), payload.email()).isPresent() || usersRepo.findByPhone(payload.phone_number()).isPresent()) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "already registered");
        }

        User user = new User(payload.employee_id(), payload.first_name(), payload.last_name(), encoder.encode(payload.password()), payload.phone_number(), payload.email());

        usersRepo.save(user);
    }
}