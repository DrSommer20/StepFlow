
package de.sommer.stepflowBackend.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.sommer.stepflowBackend.models.User;
import de.sommer.stepflowBackend.services.api.UserService;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "userId",
    "firstName",
    "name",
    "email",
    "password",
    "role",
    "active",
    "teamIds"
})
public class UserDTO {

    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("teamIds")
    private List<Integer> teamIds;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @Autowired
    private UserService userService;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserDTO() {
    }

    public UserDTO(Integer userId, String name, String firstName, String email, String password, Boolean active) {
        super();
        this.userId = userId;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public UserDTO(Integer userId, String name, String firstName, String email, String password, Boolean active, List<Integer> teamIds) {
        super();
        this.userId = userId;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.active = active;
        this.teamIds = teamIds;
    }

    public UserDTO(User user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.firstName = user.getFirstName();
        this.email = user.getEmail();
        this.password = "";
        this.active = user.isActive();
        this.teamIds = user.getMemberships().stream().map(membership -> membership.getTeam().getId()).toList();
    }

    @JsonProperty("userId")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("active")
    public Boolean getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonProperty("teamIds")
    public List<Integer> getTeamIds() {
        return teamIds;
    }

    @JsonProperty("teamIds")
    public void setTeamIds(List<Integer> teamIds) {
        this.teamIds = teamIds;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public static UserDTO onlyName(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getFirstName(), null, null, null);
    }

}
