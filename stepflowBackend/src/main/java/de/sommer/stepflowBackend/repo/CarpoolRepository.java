package de.sommer.stepflowBackend.repo;

import de.sommer.stepflowBackend.models.Carpool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarpoolRepository extends JpaRepository<Carpool, Long> {

    List<Carpool> findByEventId(Long eventId);

    List<Carpool> findByDriverId(Long driverId);

    Optional<Carpool> findByIdAndDriverId(Long carpoolId, Long driverId);

    List<Carpool> findBySeatsAvailableGreaterThanEqual(Long seatsAvailable);
}
