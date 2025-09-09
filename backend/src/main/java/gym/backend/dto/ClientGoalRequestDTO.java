package gym.backend.dto;

import gym.backend.model.ClientGoal;

import java.time.LocalDate;

public class ClientGoalRequestDTO {
    
    private Long clientId;
    
    private String title;
    
    private String description;
    
    private ClientGoal.GoalType goalType;
    
    private ClientGoal.GoalCategory category;
    
    private ClientGoal.GoalStatus status = ClientGoal.GoalStatus.ACTIVE;
    
    private LocalDate startDate;
    
    private LocalDate targetDate;
    
    private Double targetValue;
    
    private String unit;
    
    private String progressNotes;
    
    private Long assignedById;
    
    private Integer priority = 1;
    
    private Boolean isPublic = false;

    // Constructors
    public ClientGoalRequestDTO() {}

    public ClientGoalRequestDTO(Long clientId, String title, ClientGoal.GoalType goalType, 
                               LocalDate startDate, LocalDate targetDate) {
        this.clientId = clientId;
        this.title = title;
        this.goalType = goalType;
        this.startDate = startDate;
        this.targetDate = targetDate;
    }

    // Getters and Setters
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientGoal.GoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(ClientGoal.GoalType goalType) {
        this.goalType = goalType;
    }

    public ClientGoal.GoalCategory getCategory() {
        return category;
    }

    public void setCategory(ClientGoal.GoalCategory category) {
        this.category = category;
    }

    public ClientGoal.GoalStatus getStatus() {
        return status;
    }

    public void setStatus(ClientGoal.GoalStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getProgressNotes() {
        return progressNotes;
    }

    public void setProgressNotes(String progressNotes) {
        this.progressNotes = progressNotes;
    }

    public Long getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(Long assignedById) {
        this.assignedById = assignedById;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    // Helper methods
    public boolean isActive() {
        return status == ClientGoal.GoalStatus.ACTIVE;
    }

    public boolean isCompleted() {
        return status == ClientGoal.GoalStatus.COMPLETED;
    }

    public boolean isPaused() {
        return status == ClientGoal.GoalStatus.PAUSED;
    }

    public boolean isCancelled() {
        return status == ClientGoal.GoalStatus.CANCELLED;
    }

    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public boolean hasProgressNotes() {
        return progressNotes != null && !progressNotes.trim().isEmpty();
    }

    public boolean hasTargetValue() {
        return targetValue != null;
    }

    public boolean hasUnit() {
        return unit != null && !unit.trim().isEmpty();
    }

    public boolean isPublic() {
        return isPublic != null && isPublic;
    }

    public boolean isPrivate() {
        return !isPublic();
    }

    public boolean hasValidStartDate() {
        return startDate != null;
    }

    public boolean hasValidTargetDate() {
        return targetDate != null;
    }

    public boolean hasValidTargetValue() {
        return targetValue != null && targetValue > 0;
    }

    public boolean hasValidPriority() {
        return priority != null && priority >= 1 && priority <= 5;
    }

    public boolean isOverdue() {
        if (targetDate == null || isCompleted()) {
            return false;
        }
        return LocalDate.now().isAfter(targetDate);
    }

    public boolean isDueSoon() {
        if (targetDate == null || isCompleted()) {
            return false;
        }
        return targetDate.isBefore(LocalDate.now().plusDays(7));
    }

    public long getDaysRemaining() {
        if (targetDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
    }

    public long getDaysSinceStart() {
        if (startDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, LocalDate.now());
    }

    public boolean isLongTerm() {
        if (targetDate == null || startDate == null) {
            return false;
        }
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, targetDate);
        return days > 90;
    }

    public boolean isShortTerm() {
        if (targetDate == null || startDate == null) {
            return false;
        }
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, targetDate);
        return days <= 30;
    }

    public boolean isMediumTerm() {
        if (targetDate == null || startDate == null) {
            return false;
        }
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, targetDate);
        return days > 30 && days <= 90;
    }

    public boolean hasValidDateRange() {
        if (startDate == null || targetDate == null) {
            return false;
        }
        return !targetDate.isBefore(startDate);
    }

    public boolean isFuture() {
        if (startDate == null) {
            return false;
        }
        return startDate.isAfter(LocalDate.now());
    }

    public boolean isPast() {
        if (targetDate == null) {
            return false;
        }
        return targetDate.isBefore(LocalDate.now());
    }

    public boolean isCurrent() {
        if (startDate == null || targetDate == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return !now.isBefore(startDate) && !now.isAfter(targetDate);
    }

    @Override
    public String toString() {
        return "ClientGoalRequestDTO{" +
                "clientId=" + clientId +
                ", title='" + title + '\'' +
                ", goalType=" + goalType +
                ", status=" + status +
                ", startDate=" + startDate +
                ", targetDate=" + targetDate +
                ", targetValue=" + targetValue +
                '}';
    }
}
