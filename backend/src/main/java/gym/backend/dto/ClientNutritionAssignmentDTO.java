package gym.backend.dto;

import gym.backend.model.ClientNutritionAssignment;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientNutritionAssignmentDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long nutritionPlanId;
    private String nutritionPlanName;
    private Long assignedById;
    private String assignedByName;
    private LocalDate assignedDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private ClientNutritionAssignment.AssignmentStatus status;
    private String notes;
    private String progressNotes;
    private Integer completionPercentage;
    private LocalDate lastMealLoggedDate;
    private Integer totalMealsLogged;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ClientNutritionAssignmentDTO() {}

    public ClientNutritionAssignmentDTO(Long id, Long clientId, String clientName, 
                                      Long nutritionPlanId, String nutritionPlanName, 
                                      ClientNutritionAssignment.AssignmentStatus status) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.nutritionPlanId = nutritionPlanId;
        this.nutritionPlanName = nutritionPlanName;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getNutritionPlanId() {
        return nutritionPlanId;
    }

    public void setNutritionPlanId(Long nutritionPlanId) {
        this.nutritionPlanId = nutritionPlanId;
    }

    public String getNutritionPlanName() {
        return nutritionPlanName;
    }

    public void setNutritionPlanName(String nutritionPlanName) {
        this.nutritionPlanName = nutritionPlanName;
    }

    public Long getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(Long assignedById) {
        this.assignedById = assignedById;
    }

    public String getAssignedByName() {
        return assignedByName;
    }

    public void setAssignedByName(String assignedByName) {
        this.assignedByName = assignedByName;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDate assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ClientNutritionAssignment.AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(ClientNutritionAssignment.AssignmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getProgressNotes() {
        return progressNotes;
    }

    public void setProgressNotes(String progressNotes) {
        this.progressNotes = progressNotes;
    }

    public Integer getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(Integer completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public LocalDate getLastMealLoggedDate() {
        return lastMealLoggedDate;
    }

    public void setLastMealLoggedDate(LocalDate lastMealLoggedDate) {
        this.lastMealLoggedDate = lastMealLoggedDate;
    }

    public Integer getTotalMealsLogged() {
        return totalMealsLogged;
    }

    public void setTotalMealsLogged(Integer totalMealsLogged) {
        this.totalMealsLogged = totalMealsLogged;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public boolean isActive() {
        return status == ClientNutritionAssignment.AssignmentStatus.ACTIVE;
    }

    public boolean isCompleted() {
        return status == ClientNutritionAssignment.AssignmentStatus.COMPLETED;
    }

    public boolean isPaused() {
        return status == ClientNutritionAssignment.AssignmentStatus.PAUSED;
    }

    public boolean isCancelled() {
        return status == ClientNutritionAssignment.AssignmentStatus.CANCELLED;
    }

    public String getFormattedCompletionPercentage() {
        if (completionPercentage == null) {
            return "0%";
        }
        return completionPercentage + "%";
    }

    public String getFormattedDuration() {
        if (startDate == null) {
            return "N/A";
        }
        
        LocalDate endDateToUse = endDate != null ? endDate : LocalDate.now();
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDateToUse);
        
        if (days < 7) {
            return days + " dni";
        } else if (days < 30) {
            long weeks = days / 7;
            return weeks + " tygodni";
        } else {
            long months = days / 30;
            return months + " miesięcy";
        }
    }

    public boolean hasRecentActivity() {
        if (lastMealLoggedDate == null) {
            return false;
        }
        return lastMealLoggedDate.isAfter(LocalDate.now().minusDays(7));
    }

    public boolean isOverdue() {
        if (endDate == null || isCompleted()) {
            return false;
        }
        return LocalDate.now().isAfter(endDate);
    }

    @Override
    public String toString() {
        return "ClientNutritionAssignmentDTO{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", nutritionPlanName='" + nutritionPlanName + '\'' +
                ", status=" + status +
                ", completionPercentage=" + completionPercentage +
                ", startDate=" + startDate +
                '}';
    }
}
