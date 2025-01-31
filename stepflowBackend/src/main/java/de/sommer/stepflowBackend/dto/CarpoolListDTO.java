
package de.sommer.stepflowBackend.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.sommer.stepflowBackend.models.Carpool;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "carpools"
})
public class CarpoolListDTO {

    @JsonProperty("carpools")
    private List<CarpoolDTO> carpools;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public CarpoolListDTO() {
    }

    public CarpoolListDTO(List<CarpoolDTO> carpools) {
        super();
        this.carpools = carpools;
    }

    public CarpoolListDTO(List<Carpool> allCarpools, boolean fromCarpools) {
        for (Carpool carpool : allCarpools) {
            this.carpools.add(new CarpoolDTO(carpool));
        }
    }

    @JsonProperty("carpools")
    public List<CarpoolDTO> getPenalties() {
        return carpools;
    }

    @JsonProperty("carpools")
    public void setPenalties(List<CarpoolDTO> carpools) {
        this.carpools = carpools;
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
