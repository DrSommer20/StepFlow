package de.sommer.stepflowBackend.services.api;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import de.sommer.stepflowBackend.models.Team;
import de.sommer.stepflowBackend.models.User;

public interface UserService extends UserDetailsService {

    List<User> getAllUsersOfTeam(int teamId);
    Optional<User> getUserById(int id);
    Optional<User> getUserByEmail(String email);
    User createUser(User user);
    boolean existsByEmail(String email);
    User updateUser(int id, User userDetails);
    void deleteUser(int id);
    boolean isUserAdminOfTeam(User user, int teamId);
    List<Team> getTeamsOfUser(int userId);
    boolean isUserMemberOfTeam(User user, int teamId);
    
}