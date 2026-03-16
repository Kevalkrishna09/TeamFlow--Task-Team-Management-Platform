package com.keval.teamflow.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TeamTaskResponseDTO {
    private Long taskId;
    private String taskName;
    private String status;
    private String priority;
    private String assignedTo;
    private LocalDate dueDate;
}
