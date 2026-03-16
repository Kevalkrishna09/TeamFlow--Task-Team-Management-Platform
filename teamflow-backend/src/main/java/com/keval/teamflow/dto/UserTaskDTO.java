package com.keval.teamflow.dto;

import com.keval.teamflow.domain.enums.TaskStatus;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserTaskDTO {
    private Long taskId;
    private String taskName;
    private String teamName;
    private LocalDate dueDate;
    private TaskStatus status;
}
