package de.sommer.stepflowBackend.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Column(name = "team_description")
    private String teamDescription;

    @Column(name = "team_color", nullable = false)
    private String teamColor;

    @Lob
    @Column(name = "team_logo")
    private byte[] teamLogo;

    @Lob
    @Column(name = "team_banner")
    private byte[] teamBanner;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamMembership> memberships;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    public byte[] getTeamLogo() {
        return teamLogo;
    }

    public void setTeamLogo(byte[] teamLogo) {
        this.teamLogo = teamLogo;
    }

    public byte[] getTeamBanner() {
        return teamBanner;
    }

    public void setTeamBanner(byte[] teamBanner) {
        this.teamBanner = teamBanner;
    }

    public Set<TeamMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<TeamMembership> memberships) {
        this.memberships = memberships;
    }
}