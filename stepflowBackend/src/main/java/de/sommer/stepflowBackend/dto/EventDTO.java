
package de.sommer.stepflowBackend.dto;

import java.text.DateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.sommer.stepflowBackend.models.Event;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "eventId",
    "title",
    "description",
    "date",
    "location",
    "createdBy"
})
public class EventDTO {

    @JsonProperty("eventId")
    private Integer eventId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("date")
    private String date;
    @JsonProperty("location")
    private String location;
    @JsonProperty("createdBy")
    private Integer createdBy;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public EventDTO() {
    }

    public EventDTO(Integer eventId, String title, String description, String date, String location, Integer createdBy) {
        super();
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.createdBy = createdBy;
    }

    public EventDTO(Event newEvent) {
        this.eventId = newEvent.getEventId();
        this.title = newEvent.getTitle();
        this.description = newEvent.getDescription();
        DateFormat df = DateFormat.getDateTimeInstance();
        
        this.date = df.format(newEvent.getDate()).substring(0, 10);
        this.location = newEvent.getLocation();
        this.createdBy = newEvent.getCreatedBy().getId();
    }

    @JsonProperty("eventId")
    public Integer getEventId() {
        return eventId;
    }

    @JsonProperty("eventId")
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty("createdBy")
    public Integer getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
