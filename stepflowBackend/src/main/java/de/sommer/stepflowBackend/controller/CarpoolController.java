package de.sommer.stepflowBackend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sommer.stepflowBackend.dto.CarpoolDTO;
import de.sommer.stepflowBackend.dto.CarpoolListDTO;
import de.sommer.stepflowBackend.models.Carpool;
import de.sommer.stepflowBackend.models.Event;
import de.sommer.stepflowBackend.models.User;
import de.sommer.stepflowBackend.services.api.CarpoolService;
import de.sommer.stepflowBackend.services.api.EventService;
import de.sommer.stepflowBackend.services.api.UserService;

@RestController
@RequestMapping("/api/carpools")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CarpoolController {

    @Autowired
    private CarpoolService carpoolService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createCarpool(@RequestBody CarpoolDTO carpool) {
        if(carpool == null || carpool.getEventId() == null || carpool.getDriver() == null || carpool.getSeatsAvailable() == null) {
            return ResponseEntity.badRequest().body("Invalid carpool");
        }

        Optional<User> driver = userService.getUserById(carpool.getDriver());
        Optional<Event> event = eventService.getEventById(carpool.getEventId());
        if(!driver.isPresent()) return ResponseEntity.badRequest().body("Driver not found");
        if(!event.isPresent()) return ResponseEntity.badRequest().body("Event not found");

        Carpool newCarpool = new Carpool();
        newCarpool.setDriver(driver.get());
        newCarpool.setEvent(event.get());
        newCarpool.setSeatsAvailable(carpool.getSeatsAvailable());
        carpool.getPassengers().forEach(passenger -> {
            Optional<User> user = userService.getUserById(passenger.getUserId());
            if(user.isPresent()) {
                newCarpool.addPassenger(user.get());
            }
        });
        carpoolService.createCarpool(newCarpool);
        return ResponseEntity.ok(newCarpool);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCarpool(@PathVariable int id) {
        Optional<Carpool> carpool = carpoolService.getCarpoolById(id);
        if(carpool.isPresent()) {
            return ResponseEntity.ok(new CarpoolDTO(carpool.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCarpools() {
        return ResponseEntity.ok(new CarpoolListDTO(carpoolService.getAllCarpools(), true));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCarpool(@PathVariable int id, @RequestBody Carpool carpool) {
        Carpool updatedCarpool = carpoolService.updateCarpool(id, carpool);
        if(updatedCarpool == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedCarpool);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarpool(@PathVariable int id) {
        if(!carpoolService.getCarpoolById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        carpoolService.deleteCarpool(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getCarpoolsByEventId(@PathVariable int eventId) {
        if(!eventService.getEventById(eventId).isPresent()) {
            return ResponseEntity.badRequest().body("Event not found");
        }
        return ResponseEntity.ok(new CarpoolListDTO(carpoolService.getCarpoolsByEventId(eventId), true));
    }
}
