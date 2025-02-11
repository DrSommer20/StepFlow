package de.sommer.stepflowBackend.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.sommer.stepflowBackend.models.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer>  {
}

