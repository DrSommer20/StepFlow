package de.sommer.stepflowBackend.services.api;

import de.sommer.stepflowBackend.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    User createUser(User user);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);

}