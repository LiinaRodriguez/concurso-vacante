package com.concursovacante.jobapplication.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GenerationType;

import lombok.Data;

@Entity
@Table(name="jobapplication")
@Data
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idApplication")
    private Long idApplication;

    @Column(name = "processId")
    private String processId;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

    @Column(name = "experience")
    private String experience;

    @Column(name = "countReviewJA")
    private Long countReviewJA;

    @JoinColumn(name = "fk_user")
    @OneToOne
    private User user;
}
