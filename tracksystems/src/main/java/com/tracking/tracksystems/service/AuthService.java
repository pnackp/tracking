package com.tracking.tracksystems.service;

import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.database.users.UsersRepo;
import com.tracking.tracksystems.dto.InterfaceManage;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class AuthService {

    private final UsersRepo usersRepo;
    private final PasswordEncoder encoder;
    private final EmailService  emailService;
    private final JwtService jwtService;

    public AuthService(UsersRepo usersRepo, PasswordEncoder encoder, EmailService emailService, JwtService jwtService) {
        this.usersRepo = usersRepo;
        this.encoder = encoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    public User login(InterfaceManage.Login payload) {
        User user = usersRepo.findByEmployeeIdOrEmail(payload.identifier(), payload.identifier()).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Invalid Account"));

        if (!encoder.matches(payload.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Account");
        }

        if (!user.getEmailVerify()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please verify your email");
        }

        if (!user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Waiting for Admin verify");
        }

        return user;
    }

    @Transactional
    public void register(InterfaceManage.Register payload) {
        if (usersRepo.findByEmployeeIdOrEmail(payload.employee_id(), payload.email()).isPresent() || usersRepo.findByPhone(payload.phone_number()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "already registered");
        }
        User user = new User(payload.employee_id(), payload.first_name(), payload.last_name(), encoder.encode(payload.password()), payload.phone_number(), payload.email());
        usersRepo.save(user);
        emailService.sendVerificationEmail(user.getEmail() , jwtService.generateToken(user.getEmployeeId() , "user" , 3600));
    }

    @Transactional
    public void verifyEmail(String token) {

        String status = jwtService.validateToken(token);

        if ("VALID".equals(status)) {

            String userId = jwtService.extractId(token);

            User user = usersRepo.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED, "Account not found"));

            if (Boolean.TRUE.equals(user.getEmailVerify())) {
                return;
            }

            user.setEmailVerify(true);
            usersRepo.save(user);
            return;
        }

        if ("EXPIRED".equals(status)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token expired. Please request a new verification email.");
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid token");
    }

}