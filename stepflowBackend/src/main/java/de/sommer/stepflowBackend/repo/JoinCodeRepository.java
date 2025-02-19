package de.sommer.stepflowBackend.repo;

import de.sommer.stepflowBackend.models.JoinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JoinCodeRepository extends JpaRepository<JoinCode, Integer> {
    Optional<JoinCode> findByCode(String code);
}