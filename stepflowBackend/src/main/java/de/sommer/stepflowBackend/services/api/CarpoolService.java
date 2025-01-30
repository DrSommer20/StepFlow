package de.sommer.stepflowBackend.services.api;

import de.sommer.stepflowBackend.models.Carpool;

import java.util.List;
import java.util.Optional;

public interface CarpoolService {

    List<Carpool> getAllCarpools();

    Optional<Carpool> getCarpoolById(Long id);

    List<Carpool> getCarpoolsByEventId(Long eventId);

    List<Carpool> getCarpoolsByDriverId(Long driverId);

    Carpool createCarpool(Carpool carpool);

    Carpool updateCarpool(Long id, Carpool carpoolDetails);

    void deleteCarpool(Long id);
}
