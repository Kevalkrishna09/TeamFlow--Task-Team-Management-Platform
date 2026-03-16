package com.keval.teamflow.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class InvitationTokenResponseDTO {
    private String teamId;
    private String email;
    private String role;
    private Boolean isExistingUser;
}
