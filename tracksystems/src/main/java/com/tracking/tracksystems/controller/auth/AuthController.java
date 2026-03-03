package com.tracking.tracksystems.controller.auth;

import com.tracking.tracksystems.database.users.User;
import com.tracking.tracksystems.dto.InterfaceManage;
import com.tracking.tracksystems.service.AuthService;
import com.tracking.tracksystems.service.CookieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    public AuthController(AuthService authService,
                          CookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody InterfaceManage.Login payload) {

        User user = authService.login(payload);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieService.createAccessCookie(user.getEmployeeId(), user.getRole()).toString())
                .header(HttpHeaders.SET_COOKIE, cookieService.createRefreshCookie(user.getEmployeeId(), user.getRole()).toString())
                .body(new InterfaceManage.LoginResponse(user.getFirstName(), user.getLastName(), user.getPhone(), user.getRole()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody InterfaceManage.Register payload) {
        authService.register(payload);

        return ResponseEntity.status(HttpStatus.CREATED).body(new InterfaceManage.response("registered successful"));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieService.clearAccessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, cookieService.clearRefreshCookie().toString())
                .body("Logout");
    }
}
