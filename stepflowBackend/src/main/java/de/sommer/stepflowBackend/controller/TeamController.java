package de.sommer.stepflowBackend.controller;

import de.sommer.stepflowBackend.dto.TeamDTO;
import de.sommer.stepflowBackend.models.JoinCode;
import de.sommer.stepflowBackend.models.Team;
import de.sommer.stepflowBackend.models.User;
import de.sommer.stepflowBackend.services.api.AuthService;
import de.sommer.stepflowBackend.services.api.JoinCodeService;
import de.sommer.stepflowBackend.services.api.TeamService;
import de.sommer.stepflowBackend.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private JoinCodeService joinCodeService;

    @PostMapping("/create")
    public ResponseEntity<?> createTeam(@RequestBody Team team) {
        Team createdTeam = teamService.createTeam(team);
        return ResponseEntity.ok(new TeamDTO(createdTeam, teamService.getUserIdsofAdmin(createdTeam.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeamById(@PathVariable int id) {
        Optional<Team> team = teamService.getTeam(id);
        if (team.isPresent()) {
            return ResponseEntity.ok(new TeamDTO(team.get(), teamService.getUserIdsofAdmin(team.get().getId())));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/generateJoinCode")
    public ResponseEntity<?> generateJoinCode(@PathVariable int id, @RequestHeader("Authorization") String token) {
        Optional<User> user = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        Optional<Team> team = teamService.getTeam(id);
        if (user.isEmpty() || team.isEmpty() || !userService.isUserMemberOfTeam(user.get(), team.get().getId()))
            return ResponseEntity.status(403).build();
        JoinCode joinCode = joinCodeService.generateJoinCode(team.get());
        return ResponseEntity.ok(joinCode.getCode());
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinTeam(@RequestParam String joinCode, @RequestHeader("Authorization") String token) {
        Optional<User> user = userService.getUserByEmail(authService.extractEmail(token.replace("Bearer ", "")));
        Optional<JoinCode> code = joinCodeService.getJoinCode(joinCode);
        if (code.isPresent() && code.get().getExpirationTime().isAfter(LocalDateTime.now())) {
            if (user.isPresent()) {
                Team team = teamService.addUserToTeam(code.get().getTeam().getId(), user.get().getId());
                return ResponseEntity.ok(new TeamDTO(team, teamService.getUserIdsofAdmin(team.getId())));
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).body("Join code is invalid or expired");
        }
    }
}