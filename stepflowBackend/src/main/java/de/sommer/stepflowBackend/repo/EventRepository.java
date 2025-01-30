package de.sommer.stepflowBackend.repo;

import de.sommer.stepflowBackend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByTitleContaining(String title);

    List<Event> findByLocationContaining(String location);

    List<Event> findByDate(java.util.Date date);

    List<Event> findByCreatedBy(Long userId);

    Optional<Event> findByIdAndCreatedBy(Integer eventId, Long userId);
}
