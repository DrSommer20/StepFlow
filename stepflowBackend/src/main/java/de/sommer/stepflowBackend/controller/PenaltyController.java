package de.sommer.stepflowBackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.sommer.stepflowBackend.models.Penalty;

@RestController
@RequestMapping("/api/penalties")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PenaltyController {

    @PostMapping
    public Penalty createPenalty(@RequestBody Penalty penalty) {
        // Logic to create a penalty
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/{id}")
    public Penalty getPenalty(@PathVariable Long id) {
        // Logic to get a penalty by id
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping
    public List<Penalty> getAllPenalties() {
        // Logic to get all penalties
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PutMapping("/{id}")
    public Penalty updatePenalty(@PathVariable Long id, @RequestBody Penalty penalty) {
        // Logic to update a penalty
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @DeleteMapping("/{id}")
    public void deletePenalty(@PathVariable Long id) {
        // Logic to delete a penalty
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
