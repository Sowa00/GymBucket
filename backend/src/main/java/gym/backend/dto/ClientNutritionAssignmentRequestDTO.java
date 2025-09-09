package gym.backend.dto;

import gym.backend.model.ClientNutritionAssignment;
// import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ClientNutritionAssignmentRequestDTO {
    
    // @NotNull(message = "Client ID is required")
    private Long clientId;
    
    // @NotNull(message = "Nutrition plan ID is required")
    private Long nutritionPlanId;
    
    // @NotNull(message = "Assigned by ID is required")
    private Long assignedById;
    
    // @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private ClientNutritionAssignment.AssignmentStatus status = ClientNutritionAssignment.AssignmentStatus.ACTIVE;
    
    private String notes;
    
    private String progressNotes;
    
    private Integer completionPercentage = 0;

    // Constructors
    public ClientNutritionAssignmentRequestDTO() {}

    public ClientNutritionAssignmentRequestDTO(Long clientId, Long nutritionPlanId, 
                                             Long assignedById, LocalDate startDate) {
        this.clientId = clientId;
        this.nutritionPlanId = nutritionPlanId;
        this.assignedById = assignedById;
        this.startDate = startDate;
    }

    // Getters and Setters
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getNutritionPlanId() {
        return nutritionPlanId;
    }

    public void setNutritionPlanId(Long nutritionPlanId) {
        this.nutritionPlanId = nutritionPlanId;
    }

    public Long getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(Long assignedById) {
        this.assignedById = assignedById;
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

    // Helper methods
    public boolean hasEndDate() {
        return endDate != null;
    }

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

    public long getDurationInDays() {
        if (startDate == null) {
            return 0;
        }
        
        LocalDate endDateToUse = endDate != null ? endDate : LocalDate.now();
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDateToUse);
    }

    public boolean isLongTerm() {
        return getDurationInDays() > 30;
    }

    public boolean isShortTerm() {
        return getDurationInDays() <= 7;
    }

    @Override
    public String toString() {
        return "ClientNutritionAssignmentRequestDTO{" +
                "clientId=" + clientId +
                ", nutritionPlanId=" + nutritionPlanId +
                ", assignedById=" + assignedById +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}
