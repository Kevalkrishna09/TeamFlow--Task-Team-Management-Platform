package com.keval.teamflow.repository;

import com.keval.teamflow.domain.enums.UserRole;
import com.keval.teamflow.domain.models.TeamMembers;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMembersRepository extends JpaRepository<TeamMembers,Long> {
     boolean existsByUserEmailAndTeam_TeamIdAndRole(String email, String teamId, UserRole role);
     List<TeamMembers> findAllByUserEmail(String email);
     long countByTeam_TeamIdAndRole(String teamId, UserRole role);
     List<TeamMembers> findAllByTeam_TeamId(String teamId);

     List<TeamMembers> findAllByUser_UserId(Long userId);
}
