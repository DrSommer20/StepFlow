package de.sommer.stepflowBackend.controller;

import java.text.ParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sommer.stepflowBackend.dto.EventDTO;
import de.sommer.stepflowBackend.dto.EventListDTO;
import de.sommer.stepflowBackend.models.Event;
import de.sommer.stepflowBackend.models.User;
import de.sommer.stepflowBackend.services.api.AuthService;
import de.sommer.stepflowBackend.services.api.EventService;
import de.sommer.stepflowBackend.services.api.UserService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestHeader("Authorization") String token, @RequestBody EventDTO event) {
        Optional<User> user = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        if(event == null || event.getTitle() == null || event.getDate() == null || user.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid event. Information missing, please provide title, date and User");
        }
        Event newEvent;
        try{
            newEvent = new Event(event, user.get());
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd");
        }
        eventService.createEvent(newEvent);
        return ResponseEntity.ok(new EventDTO(newEvent));
        }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable int id) {
        Optional<Event> event = eventService.getEventById(id);
        if(event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new EventDTO(event.get()));
    }

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(new EventListDTO(eventService.getAllEvents(), true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@RequestHeader("Authorization") String token, @PathVariable int id, @RequestBody EventDTO event) {
        Optional<User> user = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        Optional<Event> existingEvent = eventService.getEventById(id);
        if(existingEvent.isEmpty() || user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event updatedEvent;
        try {
            updatedEvent = eventService.updateEvent(id, new Event(event, user.get()));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use dd-MM-yyyy");
        }
        return ResponseEntity.ok(new EventDTO(updatedEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable int id) {
        Optional<Event> event = eventService.getEventById(id);
        if(event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}
