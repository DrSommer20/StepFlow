package de.sommer.stepflowBackend.controller;

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
import de.sommer.stepflowBackend.services.api.TeamService;
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

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventDTO event, @RequestHeader("Team") int teamId, @RequestHeader("Authorization") String token) {
        if(!checkUserPartOfTeam(token, teamId)) {
            return ResponseEntity.status(403).build();
        }
        Optional<User> user = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        if(event == null || event.getTitle() == null || event.getStart() == null || event.getEnd() == null || user.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid event. Information missing, please provide title, date and User");
        }
        Event newEvent;
        try{
            newEvent = new Event(event, user.get(), teamService.getTeam(teamId).get());
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body("Invalid date format. Use yyyy-MM-dd");
        }
        eventService.createEvent(newEvent);
        return ResponseEntity.ok(new EventDTO(newEvent));
        }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(@PathVariable int id, @RequestHeader("Team") int teamId, @RequestHeader("Authorization") String token) {
        if(!checkUserPartOfTeam(token, teamId)) {
            return ResponseEntity.status(403).build();
        }
        Optional<Event> event = eventService.getEventById(id);
        if(event.isEmpty() || event.get().getTeam().getId() != teamId) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new EventDTO(event.get()));
    }

    @GetMapping
    public ResponseEntity<?> getAllEvents(@RequestHeader("Team") int teamId, @RequestHeader("Authorization") String token) {
        if(!checkUserPartOfTeam(token, teamId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(new EventListDTO(eventService.getAllEvents(teamId), true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable int id, @RequestBody EventDTO event, @RequestHeader("Team") int teamId, @RequestHeader("Authorization") String token) {
        if(!checkUserPartOfTeam(token, teamId)) {
            return ResponseEntity.status(403).build();
        }
        Optional<User> user = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        Optional<Event> existingEvent = eventService.getEventById(id);
        if(existingEvent.isEmpty() || user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event updatedEvent = eventService.updateEvent(id, new Event(event, user.get(), teamService.getTeam(teamId).get()));
        return ResponseEntity.ok(new EventDTO(updatedEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable int id, @RequestHeader("Team") int teamId, @RequestHeader("Authorization") String token) {
        if(!checkUserPartOfTeam(token, teamId)) {
            return ResponseEntity.status(403).build();
        }
        Optional<Event> event = eventService.getEventById(id);
        if(event.isEmpty() || event.get().getTeam().getId() != teamId) {
            return ResponseEntity.notFound().build();
        }
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/today")
    public ResponseEntity<?> isEventToday(@RequestHeader("Team") int teamId, @RequestHeader("Authorization") String token) {
        if(!checkUserPartOfTeam(token, teamId)) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(new EventListDTO(eventService.eventsToday(teamId), true));
    }

    private boolean checkUserPartOfTeam(String token, int teamId) {
        Optional<User> currentUser = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        if (currentUser.isEmpty() || !userService.isUserMemberOfTeam(currentUser.get(), teamId)) {
            return false;
        }
        return true;
    }
    
}
