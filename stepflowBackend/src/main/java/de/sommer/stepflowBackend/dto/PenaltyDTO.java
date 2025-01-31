
package de.sommer.stepflowBackend.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "penaltyId",
    "userId",
    "amount",
    "reason",
    "paid"
})
public class PenaltyDTO {

    @JsonProperty("penaltyId")
    private Integer penaltyId;
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("amount")
    private Float amount;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("paid")
    private Boolean paid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public PenaltyDTO() {
    }

    public PenaltyDTO(Integer penaltyId, Integer userId, Float amount, String reason, Boolean paid) {
        super();
        this.penaltyId = penaltyId;
        this.userId = userId;
        this.amount = amount;
        this.reason = reason;
        this.paid = paid;
    }

    @JsonProperty("penaltyId")
    public Integer getPenaltyId() {
        return penaltyId;
    }

    @JsonProperty("penaltyId")
    public void setPenaltyId(Integer penaltyId) {
        this.penaltyId = penaltyId;
    }

    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("amount")
    public Float getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    @JsonProperty("paid")
    public Boolean getPaid() {
        return paid;
    }

    @JsonProperty("paid")
    public void setPaid(Boolean paid) {
        this.paid = paid;
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
