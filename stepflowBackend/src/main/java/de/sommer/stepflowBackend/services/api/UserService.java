package de.sommer.stepflowBackend.services.api;

import de.sommer.stepflowBackend.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    Optional<User> getUserById(int id);

    Optional<User> getUserByEmail(String email);

    User createUser(User user);
    
    boolean existsByEmail(String email);

    User updateUser(int id, User userDetails);

    void deleteUser(int id);

}