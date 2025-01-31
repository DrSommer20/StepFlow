package de.sommer.stepflowBackend.repo;

import de.sommer.stepflowBackend.models.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Integer> {
    List<Penalty> findByUserId(int userId);
    Optional<Penalty> findByPenaltyIdAndUserId(Integer penaltyId, int userId);
    List<Penalty> findByPaid(Boolean paid);
    List<Penalty> findByUserIdAndPaid(int userId, Boolean paid);
}
