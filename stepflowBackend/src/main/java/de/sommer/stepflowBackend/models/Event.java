package de.sommer.stepflowBackend.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import de.sommer.stepflowBackend.dto.EventDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity // Markiert die Klasse als Entity für die Datenbank
@Table(name = "events") // Optional: Legt den Tabellennamen fest (Standard ist der Klassenname)
public class Event {

    @Id // Markiert das Feld als Primärschlüssel
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(nullable = false) // Titel darf nicht null sein
    private String title;

    @Column(length = 500) // Beschreibung mit maximal 500 Zeichen
    private String description;

    @Column(name = "start_date", nullable = false) // Startdatum darf nicht null sein
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false) // Enddatum darf nicht null sein
    private LocalDateTime end;

    private String location = "Test";

    @OneToMany(mappedBy = "event")
    private List<EventAttendee> eventAttendees = new ArrayList<>();

    @Column(nullable = false) // Farbe darf nicht null sein
    private String color = "#000000"; // Standardfarbe ist Schwarz


    @Column(nullable = false) // Rekurrent darf nicht null sein
    private boolean recurrent = false; // Standardmäßig nicht rekurrent

    @Column(name = "recurrence_rule") // Anführungszeichen hinzufügen!
    private String recurrenceRule;

    @Column(name = "is_all_day") // Anführungszeichen hinzufügen!
    private boolean allDay = false;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "user_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id", nullable = false)
    private Team team;

    public Event(EventDTO event, User user, Team team) {
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.start = LocalDateTime.parse(event.getStart());
        this.end = LocalDateTime.parse(event.getEnd());
        this.location = event.getLocation();
        this.color = event.getColor();
        this.recurrent = Boolean.parseBoolean(event.getRecurrent());
        this.recurrenceRule = event.getRecurrenceRule();
        this.createdBy = user;
        this.allDay = event.isAllDay();
        this.team = team;
    }

    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isRecurrent() {
        return recurrent;
    }

    public void setRecurrent(boolean recurrent) {
        this.recurrent = recurrent;
    }

    public String getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<EventAttendee> getEventAttendees() {
        return eventAttendees;
    }

    public void setEventAttendees(List<EventAttendee> eventAttendees) {
        this.eventAttendees = eventAttendees;
    }

    public void addAttendee(User user) {
        EventAttendee eventAttendee = new EventAttendee(this, user);
        eventAttendees.add(eventAttendee);
    }

    public void removeAttendee(User user) {
        eventAttendees.removeIf(eventAttendee -> eventAttendee.getUser().getId() == user.getId());
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}