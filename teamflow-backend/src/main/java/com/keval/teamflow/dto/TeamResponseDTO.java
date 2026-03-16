package com.keval.teamflow.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeamResponseDTO {

    private String teamId;
    private String name;
    private String createdBy;
    private Instant createdTime;
    private long memberCount;
    private long totalTasks;
    private long pendingTasks;
    private long completedTasks;
    private long overdueTasks;
}