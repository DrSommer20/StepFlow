package de.sommer.stepflowBackend.services.impl;

import de.sommer.stepflowBackend.models.Penalty;
import de.sommer.stepflowBackend.repo.PenaltyRepository;
import de.sommer.stepflowBackend.services.api.PenaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PenaltyServiceImpl implements PenaltyService {

    @Autowired
    private PenaltyRepository penaltyRepository;

    @Override
    public List<Penalty> getAllPenalties() {
        return penaltyRepository.findAll();
    }

    @Override
    public Optional<Penalty> getPenaltyById(int id) {
        return penaltyRepository.findById(id);
    }

    @Override
    public List<Penalty> getPenaltiesByUserId(int userId) {
        return penaltyRepository.findByUserId(userId);
    }

    @Override
    public List<Penalty> getPenaltiesByPaidStatus(Boolean paid) {
        return penaltyRepository.findByPaid(paid);
    }

    @Override
    public Penalty createPenalty(Penalty penalty) {
        return penaltyRepository.save(penalty);
    }

    @Override
    public Penalty updatePenalty(int id, Penalty penaltyDetails) {
        Optional<Penalty> penalty = penaltyRepository.findById(id);
        if (penalty.isPresent()) {
            Penalty existingPenalty = penalty.get();
            existingPenalty.setUser(penaltyDetails.getUser());
            existingPenalty.setAmount(penaltyDetails.getAmount());
            existingPenalty.setReason(penaltyDetails.getReason());
            existingPenalty.setPaid(penaltyDetails.getPaid());
            return penaltyRepository.save(existingPenalty);
        } else {
            throw new RuntimeException("Penalty not found with id " + id);
        }
    }

    @Override
    public void deletePenalty(int id) {
        penaltyRepository.deleteById(id);
    }
}