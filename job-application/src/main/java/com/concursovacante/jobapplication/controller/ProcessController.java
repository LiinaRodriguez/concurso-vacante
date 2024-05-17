package com.concursovacante.jobapplication.controller;

import com.concursovacante.jobapplication.dto.JobApplicationDTO;
import com.concursovacante.jobapplication.service.impl.User_FilAppFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProcessController {
    private final User_FilAppFor applicant;

    public ProcessController(User_FilAppFor applicant) {
        this.applicant = applicant;
    }

    @PostMapping("/startProcess")
    public String startProcessInstance(@ModelAttribute JobApplicationDTO jobApplicationDTO) {
        this.applicant.startProcessInstance(jobApplicationDTO);
        return "/index";
    }

    @GetMapping("/complete")
    public String completeTask(@RequestParam(name = "taskId") String taskId) {
        this.applicant.completeTask(taskId);
        return "/index";
    }

}
