package de.sommer.stepflowBackend.services.api;

import de.sommer.stepflowBackend.models.Carpool;

import java.util.List;
import java.util.Optional;

public interface CarpoolService {

    List<Carpool> getAllCarpools();
    Optional<Carpool> getCarpoolById(int id);
    List<Carpool> getCarpoolsByEventId(int eventId);
    List<Carpool> getCarpoolsByDriverId(int driverId);
    Carpool createCarpool(Carpool carpool);
    Carpool updateCarpool(int id, Carpool carpoolDetails);
    void deleteCarpool(int id);
}
