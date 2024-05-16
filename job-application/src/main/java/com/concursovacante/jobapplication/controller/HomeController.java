package com.concursovacante.jobapplication.controller;

import com.concursovacante.jobapplication.model.JobApplication;
import com.concursovacante.jobapplication.model.User;
import com.concursovacante.jobapplication.service.impl.JobApplicationServiceImpl;
import com.concursovacante.jobapplication.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class HomeController {
    private final JobApplicationServiceImpl jobApplicationService;
    private final UserServiceImpl userService;

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("titulo","Welcome!");
        return "index";
    }

    @GetMapping("/applicationform")
    public String applyToJob(Model model){

        User user = new User();
        JobApplication jobApplication  = new JobApplication();
       // List<JobApplication> jobApplication = new ArrayList<>();

        model.addAttribute("user",user);
        model.addAttribute("jobApplication", jobApplication);
    return "views/applicationForm";
    }
    @GetMapping("/confirmapplication")
    public String confirmApplication(@RequestParam (name = "applicantId", required = false) Long applicantId, Model model){

        List<JobApplication> jobApplications = jobApplicationService.getAllJobApplications();

        applicantId = jobApplications.get(jobApplications.size()-1).getUser().getId();

        System.out.println(applicantId);

        User user = userService.getUserById(applicantId);

        List<JobApplication> jobApplicationList = jobApplicationService.findJobApplicationByUser(user);
        JobApplication lastJobApplication = jobApplicationList.get(jobApplicationList.size()-1);

        model.addAttribute("user", user);
        model.addAttribute("jobApplication", lastJobApplication);

        return "views/confirmationApplication";
    }
}
