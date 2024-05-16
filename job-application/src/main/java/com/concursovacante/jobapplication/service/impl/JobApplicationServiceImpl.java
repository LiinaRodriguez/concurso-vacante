package com.concursovacante.jobapplication.service.impl;

import com.concursovacante.jobapplication.model.JobApplication;
import com.concursovacante.jobapplication.model.User;
import com.concursovacante.jobapplication.repository.IJobApplicationRepository;
import com.concursovacante.jobapplication.service.IJobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements IJobApplicationService {

    private final IJobApplicationRepository jobApplicationRepository;

    @Override
    public List<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }
    @Override
    public JobApplication getJobApplicationById(Long id) {
        Optional<JobApplication> optionalJobApplication = jobApplicationRepository.findById(id);
        return optionalJobApplication.orElse(null);
    }

    @Override
    public JobApplication createJobApplication(JobApplication jobApplication) {
        return jobApplicationRepository.save(jobApplication);
    }

    @Override
    public JobApplication updateJobApplication(Long id, JobApplication jobApplication) {
        if (jobApplicationRepository.existsById(id)) {
            jobApplication.setIdApplication(id);
            return jobApplicationRepository.save(jobApplication);
        }
        return null;
    }

    @Override
    public void deleteJobApplication(Long id) {
        jobApplicationRepository.deleteById(id);
    }

    @Override
    public List<JobApplication> findJobApplicationByUser(User user) {
        return  jobApplicationRepository.findByUser(user);
    }

    @Override
    public JobApplication getJobApplicationByProcessId(String processId) {
        return jobApplicationRepository.findByProcessId(processId);
    }
}
