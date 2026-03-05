package com.tracking.tracksystems.controller.check;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/check")
public class check {
    @GetMapping("/checkcookie")
    public ResponseEntity<?> checkToken() {
        return ResponseEntity.ok("OK");
    }
}
