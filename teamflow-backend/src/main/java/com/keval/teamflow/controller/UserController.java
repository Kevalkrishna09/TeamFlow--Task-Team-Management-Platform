package com.keval.teamflow.controller;

import com.keval.teamflow.domain.enums.UserRole;
import com.keval.teamflow.domain.enums.UserStatus;
import com.keval.teamflow.domain.models.ResponseUtil;
import com.keval.teamflow.domain.models.Team;
import com.keval.teamflow.domain.models.TeamMembers;
import com.keval.teamflow.domain.models.TeamResponse;
import com.keval.teamflow.domain.models.User;
import com.keval.teamflow.domain.models.UserTaskStatusUpdateDTO;
import com.keval.teamflow.domain.models.UserTaskSummaryResponseDTO;
import com.keval.teamflow.domain.models.UserTeamResponseDTO;
import com.keval.teamflow.dto.ApiResponse;
import com.keval.teamflow.dto.UserTasksResponseDTO;
import com.keval.teamflow.repository.TeamMembersRepository;
import com.keval.teamflow.repository.TeamRepository;
import com.keval.teamflow.repository.UserRepository;
import com.keval.teamflow.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final TeamMembersRepository teamMembersRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, TeamMembersRepository teamMembersRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder, UserService userService) {
         this.teamMembersRepository = teamMembersRepository;
        this.userRepository= userRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }
    @GetMapping("/info")
    public User getUserDetails(){
        String email = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        return userRepository.findByEmail(email).get();

    }

    @GetMapping("/{name}/create")
    @ResponseBody
    public User createUser(@PathVariable String name) {
        User user = new User();
        user.setName(name);
        user.setEmail(name + System.currentTimeMillis() + "@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(UserRole.MEMBER); // 'user' role not present, using MEMBER
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        Team team = teamRepository.findByTeamId("TEAM-0001").orElse(null);

         TeamMembers teamMembers = new TeamMembers(user, team, UserRole.MEMBER);
         teamMembersRepository.save(teamMembers);
         return user;
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<ApiResponse<UserTasksResponseDTO>> getUserTasks(@PathVariable Long userId) {
        // Implement logic to fetch tasks for the user
        var userTasks= userService.findUserTask(userId);
        return ResponseEntity.ok(ResponseUtil.success(userTasks, HttpStatus.OK.value(),"User Tasks generated"));
    }

    @GetMapping("/{userId}/teams")
    public ResponseEntity<ApiResponse<UserTeamResponseDTO>> getUserTeams(@PathVariable Long userId) {
        // Implement logic to fetch teams for the user
       List<TeamResponse> teamIds=  userService.findUserTeams(userId);
       return ResponseEntity.ok(ResponseUtil.success(new UserTeamResponseDTO(teamIds), HttpStatus.OK.value(),"User Teams generated"));
    }

    @PutMapping("/{userId}/tasks/status")
    public ResponseEntity<ApiResponse<Object>> updateTaskStatus(@PathVariable Long userId, @RequestBody UserTaskStatusUpdateDTO taskStatusUpdate) {
        // Implement logic to update task status for the user
       userService.updateTask(userId, taskStatusUpdate);
       return ResponseEntity.ok(ResponseUtil.success(null, HttpStatus.OK.value(),"Task status updated successfully"));
    }

    @GetMapping("/{userId}/tasks/summary")
    public ResponseEntity<ApiResponse<UserTaskSummaryResponseDTO>> getUserTaskSummary(@PathVariable Long userId) {
        // Implement logic to fetch task summary for the user
        UserTaskSummaryResponseDTO taskSummary = userService.findTaskSummary(userId);
        return  ResponseEntity.ok(ResponseUtil.success(taskSummary, HttpStatus.OK.value(),"Task Summary generated"));
    }


    @PutMapping("/{userId}/password")
    public ResponseEntity<ApiResponse<Object>> updatePassword(@PathVariable Long userId, @RequestBody String newPassword) {
        // Implement logic to update password for the user
        userService.updatePassword(userId, newPassword);
        return ResponseEntity.ok(ResponseUtil.success(null, HttpStatus.OK.value(),"Password updated successfully"));
    }
}
