package de.sommer.stepflowBackend.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.sommer.stepflowBackend.models.Event;
import de.sommer.stepflowBackend.models.User;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByTitleContaining(String title);
    List<Event> findByLocationContaining(String location);
    List<Event> findByStart(Date start);
    List<Event> findByEnd(Date end);
    List<Event> findByCreatedBy(User user);
    Optional<Event> findByIdAndCreatedBy(int eventId, User user);
}
