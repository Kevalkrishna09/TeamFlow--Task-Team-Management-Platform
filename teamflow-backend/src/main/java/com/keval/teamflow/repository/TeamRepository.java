package com.keval.teamflow.repository;

import com.keval.teamflow.domain.models.Team;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    public boolean existsByName(String name);

    Optional<Team> findByTeamId(String teamId);

    @Query("SELECT MAX(CAST(SUBSTRING(t.teamId, 6) AS int)) FROM Team t")
    Integer findMaxTeamNumber();
}
