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
    public Optional<Event> getEventById(Integer id) {
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
    public List<Event> getEventsByDate(java.util.Date date) {
        return eventRepository.findByDate(date);
    }

    @Override
    public List<Event> getEventsByCreatedBy(Long userId) {
        return eventRepository.findByCreatedBy(userId);
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Integer id, Event eventDetails) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()) {
            Event existingEvent = event.get();
            existingEvent.setTitle(eventDetails.getTitle());
            existingEvent.setDescription(eventDetails.getDescription());
            existingEvent.setDate(eventDetails.getDate());
            existingEvent.setLocation(eventDetails.getLocation());
            existingEvent.setCreatedBy(eventDetails.getCreatedBy());
            return eventRepository.save(existingEvent);
        } else {
            throw new RuntimeException("Event not found with id " + id);
        }
    }

    @Override
    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }
}