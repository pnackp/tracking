package com.tracking.tracksystems.filter;

import com.tracking.tracksystems.service.CookieService;
import com.tracking.tracksystems.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class CookieFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CookieService cookieService;

    @Value("${ACCESS_COOKIE}")
    private String accessCookieName;

    @Value("${REFRESH_COOKIE}")
    private String refreshCookieName;

    public CookieFilter(JwtService jwtService, CookieService cookieService) {
        this.jwtService = jwtService;
        this.cookieService = cookieService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        Map<String, String> cookieMap = new HashMap<>();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }

        String accessToken = cookieMap.get(accessCookieName);
        String refreshToken = cookieMap.get(refreshCookieName);

        try {
            if (accessToken != null) {
                String accessStatus = jwtService.validateToken(accessToken);
                System.out.println("passed");
                if ("VALID".equals(accessStatus)) {

                    String username = jwtService.extractUsername(accessToken);
                    String role = jwtService.extractRole(accessToken);

                    setAuthentication(username, role);
                    filterChain.doFilter(request, response);
                    System.out.println("passed1");
                    return;
                }

                if ("EXPIRED".equals(accessStatus) && refreshToken != null) {

                    String refreshStatus = jwtService.validateToken(refreshToken);
                    if ("VALID".equals(refreshStatus)) {

                        String username = jwtService.extractUsername(refreshToken);
                        String role = jwtService.extractRole(refreshToken);

                        response.addHeader(
                                HttpHeaders.SET_COOKIE,
                                cookieService.createAccessCookie(username, role).toString()
                        );

                        setAuthentication(username, role);
                        filterChain.doFilter(request, response);
                        System.out.println("passed3");
                        return;
                    }
                    System.out.println("passed2");
                }
            }

            if (accessToken == null && refreshToken != null) {
                String refreshStatus = jwtService.validateToken(refreshToken);

                if ("VALID".equals(refreshStatus)) {

                    String username = jwtService.extractUsername(refreshToken);
                    String role = jwtService.extractRole(refreshToken);

                    response.addHeader(
                            HttpHeaders.SET_COOKIE,
                            cookieService.createAccessCookie(username, role).toString()
                    );

                    setAuthentication(username, role);
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String username, String role) {

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}