package com.keval.teamflow.services;

import com.keval.teamflow.domain.enums.InvitationStatus;
import com.keval.teamflow.domain.enums.UserRole;
import com.keval.teamflow.domain.models.Team;
import com.keval.teamflow.domain.models.TeamInvitation;
import com.keval.teamflow.domain.models.TeamMembers;
import com.keval.teamflow.domain.models.User;
import com.keval.teamflow.dto.RegisterDTO;
import com.keval.teamflow.exceptionhandler.InvalidTokenException;
import com.keval.teamflow.exceptionhandler.InvitationAccecptedException;
import com.keval.teamflow.repository.TeamInvitationRepository;
import com.keval.teamflow.repository.TeamMembersRepository;
import com.keval.teamflow.repository.TeamRepository;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {


    private final UserService userService;
    private final TeamInvitationRepository teamInvitationRepository;
    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;

    @Autowired
    public AuthService(UserService userService, TeamInvitationRepository teamInvitationRepository, TeamRepository teamRepository, TeamMembersRepository teamMembersRepository){
        this.userService = userService;
        this.teamInvitationRepository = teamInvitationRepository;
        this.teamRepository = teamRepository;
        this.teamMembersRepository = teamMembersRepository;
    }

    public User signUp(RegisterDTO request, UserRole role){
       return userService.createUser(request,role);
    }

    public User findByEmail(String email) {
        return userService.findByEmail(email);
    }

    public TeamMembers addUserToTeamViaInvitationToken(String invitationToken,User user) {

        TeamInvitation invitation = teamInvitationRepository.findByToken(invitationToken).orElseThrow(()-> new InvalidTokenException("Invalid token"));
        if(invitation.getInvitationStatus().equals(InvitationStatus.ACCEPTED))return null;

        //get user and add to team
        TeamMembers teamMember = new TeamMembers();
       Optional<Team>  team = teamRepository.findByTeamId(invitation.getTeamId());
       if(team.isEmpty()){
        throw new InvalidTokenException("Invalid token");
       }

        teamMember.setTeam(team.get());
       teamMember.setUser(user);
         teamMember.setRole(invitation.getRole());
            markInvitationAsUsed(invitation);
        return teamMembersRepository.save(teamMember);
    }

    public User createUserToTeamViaInvitationToken(String invitationToken, @Valid RegisterDTO request) {
        TeamInvitation invitation = teamInvitationRepository.findByToken(invitationToken).orElseThrow(()-> new InvalidTokenException("Invalid token"));
        if(invitation.getInvitationStatus().equals(InvitationStatus.ACCEPTED))throw new InvitationAccecptedException("Invitation already accepted");
        //get user and add to team
        TeamMembers teamMember = new TeamMembers();
        Optional<Team>  team = teamRepository.findByTeamId(invitation.getTeamId());
        if(team.isEmpty()){
            throw new InvalidTokenException("Invalid token");
        }
        User user = signUp(request,invitation.getRole());
        teamMember.setTeam(team.get());
        teamMember.setUser(user);
        teamMember.setRole(invitation.getRole());
        teamMembersRepository.save(teamMember);
        markInvitationAsUsed(invitation);

         return user;
    }
    private void markInvitationAsUsed(TeamInvitation invitation) {
        invitation.setInvitationStatus(InvitationStatus.ACCEPTED);
        teamInvitationRepository.save(invitation);
    }
}
