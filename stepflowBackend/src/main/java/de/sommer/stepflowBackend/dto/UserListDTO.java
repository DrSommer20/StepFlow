
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

import de.sommer.stepflowBackend.models.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "users"
})
public class UserListDTO {

    @JsonProperty("users")
    private List<UserDTO> users;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserListDTO() {
    }

    public UserListDTO(List<UserDTO> users) {
        super();
        this.users = users;
    }

    public UserListDTO(List<User> allUsers, boolean fromUsers) {
        for (User user : allUsers) {
            this.users.add(new UserDTO(user));
        }
    }

    @JsonProperty("users")
    public List<UserDTO> getUsers() {
        return users;
    }

    @JsonProperty("users")
    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public static UserListDTO onlyName(List<User> allUsers) {
        UserListDTO userListDTO = new UserListDTO();
        List<UserDTO> users = new ArrayList<>();
        for (User user : allUsers) {
            users.add(UserDTO.onlyName(user));
        }
        userListDTO.setUsers(users);
        return userListDTO;
    }

}
