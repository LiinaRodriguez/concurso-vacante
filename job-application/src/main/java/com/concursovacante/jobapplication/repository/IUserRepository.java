package com.concursovacante.jobapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.concursovacante.jobapplication.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

}
