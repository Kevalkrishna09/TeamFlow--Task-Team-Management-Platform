package com.keval.teamflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamTaskInfoDTO {
    private long totalTasks;
    private long pendingTasks;
    private long completedTasks;
    private long overdueTasks;
}
