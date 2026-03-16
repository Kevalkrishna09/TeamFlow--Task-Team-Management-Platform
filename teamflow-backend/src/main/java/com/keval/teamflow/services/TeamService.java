package com.keval.teamflow.services;

import com.keval.teamflow.domain.enums.InvitationStatus;
import com.keval.teamflow.domain.enums.UserRole;
import com.keval.teamflow.domain.models.EmailDetails;
import com.keval.teamflow.domain.models.Team;
import com.keval.teamflow.domain.models.TeamInvitation;
import com.keval.teamflow.domain.models.TeamMembers;
import com.keval.teamflow.domain.models.User;
import com.keval.teamflow.dto.TeamDTO;
import com.keval.teamflow.dto.TeamMemberInviteDTO;
import com.keval.teamflow.dto.TeamMemberResponseDTO;
import com.keval.teamflow.dto.TeamResponseDTO;
import com.keval.teamflow.dto.TeamTaskInfoDTO;
import com.keval.teamflow.exceptionhandler.TeamAlreadyExistsException;
import com.keval.teamflow.exceptionhandler.UserNotFoundException;
import com.keval.teamflow.repository.TeamInvitationRepository;
import com.keval.teamflow.repository.TeamMembersRepository;
import com.keval.teamflow.repository.TeamRepository;
import com.keval.teamflow.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;
    private final UserRepository userRepository;
    private final TeamInvitationRepository teamInvitationRepository;
    private final EmailService emailService;
    private final TaskService taskService;

    @Autowired
    public TeamService(TeamRepository teamRepository,TeamMembersRepository teamMembersRepository,UserRepository userRepository,TeamInvitationRepository teamInvitationRepository,
    EmailService emailservice,TaskService taskService) {
         this.taskService = taskService;
        this.teamRepository = teamRepository;
        this.teamMembersRepository = teamMembersRepository;
        this.userRepository = userRepository;
        this.teamInvitationRepository = teamInvitationRepository;
        this.emailService= emailservice;
    }

    private final String websiteURL = "http://teamflow.com/invitation?token=";
    @Transactional
    public Team createTeamWithAdmin(TeamDTO teamDto , String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User not found"));
        if(teamRepository.existsByName(teamDto.getName())) {
            throw new TeamAlreadyExistsException("Team already exists");
        }

         Team team = teamRepository.save(new Team(generatePublicId(),teamDto.getName(),teamDto.getDescription()));
         teamMembersRepository.save(new TeamMembers(user, team,UserRole.ADMIN));
         return team;
    }
    public TeamInvitation createInvitationAndSendEmail(TeamMemberInviteDTO teamMemberInviteDTO,String teamId){
        UserRole role = teamMemberInviteDTO.getRole().equals(UserRole.TEAM_LEAD.toString())? UserRole.TEAM_LEAD : UserRole.MEMBER;

        String token =  UUID.randomUUID().toString();
        TeamInvitation teamInvitation= new TeamInvitation();
        teamInvitation.setTeamId(teamId);
        teamInvitation.setEmail(teamMemberInviteDTO.getEmail());
        teamInvitation.setExpiryTime(Instant.now().plus(3, ChronoUnit.DAYS));
        teamInvitation.setRole(role);
        teamInvitation.setToken(token);
        teamInvitation.setInvitationStatus(InvitationStatus.PENDING);
        teamInvitationRepository.save(teamInvitation);
        //send an email
        String subject = "Welcome to teamflow";
        String messageBody = websiteURL+token;
        EmailDetails details = new EmailDetails(teamMemberInviteDTO.getEmail(),messageBody,subject);
        emailService.sendSimpleMail(details);
        return teamInvitation;
    }

    public boolean isEligibleOwner(String email, String teamId){
       return  teamMembersRepository.existsByUserEmailAndTeam_TeamIdAndRole(email,teamId,UserRole.ADMIN);

    }


    public Team getTeamByTeamId(String teamId){
        return teamRepository.findByTeamId(teamId).orElseThrow(()-> new EntityNotFoundException("Team not found"));
    }

    public List<Team> getAllTeams(String  email){
        List<TeamMembers> teamMembers = teamMembersRepository.findAllByUserEmail(email);
        return teamMembers.stream().map(TeamMembers::getTeam).toList();
    }
    public String generatePublicId() {
        Integer max = teamRepository.findMaxTeamNumber(); // custom query needed
        int next = (max == null) ? 1 : max + 1;
        if (next > 9999) {
            throw new RuntimeException("Counter exceeded");
        }
        return String.format("TEAM-%04d", next);
    }
    public TeamResponseDTO getTeamInfo(Team team, Authentication authentication){
        TeamTaskInfoDTO teamTaskInfoDTO = taskService.getTeamTaskInfoCount(team.getTeamId());
        long totalTeamMembers = teamMembersRepository.countByTeam_TeamIdAndRole(team.getTeamId(),UserRole.MEMBER);
        return new TeamResponseDTO(
                team.getTeamId(),
                team.getName(),
                authentication.getName(),
                team.getCreatedDate(),
                totalTeamMembers,
                teamTaskInfoDTO.getTotalTasks(),
                teamTaskInfoDTO.getPendingTasks(),
                teamTaskInfoDTO.getCompletedTasks(),
                teamTaskInfoDTO.getOverdueTasks()
        );
    }
    public List<TeamMemberResponseDTO> getTeamMemberInfo(String teamId){
      List<TeamMembers> teamMembers = teamMembersRepository.findAllByTeam_TeamId(teamId);
        return  teamMembers.stream().map(member-> new TeamMemberResponseDTO(
                member.getUser().getName(),
                member.getUser().getEmail(),
                member.getRole().toString(),
                taskService.getTotalTasksAssignedToUser(member.getUser().getEmail())
        )).toList();
    }
    public List<TeamResponseDTO> getTeamsInfo(List<Team> teams, Authentication authentication)
    {
       return  teams.stream().map( team->{
            TeamTaskInfoDTO teamTaskInfoDTO = taskService.getTeamTaskInfoCount(team.getTeamId());
            long totalTeamMembers = teamMembersRepository.countByTeam_TeamIdAndRole(team.getTeamId(),UserRole.MEMBER);
            return new TeamResponseDTO(
                    team.getTeamId(),
                    team.getName(),
                    authentication.getName(),
                    team.getCreatedDate(),
                    totalTeamMembers,
                    teamTaskInfoDTO.getTotalTasks(),
                    teamTaskInfoDTO.getPendingTasks(),
                    teamTaskInfoDTO.getCompletedTasks(),
                    teamTaskInfoDTO.getOverdueTasks()
            );
        }).toList();
    }

}
