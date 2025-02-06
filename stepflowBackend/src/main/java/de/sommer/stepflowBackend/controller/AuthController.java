package de.sommer.stepflowBackend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sommer.stepflowBackend.dto.LoginRequest;
import de.sommer.stepflowBackend.dto.MessageToken;
import de.sommer.stepflowBackend.dto.RegisterRequest;
import de.sommer.stepflowBackend.models.User;
import de.sommer.stepflowBackend.services.api.AuthService;
import de.sommer.stepflowBackend.services.api.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getEmail());
        Optional<User> userOptional = userService.getUserByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Manually set authentication
                String token = authService.generateToken(user);


                return new ResponseEntity<>(new MessageToken(token), HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest().body("Invalid password");
            }
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setFirstName(registerRequest.getFirstName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("user");
        userService.createUser(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        if (authService.validateToken(token.replace("Bearer ", ""))) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.badRequest().body("Token is invalid");
        }
    }
    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) {
        String tokenWithoutBearer = token.replace("Bearer ", "");
        if (authService.validateToken(tokenWithoutBearer)) {
            String newToken = authService.generateNewToken(tokenWithoutBearer);
            return ResponseEntity.ok(new MessageToken(newToken));
        } else {
            return ResponseEntity.badRequest().body("Token is invalid");
        }
    }
}