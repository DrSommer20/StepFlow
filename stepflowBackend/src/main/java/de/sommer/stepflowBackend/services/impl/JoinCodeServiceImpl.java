package de.sommer.stepflowBackend.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.sommer.stepflowBackend.models.JoinCode;
import de.sommer.stepflowBackend.models.Team;
import de.sommer.stepflowBackend.repo.JoinCodeRepository;
import de.sommer.stepflowBackend.services.api.JoinCodeService;

@Service
public class JoinCodeServiceImpl implements JoinCodeService{

    @Autowired
    private JoinCodeRepository joinCodeRepository;

    public JoinCode generateJoinCode(Team team) {
        String code = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(48);
        JoinCode joinCode = new JoinCode(code, expirationTime, team);
        return joinCodeRepository.save(joinCode);
    }

    public Optional<JoinCode> getJoinCode(String code) {
        return joinCodeRepository.findByCode(code);
    }
    
}
