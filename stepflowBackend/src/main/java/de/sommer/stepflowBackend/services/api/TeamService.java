package de.sommer.stepflowBackend.services.api;

import java.util.Optional;

import de.sommer.stepflowBackend.models.Team;

public interface TeamService {
    Optional<Team> getTeam(int teamId);
    Team createTeam(Team team);
    Team updateTeam(Team team);
    void deleteTeam(int teamId);
    Team addUserToTeam(int teamId, int userId);
    void removeUserFromTeam(int teamId, int userId);
    Team updateUserRoleinTeam(int teamId, int userId, String role);

} 
