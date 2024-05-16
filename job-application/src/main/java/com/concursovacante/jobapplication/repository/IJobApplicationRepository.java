package com.concursovacante.jobapplication.repository;

import com.concursovacante.jobapplication.model.JobApplication;
import com.concursovacante.jobapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUser(User user);
    JobApplication findByProcessId(String processId);
}
