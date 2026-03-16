package com.keval.teamflow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskResponseDTO {

    private String title;
    private String description;
    private String status;
    private String teamId;

}
