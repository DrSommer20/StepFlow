package de.sommer.stepflowBackend.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import de.sommer.stepflowBackend.models.Team;
import de.sommer.stepflowBackend.models.User;
import de.sommer.stepflowBackend.repo.TeamRepository;
import de.sommer.stepflowBackend.services.api.TeamService;

public class TeamServiceImpl implements TeamService{

    @Autowired
    TeamRepository teamRepository;

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
        }.orElseThrow(() -> new RuntimeException("Team not found with id " + updatedTeam.getId()))
            
        )
        
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
            team.
        }
        else{
            return null;
        }
    }

    @Override
    public void removeUserFromTeam(int teamId, int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeUserFromTeam'");
    }

    @Override
    public Team updateUserRoleinTeam(int teamId, int userId, String role) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserRoleinTeam'");
    }


    
}
