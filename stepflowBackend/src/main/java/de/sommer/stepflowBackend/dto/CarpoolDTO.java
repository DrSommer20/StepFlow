
package de.sommer.stepflowBackend.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.sommer.stepflowBackend.models.Carpool;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "carpoolId",
    "eventId",
    "driver",
    "seatsAvailable",
    "passengers"
})
public class CarpoolDTO {

    @JsonProperty("carpoolId")
    private Integer carpoolId;
    @JsonProperty("eventId")
    private Integer eventId;
    @JsonProperty("driver")
    private Integer driver;
    @JsonProperty("seatsAvailable")
    private Integer seatsAvailable;
    @JsonProperty("passengers")
    private List<Passenger> passengers;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public CarpoolDTO() {
    }

    public CarpoolDTO(Integer carpoolId, Integer eventId, Integer driver, Integer seatsAvailable, List<Passenger> passengers) {
        super();
        this.carpoolId = carpoolId;
        this.eventId = eventId;
        this.driver = driver;
        this.seatsAvailable = seatsAvailable;
        this.passengers = passengers;
    }

    public CarpoolDTO(Carpool carpool) {
        this.carpoolId = carpool.getCarpoolId();
        this.eventId = carpool.getEvent().getEventId();
        this.driver = carpool.getDriver().getId();
        this.seatsAvailable = carpool.getSeatsAvailable();
        this.passengers = carpool.getPassengers().stream().map(user -> new Passenger(user)).collect(Collectors.toList());
    }

    @JsonProperty("carpoolId")
    public Integer getCarpoolId() {
        return carpoolId;
    }

    @JsonProperty("carpoolId")
    public void setCarpoolId(Integer carpoolId) {
        this.carpoolId = carpoolId;
    }

    @JsonProperty("eventId")
    public Integer getEventId() {
        return eventId;
    }

    @JsonProperty("eventId")
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    @JsonProperty("driver")
    public Integer getDriver() {
        return driver;
    }

    @JsonProperty("driver")
    public void setDriver(Integer driver) {
        this.driver = driver;
    }

    @JsonProperty("seatsAvailable")
    public Integer getSeatsAvailable() {
        return seatsAvailable;
    }

    @JsonProperty("seatsAvailable")
    public void setSeatsAvailable(Integer seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    @JsonProperty("passengers")
    public List<Passenger> getPassengers() {
        return passengers;
    }

    @JsonProperty("passengers")
    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
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
