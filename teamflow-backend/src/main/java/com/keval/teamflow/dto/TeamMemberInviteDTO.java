package com.keval.teamflow.dto;

import com.keval.teamflow.domain.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberInviteDTO {
    private String email;
    private UserRole role;
}
