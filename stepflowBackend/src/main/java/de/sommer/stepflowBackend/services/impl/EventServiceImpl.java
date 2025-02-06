package de.sommer.stepflowBackend.services.impl;

import de.sommer.stepflowBackend.models.Event;
import de.sommer.stepflowBackend.repo.EventRepository;
import de.sommer.stepflowBackend.services.api.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> getEventById(int id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<Event> getEventsByTitle(String title) {
        return eventRepository.findByTitleContaining(title);
    }

    @Override
    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findByLocationContaining(location);
    }


    @Override
    public List<Event> getEventsByCreatedBy(int userId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(int id, Event eventDetails) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            Event existingEvent = event.get();
            existingEvent.setTitle(eventDetails.getTitle());
            existingEvent.setDescription(eventDetails.getDescription());
            existingEvent.setStart(eventDetails.getStart());
            existingEvent.setEnd(eventDetails.getEnd());
            existingEvent.setLocation(eventDetails.getLocation());
            existingEvent.setAttendees(eventDetails.getAttendees());
            existingEvent.setRecurrenceRule(eventDetails.getRecurrenceRule());
            existingEvent.setRecurrent(eventDetails.isRecurrent());
            existingEvent.setAllDay(eventDetails.isAllDay());
            return eventRepository.save(existingEvent);
        } else {
            throw new RuntimeException("Event not found with id " + id);
        }
    }

    @Override
    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }
}