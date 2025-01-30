package de.sommer.stepflowBackend.services.impl;

import de.sommer.stepflowBackend.models.Carpool;
import de.sommer.stepflowBackend.repo.CarpoolRepository;
import de.sommer.stepflowBackend.services.api.CarpoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarpoolServiceImpl implements CarpoolService {

    @Autowired
    private CarpoolRepository carpoolRepository;

    @Override
    public List<Carpool> getAllCarpools() {
        return carpoolRepository.findAll();
    }

    @Override
    public Optional<Carpool> getCarpoolById(Long id) {
        return carpoolRepository.findById(id);
    }

    @Override
    public List<Carpool> getCarpoolsByEventId(Long eventId) {
        return carpoolRepository.findByEventId(eventId);
    }

    @Override
    public List<Carpool> getCarpoolsByDriverId(Long driverId) {
        return carpoolRepository.findByDriverId(driverId);
    }

    @Override
    public Carpool createCarpool(Carpool carpool) {
        return carpoolRepository.save(carpool);
    }

    @Override
    public Carpool updateCarpool(Long id, Carpool carpoolDetails) {
        Optional<Carpool> carpool = carpoolRepository.findById(id);
        if (carpool.isPresent()) {
            Carpool existingCarpool = carpool.get();
            existingCarpool.setEvent(carpoolDetails.getEvent());
            existingCarpool.setDriver(carpoolDetails.getDriver());
            existingCarpool.setSeatsAvailable(carpoolDetails.getSeatsAvailable());
            existingCarpool.setPassengers(carpoolDetails.getPassengers());
            return carpoolRepository.save(existingCarpool);
        } else {
            throw new RuntimeException("Carpool not found with id " + id);
        }
    }

    @Override
    public void deleteCarpool(Long id) {
        carpoolRepository.deleteById(id);
    }
}
