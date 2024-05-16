package com.concursovacante.jobapplication.service.impl;

import com.concursovacante.jobapplication.dto.JobApplicationDTO;
import com.concursovacante.jobapplication.dto.TaskInfoDTO;
import com.concursovacante.jobapplication.model.JobApplication;
import com.concursovacante.jobapplication.service.IApplicantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class User_FilAppFor implements IApplicantService {
    private final RestTemplate restTemplate;
    private final JobApplicationServiceImpl jobApplicationService;
    @Value("${camunda.url:http://localhost:9000/engine-rest/}")
    private String camundaUrl;
    private List<TaskInfoDTO> tasksList = new ArrayList<>();
    public String startProcessInstance(JobApplicationDTO jobApplicationDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println(jobApplicationDTO);
        String fullName = jobApplicationDTO.getUser().getFullName();
        String email = jobApplicationDTO.getUser().getEmail();
        String phoneNumber = jobApplicationDTO.getUser().getPhoneNumber();
        String education = jobApplicationDTO.getUser().getEducation();
        String experience = jobApplicationDTO.getExperience();
        String role = jobApplicationDTO.getRole();

            Map<String, Object> variables = new HashMap<>();
            variables.put("idApplication", Map.of("value", jobApplicationDTO.getIdApplication(), "type", "Long"));
            variables.put("fullName", Map.of("value", fullName, "type", "String"));
            variables.put("email", Map.of("value", email, "type", "String"));
            // variables.put("phoneNumber", Map.of("value", phoneNumber, "type", "String"));
            variables.put("education", Map.of("value", education, "type", "String"));
            variables.put("experience", Map.of("value", jobApplicationDTO.getExperience(), "type", "Long"));
            variables.put("role", Map.of("value", jobApplicationDTO.getRole(), "type", "String"));
            //variables.put("curriculum", Map.of("value", jobApplicationDTO.getCurriculum(), "type", "String"));
            variables.put("countReviewsBpm", Map.of("value", 0, "type", "Long"));
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("variables", variables);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            try {
                ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:9000/engine-rest/" + "process-definition/key/Concurso_Vacante/start", requestEntity, Map.class);
                String processId = String.valueOf(response.getBody().get("id"));
                System.out.println("processID "+ processId);
                TaskInfoDTO taskInfo = getTaskInfoByProcessIdWithApi(processId);
                if (taskInfo != null) {
                    setAssignee(taskInfo.getTaskId(), "user");
                    taskInfo.setProcessId(processId);
                    return processId;
                } else {
                    System.err.println("TaskInfo is null for processId: " + processId);

                    return null;
                }

            } catch (HttpClientErrorException e) {
                return e.getResponseBodyAsString();
            }
    }

    public void setAssignee(String taskId, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", userId);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(camundaUrl+"/task/"+taskId+"/assignee", HttpMethod.POST, requestEntity, String.class);
            System.out.println("Assignee set successfully");
            System.out.println("*****Asignee : "+ userId);
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error in the Camunda request: " + errorMessage);
        }
    }

    public TaskInfoDTO getTaskInfoByProcessId(String processId) {

        try {
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl+"/task?processInstanceId="+processId, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {
            });

            List<Map> tasks = response.getBody();
            if (tasks != null && !tasks.isEmpty()) {
                Map<String, String> taskInfoMap = new HashMap<>();
                taskInfoMap.put("taskId", String.valueOf(tasks.get(0).get("id")));
                taskInfoMap.put("taskName", String.valueOf(tasks.get(0).get("name")));
                taskInfoMap.put("assignee", String.valueOf(tasks.get(0).get("assignee")));

                System.out.println("Task Info for Process ID " + processId + ":");
                System.out.println("Task ID: " + taskInfoMap.get("taskId"));
                System.out.println("Task Name: " + taskInfoMap.get("taskName"));
                System.out.println("Assignee: " + taskInfoMap.get("assignee"));

                TaskInfoDTO taskInfo = new TaskInfoDTO();
                taskInfo.setProcessId(processId);
                taskInfo.setTaskId(taskInfoMap.get("taskId"));
                taskInfo.setTaskName(taskInfoMap.get("taskName"));
                taskInfo.setTaskAssignee(taskInfoMap.get("assignee"));

                tasksList.add(taskInfo);
                return taskInfo;
            } else {
                System.err.println("No tasks found for Process ID " + processId);
                return null;
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error with Camunda request: " + errorMessage);
            return null;
        }
    }

    public TaskInfoDTO getTaskInfoByProcessIdWithApi(String processId) {
        try {
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl+"task?processInstanceId="+processId, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {
            });
            List<Map> tasks = response.getBody();

            if (tasks != null && !tasks.isEmpty()) {
                TaskInfoDTO taskInfo = new TaskInfoDTO();
                taskInfo.setTaskId(String.valueOf(tasks.get(0).get("id")));
                taskInfo.setTaskName(String.valueOf(tasks.get(0).get("name")));
                taskInfo.setTaskAssignee(String.valueOf(tasks.get(0).get("assignee")));

                return taskInfo;
            } else {
                System.err.println("No tasks found for Process ID " + processId);
                return null;
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error with Camunda request: " + errorMessage);
            return null;
        }
    }

    public void completeTask(String processId) {

        TaskInfoDTO taskInfoDTO = getTaskInfoByProcessId(processId);

        if(taskInfoDTO!= null){
            String taskId = taskInfoDTO.getTaskId();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            try{
                ResponseEntity<Map> response = restTemplate.postForEntity("http://localhost:9000/engine-rest"+"/task/"+taskId+"/complete", requestEntity, Map.class);

                String subProcessId = getSubProcessInstanceById(processId);
                if(subProcessId != null){
                    TaskInfoDTO taskInfoApi = getTaskInfoByProcessIdWithApi(subProcessId);
                    setAssignee(taskInfoApi.getTaskId(), "rrhh");
                    updateReviewAndStatus(processId, subProcessId,"Revisar Vacante");

                    JobApplication jobApplication = jobApplicationService.getJobApplicationByProcessId(subProcessId);
                    jobApplication.setStatus(taskInfoApi.getTaskName());
                    jobApplicationService.updateJobApplication(jobApplication.getIdApplication(), jobApplication);

                }else{
                    System.out.println("Vacante cerrada");
                }

            } catch (HttpClientErrorException e) {
                String errorMessage = e.getResponseBodyAsString();
                System.err.println("Error with Camunda request: " + errorMessage);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Could not get task information for process ID: " + processId);

        }

    }

    public void messageEvent(String processId) {
        String messageName = "hayIncosistencias";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("inconsistenciasSubsanadas", Map.of("value", true, "type", "boolean"));

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("messageName", messageName);
        requestBodyMap.put("businessKey", processId);
        requestBodyMap.put("processVariables", processVariables);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestBodyMap);
        } catch (JsonProcessingException e) {
            System.err.println("error converting request body to JSON");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(camundaUrl+"/message", HttpMethod.POST, requestEntity, String.class);
            System.out.println("Message event done. BusinessID: "+processId);
            updateReviewAndStatus(processId, processId,"Revisar detalles de solicitud");
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error with Camunda request: " + errorMessage);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getCountReview(String processId) {
        JobApplication jobApplication = jobApplicationService.getJobApplicationByProcessId(processId);
        return jobApplication.getCountReviewJA();
    }

    public void updateReviewAndStatus(String processId, String subProcessId, String status) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/job_application", "lina", "comingback");

        String updateQuery = "UPDATE jobapplication SET status = ?, process_id = ? WHERE process_id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(1, status);
            updateStatement.setString(2, subProcessId);
            updateStatement.setString(3, processId);

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Status updated, and count_reviewja incremented.");
            } else {
                System.out.println("No records found for the given processId: " + processId);
            }
        }
    }

    public String getSubProcessInstanceById(String processId){
        try {
            ResponseEntity<List<Map>> response = restTemplate
                    .exchange(camundaUrl+"process-instance?superProcessInstance="+processId, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {
            });

            List<Map> subProcessInstance = response.getBody();

            if (subProcessInstance != null && !subProcessInstance.isEmpty()) {
                processId = String.valueOf(subProcessInstance.get(0).get("id"));
                return processId;
            } else {
                System.err.println("subprocess not found for: " + processId);
                return null;
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error with Camunda request: " + errorMessage);
            return null;
        }

    }

}
