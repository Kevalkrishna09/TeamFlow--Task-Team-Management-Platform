package com.keval.teamflow.services;

import com.keval.teamflow.domain.enums.TaskStatus;
import com.keval.teamflow.domain.enums.UserRole;
import com.keval.teamflow.domain.enums.UserStatus;
import com.keval.teamflow.domain.models.Task;
import com.keval.teamflow.domain.models.TeamMembers;
import com.keval.teamflow.domain.models.TeamResponse;
import com.keval.teamflow.domain.models.User;
import com.keval.teamflow.domain.models.UserTaskStatusUpdateDTO;
import com.keval.teamflow.domain.models.UserTaskSummaryResponseDTO;
import com.keval.teamflow.dto.RegisterDTO;
import com.keval.teamflow.dto.UserTaskDTO;
import com.keval.teamflow.dto.UserTasksResponseDTO;
import com.keval.teamflow.repository.TaskRepository;
import com.keval.teamflow.repository.TeamMembersRepository;
import com.keval.teamflow.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskRepository taskRepository;
    private final TeamMembersRepository teamMembersRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TaskRepository taskRepository, TeamMembersRepository teamMembersRepository){
        this.userRepository= userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskRepository = taskRepository;
        this.teamMembersRepository = teamMembersRepository;
    }
    public User createUser(RegisterDTO userDto, UserRole role){
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public UserTaskSummaryResponseDTO findTaskSummary(Long userId) {
       List<Task> userTasks = taskRepository.findByAssignedTo_UserId(userId);
         long totalTasks = userTasks.size();
         long completedTasks = userTasks.stream().filter(task -> task.getStatus() == TaskStatus.COMPLETED && (LocalDate.now().isBefore(task.getDueDate()))).count();
         long inProgressTasks = userTasks.stream().filter(task -> task.getStatus() == TaskStatus.IN_PROGRESS).count();
         long toDo = userTasks.stream().filter(task -> task.getStatus() == TaskStatus.TODO).count();
        long overDue =  userTasks.stream().filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(java.time.LocalDate.now()) && task.getStatus() != TaskStatus.COMPLETED).count();
        return new UserTaskSummaryResponseDTO(totalTasks,toDo, inProgressTasks, completedTasks,overDue);
    }

    public UserTasksResponseDTO findUserTask(Long userId) {
        List<Task> userTasks = taskRepository.findByAssignedTo_UserId(userId);
        List<UserTaskDTO> userTaskDTOS=  userTasks.stream().map((task)->
                (
                new UserTaskDTO(task.getId(), task.getTitle(), task.getTeam().getName(), task.getDueDate(), task.getStatus())
                )
        ).toList();
        return new UserTasksResponseDTO(userTaskDTOS);
    }

    public void updateTask(Long  userId, UserTaskStatusUpdateDTO taskStatusUpdate) {
        log.info("Updating task status for userId: {}, taskId: {}, newStatus: {}", userId, taskStatusUpdate.getTaskId(), taskStatusUpdate.getStatus());
        Task task = taskRepository.findByAssignedTo_UserIdAndId(userId,taskStatusUpdate.getTaskId()).orElse(null);
        if (task != null && task.getAssignedTo().getUserId().equals(userId)) {
            task.setStatus(taskStatusUpdate.getStatus());
            taskRepository.save(task);
        }
    }

    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }

    }

    public List<TeamResponse> findUserTeams(Long userId) {
        List<TeamMembers> teamMembers =teamMembersRepository.findAllByUser_UserId(userId);
        return teamMembers.stream().map(tm -> new TeamResponse(tm.getTeam().getTeamId(),tm.getTeam().getName())).toList();
    }
}
