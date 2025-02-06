package de.sommer.stepflowBackend.services.api;

import de.sommer.stepflowBackend.models.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllEvents();

    Optional<Event> getEventById(int id);

    List<Event> getEventsByTitle(String title);

    List<Event> getEventsByLocation(String location);

    List<Event> getEventsByCreatedBy(int userId);

    Event createEvent(Event event);

    Event updateEvent(int id, Event eventDetails);

    void deleteEvent(int id);
}
