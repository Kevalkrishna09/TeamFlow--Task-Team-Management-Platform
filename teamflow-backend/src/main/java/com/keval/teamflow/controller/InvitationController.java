package com.keval.teamflow.controller;

import com.keval.teamflow.domain.models.InvitationTokenResponseDTO;
import com.keval.teamflow.domain.models.ResponseUtil;
import com.keval.teamflow.domain.models.TeamInvitation;
import com.keval.teamflow.dto.ApiResponse;
import com.keval.teamflow.exceptionhandler.InvalidTokenException;
import com.keval.teamflow.repository.TeamInvitationRepository;
import com.keval.teamflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invitation")
public class InvitationController {
    @Autowired
    TeamInvitationRepository teamInvitationRepository;

    @Autowired
    UserRepository userRepository;
    //it will return the teamId and the email of the user with role who is invited to join the team
    //the token will be used to accept or reject the invitation
    //will also return if user already present or not so that we can show signup or login page accordingly
    @GetMapping("/{tokenId}")
    public ResponseEntity<ApiResponse<InvitationTokenResponseDTO>> validateInvitationToken(@PathVariable  String tokenId){
        TeamInvitation teamInvitation = teamInvitationRepository.findByToken(tokenId).orElseThrow(()-> new InvalidTokenException("Invalid token"));
        if(teamInvitation.getExpiryTime().isBefore(java.time.Instant.now())){
            throw new InvalidTokenException("Token expired");
        }
        //check if user already present or not
        boolean userExists = userRepository.existsByEmail(teamInvitation.getEmail());
        InvitationTokenResponseDTO responseDTO = new InvitationTokenResponseDTO(
                teamInvitation.getTeamId(),
                teamInvitation.getEmail(),
                teamInvitation.getRole().toString(),
                userExists);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseUtil.success(responseDTO, HttpStatus.OK.value(), "Token validated successfully"));
    }
}
