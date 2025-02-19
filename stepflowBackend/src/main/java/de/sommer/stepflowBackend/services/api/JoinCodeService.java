package de.sommer.stepflowBackend.services.api;

import java.util.Optional;

import de.sommer.stepflowBackend.models.JoinCode;
import de.sommer.stepflowBackend.models.Team;

public interface JoinCodeService {

    public JoinCode generateJoinCode(Team team);
    public Optional<JoinCode> getJoinCode(String code);
    
}
