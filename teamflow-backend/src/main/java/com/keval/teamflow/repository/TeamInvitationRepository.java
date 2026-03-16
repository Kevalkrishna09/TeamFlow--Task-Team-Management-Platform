package com.keval.teamflow.repository;

import com.keval.teamflow.domain.models.TeamInvitation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation,Long> {
    Optional<TeamInvitation> findByToken(String tokenId);
}
