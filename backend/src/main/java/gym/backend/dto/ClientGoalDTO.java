package gym.backend.dto;

import gym.backend.model.ClientGoal;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientGoalDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private String title;
    private String description;
    private ClientGoal.GoalType goalType;
    private ClientGoal.GoalCategory category;
    private ClientGoal.GoalStatus status;
    private LocalDate startDate;
    private LocalDate targetDate;
    private LocalDate completedDate;
    private Double targetValue;
    private Double currentValue;
    private String unit;
    private String progressNotes;
    private Long assignedById;
    private String assignedByName;
    private Integer priority;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ClientGoalDTO() {}

    public ClientGoalDTO(Long id, Long clientId, String clientName, String title, 
                        ClientGoal.GoalType goalType, ClientGoal.GoalStatus status) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.title = title;
        this.goalType = goalType;
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

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDate completedDate) {
        this.completedDate = completedDate;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
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

    public String getAssignedByName() {
        return assignedByName;
    }

    public void setAssignedByName(String assignedByName) {
        this.assignedByName = assignedByName;
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

    public boolean hasCurrentValue() {
        return currentValue != null;
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

    public String getFormattedTargetValue() {
        if (targetValue == null) {
            return "N/A";
        }
        if (hasUnit()) {
            return String.format("%.1f %s", targetValue, unit);
        }
        return String.format("%.1f", targetValue);
    }

    public String getFormattedCurrentValue() {
        if (currentValue == null) {
            return "N/A";
        }
        if (hasUnit()) {
            return String.format("%.1f %s", currentValue, unit);
        }
        return String.format("%.1f", currentValue);
    }

    public Double getProgressPercentage() {
        if (targetValue == null || currentValue == null || targetValue == 0) {
            return 0.0;
        }
        return Math.min((currentValue / targetValue) * 100, 100.0);
    }

    public String getFormattedProgressPercentage() {
        return String.format("%.1f%%", getProgressPercentage());
    }

    public String getFormattedStartDate() {
        if (startDate == null) {
            return "N/A";
        }
        return startDate.toString();
    }

    public String getFormattedTargetDate() {
        if (targetDate == null) {
            return "N/A";
        }
        return targetDate.toString();
    }

    public String getFormattedCompletedDate() {
        if (completedDate == null) {
            return "N/A";
        }
        return completedDate.toString();
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

    public boolean isRecent() {
        if (startDate == null) {
            return false;
        }
        return startDate.isAfter(LocalDate.now().minusDays(30));
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

    public String getStatusIcon() {
        if (status == null) {
            return "📝";
        }
        
        switch (status) {
            case ACTIVE:
                return "🎯";
            case COMPLETED:
                return "✅";
            case PAUSED:
                return "⏸️";
            case CANCELLED:
                return "❌";
            default:
                return "📝";
        }
    }

    public String getStatusColor() {
        if (status == null) {
            return "blue";
        }
        
        switch (status) {
            case ACTIVE:
                return "green";
            case COMPLETED:
                return "blue";
            case PAUSED:
                return "orange";
            case CANCELLED:
                return "red";
            default:
                return "blue";
        }
    }

    @Override
    public String toString() {
        return "ClientGoalDTO{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", title='" + title + '\'' +
                ", goalType=" + goalType +
                ", status=" + status +
                ", targetValue=" + targetValue +
                ", currentValue=" + currentValue +
                '}';
    }
}
