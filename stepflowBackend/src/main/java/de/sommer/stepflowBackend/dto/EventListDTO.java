
package de.sommer.stepflowBackend.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
    "events"
})
public class EventListDTO {

    @JsonProperty("events")
    private List<EventDTO> events;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public EventListDTO() {
    }

    public EventListDTO(List<EventDTO> events) {
        super();
        this.events = events;
    }

    public EventListDTO(List<Event> allEvents, boolean fromEvents) {
        this.events = new ArrayList<>();
        for (Event event : allEvents) {
            this.events.add(new EventDTO(event));
        }
    }

    @JsonProperty("events")
    public List<EventDTO> getPenalties() {
        return events;
    }

    @JsonProperty("events")
    public void setPenalties(List<EventDTO> events) {
        this.events = events;
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
