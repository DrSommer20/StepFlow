package de.sommer.stepflowBackend.services.impl;

import de.sommer.stepflowBackend.models.Event;
import de.sommer.stepflowBackend.repo.EventRepository;
import de.sommer.stepflowBackend.services.api.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> getAllEvents(int teamId) {
        return eventRepository.findByTeamId(teamId);
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
            existingEvent.setEventAttendees(eventDetails.getEventAttendees());
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

    @Override
    public List<Event> isEventToday(int teamId) {
        List<Event> events = getAllEvents(teamId);
        LocalDate today = LocalDate.now();
        return events.stream()
            .filter(event -> {
                LocalDate startDate = event.getStart().toLocalDate();
                LocalDate endDate = event.getEnd().toLocalDate();
                return (startDate.isEqual(today) || endDate.isEqual(today) || 
                    (startDate.isBefore(today) && endDate.isAfter(today)));
            })
            .collect(Collectors.toList());
        
    }
}