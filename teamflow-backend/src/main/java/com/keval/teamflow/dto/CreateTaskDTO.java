package com.keval.teamflow.dto;

import com.keval.teamflow.domain.enums.TaskPriority;
import com.keval.teamflow.domain.enums.TaskStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateTaskDTO {
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDate dueDate;
    private String assignedUserId;
}
