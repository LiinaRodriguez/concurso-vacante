package com.concursovacante.jobapplication.service;

import com.concursovacante.jobapplication.dto.JobApplicationDTO;
import com.concursovacante.jobapplication.dto.TaskInfoDTO;
import org.springframework.scheduling.config.Task;

import java.sql.SQLException;

public interface IApplicantService {
    String startProcessInstance(JobApplicationDTO jobApplicationDTO);
    void setAssignee(String taskId, String userId);
    TaskInfoDTO getTaskInfoByProcessId(String processId);
    TaskInfoDTO getTaskInfoByProcessIdWithApi(String processId);
    void completeTask(String processId);
    void messageEvent(String processId);
    Long getCountReview(String processId);
   void updateReviewAndStatus(String processId,String subProcessId,  String status) throws SQLException;
   String getSubProcessInstanceById(String processId);


}
