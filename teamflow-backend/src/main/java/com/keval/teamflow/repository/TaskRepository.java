package com.keval.teamflow.repository;

import com.keval.teamflow.domain.enums.TaskStatus;
import com.keval.teamflow.domain.models.Task;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    long countByTeam_TeamId(String teamId);
    long countByTeam_TeamIdAndStatus(String teamId, TaskStatus status);
    int countByAssignedTo_Email(String email);

    List<Task> findAllByTeam_TeamId(String teamId);

    List<Task> findByAssignedTo_UserId(Long userId);

    Optional<Task> findByAssignedTo_UserIdAndId(Long userId, Long taskId);
}