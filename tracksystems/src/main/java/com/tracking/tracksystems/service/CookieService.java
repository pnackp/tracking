package com.tracking.tracksystems.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${ACCESS_COOKIE}")
    private String accessCookieName;

    @Value("${REFRESH_COOKIE}")
    private String refreshCookieName;

    @Value("${ACCESS_COOKIE_MAX_AGE}")
    private long accessMaxAge;

    @Value("${REFRESH_COOKIE_MAX_AGE}")
    private long refreshMaxAge;

    @Value("${COOKIE_SECURE}")
    private boolean secure;

    @Value("${COOKIE_SAMESITE}")
    private String sameSite;

    @Value("${COOKIE_PATH}")
    private String cookiePath;

    @Value("${REFRESH_COOKIE_PATH}")
    private String refreshPath;

    private final JwtService jwtService;

    public CookieService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private static final long ACCESS_TOKEN_EXP = 60 * 15; // 15 นาที
    private static final long REFRESH_TOKEN_EXP = 60 * 60 * 24 * 7;

    public ResponseCookie createAccessCookie(String token , String role) {
        return ResponseCookie.from(accessCookieName, jwtService.generateToken(token , role , ACCESS_TOKEN_EXP ))
                .httpOnly(true)
                .secure(secure)
                .path(cookiePath)
                .maxAge(accessMaxAge)
                .sameSite(sameSite)
                .build();
    }

    public ResponseCookie clearAccessCookie() {
        return ResponseCookie.from(accessCookieName, "")
                .httpOnly(true)
                .secure(secure)
                .path(cookiePath)
                .maxAge(0)
                .sameSite(sameSite)
                .build();
    }

    public ResponseCookie createRefreshCookie(String token , String role) {
        return ResponseCookie.from(refreshCookieName, jwtService.generateToken(token , role , REFRESH_TOKEN_EXP ))
                .httpOnly(true)
                .secure(secure)
                .path(refreshPath)
                .maxAge(refreshMaxAge)
                .sameSite(sameSite)
                .build();
    }

    public ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from(refreshCookieName, "")
                .httpOnly(true)
                .secure(secure)
                .path(refreshPath)
                .maxAge(0)
                .sameSite(sameSite)
                .build();
    }
}