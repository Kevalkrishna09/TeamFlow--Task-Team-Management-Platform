package com.keval.teamflow.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class TeamDTO {
    @NonNull
    private String name;

    private String description;

}
