package de.sommer.stepflowBackend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*", maxAge = 3600)
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
        public ResponseEntity<?> getUserById(@PathVariable int id, @RequestHeader("Authorization") String token, @RequestHeader("Team") int teamId) {
            Optional<User> currentUser = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
            Optional<User> user = userService.getUserById(id);
            boolean isMemberOfTeam = userService.getUserById(id).get().getMemberships().stream().anyMatch(membership -> membership.getTeam().getId() == teamId);
            if (user.isEmpty() || !isMemberOfTeam) {
                return ResponseEntity.status(403).build();
            }
            if (currentUser.isEmpty()) {
                return ResponseEntity.status(403).build();
            }
            if (currentUser.get().getId() != id && !userService.isUserAdminOfTeam(currentUser.get(), teamId)) {
                return ResponseEntity.ok(UserDTO.onlyName(user.get()));
            } else {
                return ResponseEntity.ok(new UserDTO(user.get()));
            }
        }

        @PostMapping
        public ResponseEntity<?> createUser(@RequestBody UserDTO user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return ResponseEntity.ok(new UserDTO(userService.createUser(new User(user))));
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User userDetails, @RequestHeader("Authorization") String token, @RequestHeader("Team") int teamId) {
            Optional<User> currentUser = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
            boolean isMemberOfTeam = userService.getUserById(id).get().getMemberships().stream().anyMatch(membership -> membership.getTeam().getId() == teamId);
            if (!isMemberOfTeam) {
                return ResponseEntity.status(403).build();
            }
            if (currentUser.isEmpty() || (currentUser.get().getId() != id && !userService.isUserAdminOfTeam(currentUser.get(), teamId))) {
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
        public ResponseEntity<?> deleteUser(@PathVariable int id, @RequestHeader("Authorization") String token, @RequestHeader("Team") int teamId) {
            Optional<User> user = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
            boolean isMemberOfTeam = userService.getUserById(id).get().getMemberships().stream().anyMatch(membership -> membership.getTeam().getId() == teamId);
            if (!isMemberOfTeam) {
                return ResponseEntity.status(403).build();
            }
            if (user.isEmpty() || (user.get().getId() != id && !userService.isUserAdminOfTeam(user.get(), teamId))) {
                return ResponseEntity.status(403).build();
            }
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
}