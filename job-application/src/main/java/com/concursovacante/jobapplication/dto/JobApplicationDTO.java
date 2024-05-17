package com.concursovacante.jobapplication.dto;

import com.concursovacante.jobapplication.model.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class JobApplicationDTO {
    private Long idApplication;
    private String processId;
    private String status;
    private String role;
    private String experience;
    private Long countReviewJA;
    private User user;
}
