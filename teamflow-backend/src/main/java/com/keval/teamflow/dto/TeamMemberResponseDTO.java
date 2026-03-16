package com.keval.teamflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamMemberResponseDTO {
    private String memberName;
    private String memberEmail;
    private String memberRole;
    private int totalTasks;
}
