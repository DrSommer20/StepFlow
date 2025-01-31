
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "penalties"
})
public class PenaltiesListDTO {

    @JsonProperty("penalties")
    private List<PenaltyDTO> penalties;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public PenaltiesListDTO() {
    }

    public PenaltiesListDTO(List<PenaltyDTO> penalties) {
        super();
        this.penalties = penalties;
    }

    @JsonProperty("penalties")
    public List<PenaltyDTO> getPenalties() {
        return penalties;
    }

    @JsonProperty("penalties")
    public void setPenalties(List<PenaltyDTO> penalties) {
        this.penalties = penalties;
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
