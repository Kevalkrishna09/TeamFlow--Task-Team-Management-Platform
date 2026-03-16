package com.keval.teamflow.controller;

import com.keval.teamflow.domain.models.ResponseUtil;
import com.keval.teamflow.dto.ApiResponse;
import com.keval.teamflow.services.TaskService;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/task")

public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Object>> deleteTask(@PathVariable Long taskId, Authentication authentication) {
        if (authentication.getAuthorities().stream().noneMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        taskService.deleteTask( taskId);
        return ResponseEntity.ok(ResponseUtil.success(null, HttpStatus.OK.value(), "Task deleted successfully"));
    }
}
