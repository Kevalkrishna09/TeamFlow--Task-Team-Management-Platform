package com.keval.teamflow.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserTaskSummaryResponseDTO {
    private Long totalTasks;
    private Long todoTasks;
    private Long inProgressTasks;
    private Long completedTasks;
    private Long overdueTasks;
}
