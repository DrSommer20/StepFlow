package de.sommer.stepflowBackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.sommer.stepflowBackend.models.EventAttendee;

public class EventAttendeeDTO {

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("status")
    private String status;

    public EventAttendeeDTO() {
    }

    public EventAttendeeDTO(int userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public EventAttendeeDTO(EventAttendee attendee) {
        this.userId = attendee.getUser().getId();
        this.status = attendee.getStatus();
    }

    @JsonProperty("userId")
    public int getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }
}