package de.sommer.stepflowBackend.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "carpools")
public class Carpool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carpool_id", nullable = false)
    private int carpoolId;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "user_id", nullable = false)
    private User driver;

    @Column(name = "seats_available")
    private int seatsAvailable;

    @ManyToMany
    @JoinTable(
        name = "carpool_Passengers",
        joinColumns = @JoinColumn(name = "carpool_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> passengers;

    // Getters and Setters

    public int getCarpoolId() {
        return carpoolId;
    }

    public void setCarpoolId(int carpoolId) {
        this.carpoolId = carpoolId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public Set<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<User> passengers) {
        this.passengers = passengers;
    }

    public void addPassenger(User passenger) {
        this.passengers.add(passenger);
    }

    public void removePassenger(User passenger) {
        this.passengers.remove(passenger);
    }
}
