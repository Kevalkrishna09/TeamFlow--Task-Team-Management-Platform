package com.keval.teamflow.domain.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserTeamResponseDTO {
    private List<TeamResponse> teams;
}
