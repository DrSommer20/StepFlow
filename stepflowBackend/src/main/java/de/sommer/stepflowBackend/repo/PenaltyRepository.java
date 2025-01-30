package de.sommer.stepflowBackend.repo;

import de.sommer.stepflowBackend.models.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalty, Integer> {

    List<Penalty> findByUserId(Long userId);

    Optional<Penalty> findByIdAndUserId(Integer penaltyId, Long userId);

    List<Penalty> findByPaid(Boolean paid);

    List<Penalty> findByUserIdAndPaid(Long userId, Boolean paid);
}
