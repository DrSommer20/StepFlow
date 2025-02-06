package de.sommer.stepflowBackend.repo;

import de.sommer.stepflowBackend.models.Carpool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarpoolRepository extends JpaRepository<Carpool, Integer> {
    List<Carpool> findByEvent_Id(int eventId);
    List<Carpool> findByDriverId(int driverId);
    Optional<Carpool> findByCarpoolIdAndDriverId(int carpoolId, int driverId); // Updated method name
    List<Carpool> findBySeatsAvailableGreaterThanEqual(int seatsAvailable);
}
