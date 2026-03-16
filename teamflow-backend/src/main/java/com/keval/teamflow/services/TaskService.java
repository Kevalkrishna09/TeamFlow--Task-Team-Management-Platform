package com.keval.teamflow.services;

import com.keval.teamflow.domain.enums.TaskStatus;
import com.keval.teamflow.domain.models.Task;
import com.keval.teamflow.domain.models.Team;
import com.keval.teamflow.domain.models.User;
import com.keval.teamflow.dto.CreateTaskDTO;
import com.keval.teamflow.dto.TeamTaskInfoDTO;
import com.keval.teamflow.dto.TeamTaskResponseDTO;
import com.keval.teamflow.repository.TaskRepository;
import com.keval.teamflow.repository.TeamRepository;
import com.keval.teamflow.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, TeamRepository teamRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }
    public TeamTaskInfoDTO getTeamTaskInfoCount(String  teamId) {
        long totalTasks = taskRepository.countByTeam_TeamId(teamId);
        long pendingTasks = taskRepository.countByTeam_TeamIdAndStatus(teamId, TaskStatus.TODO);
        long completedTasks = taskRepository.countByTeam_TeamIdAndStatus(teamId, TaskStatus.COMPLETED);
        long overdueTasks = taskRepository.countByTeam_TeamIdAndStatus(teamId, TaskStatus.OVERDUE);

        return new TeamTaskInfoDTO(totalTasks, pendingTasks, completedTasks, overdueTasks);
    }
    public Task createTaskForTeam(String teamId, CreateTaskDTO taskDto) {
        Team team = teamRepository.findByTeamId(teamId).orElseThrow(() -> new EntityNotFoundException("Team not found"));

        User assignedUser = userRepository.findByEmail(taskDto.getAssignedUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(taskDto.getPriority());
        task.setDueDate(taskDto.getDueDate());
        task.setStatus(TaskStatus.TODO);
        task.setAssignedTo(assignedUser);
        task.setTeam(team);
        return taskRepository.save(task);

    }


    public int getTotalTasksAssignedToUser(String email) {
        return taskRepository.countByAssignedTo_Email(email);
    }

    public List<TeamTaskResponseDTO> getTeamTaskInfo(String teamId) {
        List<Task> tasks = taskRepository.findAllByTeam_TeamId(teamId);
        for (Task task : tasks) {
             System.out.println(task.getTeam().getName());
        }
        return tasks.stream().map(task -> {
            return new  TeamTaskResponseDTO(
                    task.getId(),
                    task.getTitle(),
                    task.getStatus().toString(),
                    task.getPriority().toString(),
                    task.getAssignedTo() != null ? task.getAssignedTo().getName() : null,
                    task.getDueDate()
            );
        }).toList();
    }

    public void deleteTask( Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
