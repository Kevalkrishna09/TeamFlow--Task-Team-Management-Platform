package com.keval.teamflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TeamInviteResponseDTO {
    private String email;
    private String message;
}
