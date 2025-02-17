package de.sommer.stepflowBackend.services.api;

import de.sommer.stepflowBackend.models.Penalty;

import java.util.List;
import java.util.Optional;

public interface PenaltyService {

    List<Penalty> getAllPenalties();
    Optional<Penalty> getPenaltyById(int id);
    List<Penalty> getPenaltiesByUserId(int userId);
    List<Penalty> getPenaltiesByPaidStatus(Boolean paid);
    Penalty createPenalty(Penalty penalty);
    Penalty updatePenalty(int id, Penalty penaltyDetails);
    void deletePenalty(int id);
}