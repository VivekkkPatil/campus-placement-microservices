package com.vivek.placement.api_gateway.controller;

import com.vivek.placement.api_gateway.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    public record LoginRequest(String username, String password) {}

    public record LoginResponse(String token, String role) {}

    // Demo users only. In a real system these come from a users table with BCrypt-hashed passwords.
    private static final Map<String, String[]> USERS = Map.of(
            "tpo", new String[]{"tpo123", "TPO"},
            "student1", new String[]{"student123", "STUDENT"}
    );

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String[] user = req.username() == null ? null : USERS.get(req.username());
        if (user == null || !user[0].equals(req.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "detail", "Invalid username or password"));
        }
        return ResponseEntity.ok(new LoginResponse(jwtService.generate(req.username(), user[1]), user[1]));
    }
}
