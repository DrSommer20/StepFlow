package de.sommer.stepflowBackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.sommer.stepflowBackend.models.Team;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "teamName",
    "teamDescription",
    "teamColor",
    "teamLogo",
    "teamBanner"
})
public class TeamDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("teamName")
    private String teamName;

    @JsonProperty("teamDescription")
    private String teamDescription;

    @JsonProperty("teamColor")
    private String teamColor;

    @JsonProperty("teamLogo")
    private byte[] teamLogo;

    @JsonProperty("teamBanner")
    private byte[] teamBanner;

    public TeamDTO() {
    }

    public TeamDTO(Team team) {
        this.id = team.getId();
        this.teamName = team.getTeamName();
        this.teamDescription = team.getTeamDescription();
        this.teamColor = team.getTeamColor();
        this.teamLogo = team.getTeamLogo();
        this.teamBanner = team.getTeamBanner();
    }

    // Getters and setters
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("teamName")
    public String getTeamName() {
        return teamName;
    }

    @JsonProperty("teamName")
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @JsonProperty("teamDescription")
    public String getTeamDescription() {
        return teamDescription;
    }

    @JsonProperty("teamDescription")
    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    @JsonProperty("teamColor")
    public String getTeamColor() {
        return teamColor;
    }

    @JsonProperty("teamColor")
    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    @JsonProperty("teamLogo")
    public byte[] getTeamLogo() {
        return teamLogo;
    }

    @JsonProperty("teamLogo")
    public void setTeamLogo(byte[] teamLogo) {
        this.teamLogo = teamLogo;
    }

    @JsonProperty("teamBanner")
    public byte[] getTeamBanner() {
        return teamBanner;
    }

    @JsonProperty("teamBanner")
    public void setTeamBanner(byte[] teamBanner) {
        this.teamBanner = teamBanner;
    }
}