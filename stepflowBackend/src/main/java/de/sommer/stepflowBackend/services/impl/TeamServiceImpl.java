package de.sommer.stepflowBackend.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.sommer.stepflowBackend.models.Team;
import de.sommer.stepflowBackend.models.TeamMembership;
import de.sommer.stepflowBackend.models.User;
import de.sommer.stepflowBackend.repo.TeamRepository;
import de.sommer.stepflowBackend.services.api.TeamService;
import de.sommer.stepflowBackend.services.api.UserService;

@Service
public class TeamServiceImpl implements TeamService{

    @Autowired
    TeamRepository teamRepository;

    @Autowired 
    UserService userService;

    @Override
    public Optional<Team> getTeam(int teamId) {
        return teamRepository.findById(teamId);
    }

    @Override
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Team updatedTeam) {
        return teamRepository.findById(updatedTeam.getId()).map(team -> {
            team.setTeamName(updatedTeam.getTeamName());
            team.setTeamDescription(updatedTeam.getTeamDescription());
            team.setTeamBanner(updatedTeam.getTeamBanner());
            team.setTeamLogo(updatedTeam.getTeamLogo());
            team.setTeamColor(updatedTeam.getTeamColor());
            return teamRepository.save(team);
        }).orElseThrow(() -> new RuntimeException("Team not found with id " + updatedTeam.getId()));
        
    }

    @Override
    public void deleteTeam(int teamId) {
        teamRepository.deleteById(teamId);
    }

    @Override
    public Team addUserToTeam(int teamId, int userId) {
        Optional<Team> teamOptional = getTeam(teamId);
        if(teamOptional.isPresent()){
            Team team = teamOptional.get();
            User user = userService.getUserById(userId).orElseThrow(() -> new RuntimeException("User not found with id " + userId));
            team.getMemberships().add(new TeamMembership(user, team, "member"));
            return teamRepository.save(team);
        }
        else{
            return null;
        }
    }

    @Override
    public void removeUserFromTeam(int teamId, int userId) {
        Optional<Team> teamOptional = getTeam(teamId);
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            team.getMemberships().removeIf(membership -> membership.getUser().getId() == userId);
            teamRepository.save(team);
        } else {
            throw new RuntimeException("Team not found with id " + teamId);
        }
    }

    @Override
    public Team updateUserRoleinTeam(int teamId, int userId, String role) {
        Optional<Team> teamOptional = getTeam(teamId);
        if (teamOptional.isPresent()) {
            Team team = teamOptional.get();
            team.getMemberships().stream().filter(membership -> membership.getUser().getId() == userId).findFirst().ifPresent(membership -> membership.setRole(role));
            return teamRepository.save(team);
        } else {
            throw new RuntimeException("Team not found with id " + teamId);
        }
    }


    
}
