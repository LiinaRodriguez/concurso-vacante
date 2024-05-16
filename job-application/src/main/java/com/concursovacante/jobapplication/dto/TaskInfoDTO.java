package com.concursovacante.jobapplication.dto;

import lombok.Data;

@Data
public class TaskInfoDTO {
    private String processId;
    private String taskId;
    private String taskName;
    private String taskAssignee;
}
