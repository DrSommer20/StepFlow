package de.sommer.stepflowBackend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sommer.stepflowBackend.dto.UserDTO;
import de.sommer.stepflowBackend.dto.UserListDTO;
import de.sommer.stepflowBackend.models.User;
import de.sommer.stepflowBackend.services.api.AuthService;
import de.sommer.stepflowBackend.services.api.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired 
    private AuthService authService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        UserListDTO userList = UserListDTO.onlyName(userService.getAllUsers());
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id, @RequestHeader("Authorization") String token) {
        Optional<User> currentUser = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (currentUser.get().getId() != id && !currentUser.get().getRole().equals("admin")) {
            return ResponseEntity.ok(UserDTO.onlyName(user.get()));
        }
        else {
            return ResponseEntity.ok(new UserDTO(user.get()));
        }
        
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(new UserDTO(userService.createUser(new User(user))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User userDetails, @RequestHeader("Authorization") String token) {
        Optional<User> user = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        if (user.isEmpty() || user.get().getId() != id && !user.get().getRole().equals("admin")) {
            return ResponseEntity.status(403).build();
        }
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id, @RequestHeader("Authorization") String token) {
        Optional<User> user = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        if (user.isEmpty() || user.get().getId() != id && !user.get().getRole().equals("admin")) {
            return ResponseEntity.status(403).build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}