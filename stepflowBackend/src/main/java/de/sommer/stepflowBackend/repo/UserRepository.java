package de.sommer.stepflowBackend.repo;

import de.sommer.stepflowBackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    boolean existsByEmail(String email);
}