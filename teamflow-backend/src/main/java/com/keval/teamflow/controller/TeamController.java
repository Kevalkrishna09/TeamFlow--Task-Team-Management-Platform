package com.keval.teamflow.controller;

import com.keval.teamflow.domain.models.ResponseUtil;
import com.keval.teamflow.domain.models.Task;
import com.keval.teamflow.domain.models.Team;
import com.keval.teamflow.domain.models.TeamInvitation;
import com.keval.teamflow.dto.ApiResponse;
import com.keval.teamflow.dto.CreateTaskDTO;
import com.keval.teamflow.dto.CreateTaskResponseDTO;
import com.keval.teamflow.dto.TeamDTO;
import com.keval.teamflow.dto.TeamInviteResponseDTO;
import com.keval.teamflow.dto.TeamMemberInviteDTO;
import com.keval.teamflow.dto.TeamMemberResponseDTO;
import com.keval.teamflow.dto.TeamResponseDTO;
import com.keval.teamflow.dto.TeamTaskResponseDTO;
import com.keval.teamflow.services.TaskService;
import com.keval.teamflow.services.TeamService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final TaskService taskService;

    @Autowired
    public TeamController(TeamService teamService, TaskService taskService) {
        this.teamService = teamService;
        this.taskService = taskService;
    }

    @PostMapping("")
    public ResponseEntity<TeamResponseDTO> createTeam(@RequestBody TeamDTO teamDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        Team team = teamService.createTeamWithAdmin(teamDto, email);

        // Fill in 0 for all task-related fields in TeamResponseDTO
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TeamResponseDTO(
                        team.getTeamId(),
                        team.getName(),
                        email,
                        team.getCreatedDate(),
                        0,
                        0, // totalTasks
                        0, // pendingTasks
                        0, // completedTasks
                        0  // overdueTasks
                ));
    }

    //create a user and send invite to a member via email ,so that he can log in
    @PostMapping("/{teamId}/invite")
    public ResponseEntity<TeamInviteResponseDTO> inviteTeamMember(@Valid @RequestBody TeamMemberInviteDTO teamMemberInviteDTO, @PathVariable String teamId,Authentication authentication) {
        if(!teamService.isEligibleOwner(authentication.getName(),teamId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TeamInviteResponseDTO(teamMemberInviteDTO.getEmail(),"Un authorised"));
        }
        TeamInvitation teamInvitation = teamService.createInvitationAndSendEmail(teamMemberInviteDTO, teamId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TeamInviteResponseDTO(teamMemberInviteDTO.getEmail(),"teamCreated and mail sent"));
    }

    //get team
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDTO> getTeam(@PathVariable String teamId,Authentication authentication) {
        if(!teamService.isEligibleOwner(authentication.getName(),teamId)){
            System.out.println("User {} is not an owner, cannot access teams"+ authentication.getName());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Team team = teamService.getTeamByTeamId(teamId);
        return  ResponseEntity.ok(teamService.getTeamInfo(team,authentication));
    }
    //get all team for a user
    @GetMapping("")
    public ResponseEntity<List<TeamResponseDTO>> getAllTeams(Authentication authentication) {
        if(authentication.getAuthorities().stream().noneMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"))){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Team> teams = teamService.getAllTeams(authentication.getName());
        List<TeamResponseDTO> teamResponseDTOS =  teamService.getTeamsInfo(teams, authentication);
        return ResponseEntity.ok(teamResponseDTOS);
    }

    //create Task for a team
    // only admin/ teamLead can create task for a team
    @PostMapping("/{teamId}/task")
    public ResponseEntity<ApiResponse<CreateTaskResponseDTO>> createTaskForTeam(@PathVariable String teamId, @RequestBody CreateTaskDTO createTaskDTO, Authentication authentication){
        log.info("inside createTaskForTeam method with teamId: {} and task title: {}", teamId, createTaskDTO.getTitle());

        if(authentication.getAuthorities().stream().noneMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Task task  = taskService.createTaskForTeam(teamId, createTaskDTO);
        CreateTaskResponseDTO createTaskResponseDTO = new CreateTaskResponseDTO();
        createTaskResponseDTO.setTitle(task.getTitle());
        createTaskResponseDTO.setDescription(task.getDescription());
        createTaskResponseDTO.setStatus(task.getStatus().toString());
        createTaskResponseDTO.setTeamId(teamId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseUtil.success(createTaskResponseDTO, HttpStatus.OK.value(), "Login successful"));
    }
    @GetMapping("/{teamId}/members")
    public ResponseEntity<ApiResponse<List<TeamMemberResponseDTO>>> getTeamMembers(@PathVariable String teamId, Authentication authentication) {
        if (!teamService.isEligibleOwner(authentication.getName(), teamId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<TeamMemberResponseDTO> members = teamService.getTeamMemberInfo(teamId);
        return ResponseEntity.ok(ResponseUtil.success(members, HttpStatus.OK.value(), "Team members retrieved successfully"));
    }

    @GetMapping("/{teamId}/tasks")
    public ResponseEntity<ApiResponse<List<TeamTaskResponseDTO>>> getTeamTaskInfo(@PathVariable String teamId, Authentication authentication) {
        if (!teamService.isEligibleOwner(authentication.getName(), teamId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<TeamTaskResponseDTO> taskInfo = taskService.getTeamTaskInfo(teamId);

        return ResponseEntity.ok(ResponseUtil.success(taskInfo, HttpStatus.OK.value(), "Team task info retrieved successfully"));
    }



}
