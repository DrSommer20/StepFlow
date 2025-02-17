package de.sommer.stepflowBackend.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.sommer.stepflowBackend.models.Team;
import de.sommer.stepflowBackend.models.User;
import de.sommer.stepflowBackend.repo.UserRepository;
import de.sommer.stepflowBackend.services.api.UserService;

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsersOfTeam(int teamId) {
        return userRepository.findAll().stream()
                .filter(user -> user.getMemberships().stream()
                        .anyMatch(membership -> membership.getTeam().getId() == teamId))
                .toList();
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(int id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setFirstName(userDetails.getFirstName());
            user.setEmail(userDetails.getEmail());
            if (!userDetails.getPassword().isEmpty()) {
                user.setPassword(userDetails.getPassword());
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @Override
    public boolean isUserAdminOfTeam(User user, int teamId) {
        return user.getMemberships().stream().anyMatch(membership -> membership.getTeam().getId() == teamId && membership.getRole().equals("admin"));
    }

    @Override
    public List<Team> getTeamsOfUser(int userId) {
        return userRepository
        .findById(userId)
        .get()
        .getMemberships()
        .stream()
        .map(membership -> membership.getTeam())
        .toList();
    }

    @Override
    public boolean isUserMemberOfTeam(User user, int teamId) {
        return user.getMemberships().stream().anyMatch(membership -> membership.getTeam().getId() == teamId);
    }
}
