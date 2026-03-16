package com.keval.teamflow.domain.models;

import com.keval.teamflow.domain.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTaskStatusUpdateDTO {
    private Long taskId;
    private TaskStatus status;
}
