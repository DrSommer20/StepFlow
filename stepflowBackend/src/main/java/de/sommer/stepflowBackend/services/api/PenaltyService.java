package de.sommer.stepflowBackend.services.api;

import de.sommer.stepflowBackend.models.Penalty;

import java.util.List;
import java.util.Optional;

public interface PenaltyService {

    List<Penalty> getAllPenalties();

    Optional<Penalty> getPenaltyById(Integer id);

    List<Penalty> getPenaltiesByUserId(Long userId);

    List<Penalty> getPenaltiesByPaidStatus(Boolean paid);

    Penalty createPenalty(Penalty penalty);

    Penalty updatePenalty(Integer id, Penalty penaltyDetails);

    void deletePenalty(Integer id);
}