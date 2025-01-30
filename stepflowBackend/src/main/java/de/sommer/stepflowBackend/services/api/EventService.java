package de.sommer.stepflowBackend.services.api;

import de.sommer.stepflowBackend.models.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllEvents();

    Optional<Event> getEventById(Integer id);

    List<Event> getEventsByTitle(String title);

    List<Event> getEventsByLocation(String location);

    List<Event> getEventsByDate(java.util.Date date);

    List<Event> getEventsByCreatedBy(Long userId);

    Event createEvent(Event event);

    Event updateEvent(Integer id, Event eventDetails);

    void deleteEvent(Integer id);
}
