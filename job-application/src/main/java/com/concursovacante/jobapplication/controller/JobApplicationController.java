package com.concursovacante.jobapplication.controller;

import com.concursovacante.jobapplication.model.JobApplication;
import com.concursovacante.jobapplication.dto.JobApplicationDTO;
import com.concursovacante.jobapplication.model.User;
import com.concursovacante.jobapplication.service.impl.JobApplicationServiceImpl;
import com.concursovacante.jobapplication.service.impl.UserServiceImpl;
import com.concursovacante.jobapplication.service.impl.User_FilAppFor;
import com.concursovacante.jobapplication.utils.RequestStatus;
import org.springframework.web.servlet.view.RedirectView;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("jobapplication")
public class JobApplicationController {
    private final JobApplicationServiceImpl jobApplicationService;
    private final UserServiceImpl userService;
    private final User_FilAppFor applicantService;

    @PostMapping("/create")
    public RedirectView createJobApplication(@ModelAttribute JobApplicationDTO jobApplicationDTO,
                                             RedirectAttributes redirectAttributes) throws IOException {

        System.out.println("jobapplication/create");
        // Obtener el usuario del DTO
        User userForm = jobApplicationDTO.getUser();

        System.out.println(userForm.toString());
        // Crear el usuario si no existe
        userService.createUser(userForm);
        System.out.println(userService);
        System.out.println(jobApplicationDTO);
        System.out.println(jobApplicationDTO.getRole());
        System.out.println(jobApplicationDTO.getExperience());

        // Crear una nueva solicitud de trabajo
        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(userForm);
        jobApplication.setExperience(jobApplicationDTO.getExperience());
        jobApplication.setRole(jobApplicationDTO.getRole());
        jobApplication.setStatus(RequestStatus.DRAFT.toString());
        jobApplication.setCountReviewJA(0L);
        // Guardar la solicitud de trabajo en la base de datos
        jobApplicationService.createJobApplication(jobApplication);
        System.out.println(jobApplication);

        jobApplicationDTO.setIdApplication(jobApplication.getUser().getId());
        jobApplicationDTO.setUser(jobApplication.getUser());
        jobApplicationDTO.setStatus(jobApplication.getStatus());
        System.out.println("jobApplication.getUser.getId");
        System.out.println(jobApplication.getUser().getId());
        System.out.println("jobApplication.getUser");
        System.out.println(jobApplication.getUser());
        jobApplicationDTO.setCountReviewJA(0L);

        System.out.println(jobApplicationDTO);

        List<JobApplication> updateApplication = jobApplicationService.findJobApplicationByUser(jobApplication.getUser());
        //jobApplicationDTO.setIdApplication();
        String processId =  applicantService.startProcessInstance(jobApplicationDTO);
        System.out.println("***** PROCESS IDD: "+processId);
        for ( JobApplication application: updateApplication){
            if ("DRAFT".equals(application.getStatus())){
                application.setProcessId(processId);
                jobApplicationService.updateJobApplication(application.getIdApplication(), application);
            }
        }

        return new RedirectView("/", true);
    }

    @PostMapping("/update")
    public String updateJobApplication(@ModelAttribute("JobApplicationDTO") JobApplicationDTO jobApplicationDTO){


        User userForm = jobApplicationDTO.getUser();

        System.out.println(userForm.toString());

        userService.createUser(userForm);
        System.out.println(userService);
        System.out.println(jobApplicationDTO);
        System.out.println(jobApplicationDTO.getRole());
        System.out.println(jobApplicationDTO.getExperience());

        // Crear una nueva solicitud de trabajo
        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(userForm);
        jobApplication.setExperience(jobApplicationDTO.getExperience());
        jobApplication.setRole(jobApplicationDTO.getRole());
        jobApplication.setStatus(RequestStatus.DRAFT.toString());
        jobApplication.setCountReviewJA(0L);
        // Guardar la solicitud de trabajo en la base de datos
        jobApplicationService.createJobApplication(jobApplication);
        System.out.println(jobApplication);

        jobApplicationDTO.setIdApplication(jobApplication.getUser().getId());
        jobApplicationDTO.setUser(jobApplication.getUser());
        jobApplicationDTO.setStatus(jobApplication.getStatus());
        System.out.println("jobApplication.getUser.getId");
        System.out.println(jobApplication.getUser().getId());
        System.out.println("jobApplication.getUser");
        System.out.println(jobApplication.getUser());
        jobApplicationDTO.setCountReviewJA(0L);

        System.out.println(jobApplicationDTO);

        List<JobApplication> updateApplication = jobApplicationService.findJobApplicationByUser(jobApplication.getUser());
        //jobApplicationDTO.setIdApplication();
        String processId =  applicantService.startProcessInstance(jobApplicationDTO);
        System.out.println("***** PROCESS IDD: "+processId);
        for ( JobApplication application: updateApplication){
            if ("DRAFT".equals(application.getStatus())){
                application.setProcessId(processId);
                jobApplicationService.updateJobApplication(application.getIdApplication(), application);
            }
        }

        return "/index";
    }

    @GetMapping("/findbyid/{processId}")
    public JobApplication getJobApplicationByProcessId(@PathVariable String processId){
        return jobApplicationService.getJobApplicationByProcessId(processId);
    }
}
