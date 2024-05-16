package com.concursovacante.jobapplication.service;

import com.concursovacante.jobapplication.model.JobApplication;
import com.concursovacante.jobapplication.model.User;


import java.util.List;

public interface IJobApplicationService {
    public List<JobApplication> getAllJobApplications();

    JobApplication getJobApplicationById(Long id);

    JobApplication createJobApplication(JobApplication jobApplication);
    JobApplication updateJobApplication(Long id, JobApplication jobApplication);

    void deleteJobApplication(Long id);

    List<JobApplication> findJobApplicationByUser(User user);
    JobApplication getJobApplicationByProcessId(String processId);
    //find first by application date(?) or by application role //  experience//
}
