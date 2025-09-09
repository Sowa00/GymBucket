package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_goals")
public class ClientGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type", nullable = false, length = 50)
    private GoalType goalType;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private GoalCategory category;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "target_value")
    private Double targetValue;

    @Column(name = "current_value")
    private Double currentValue;

    @Column(length = 20)
    private String unit;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GoalStatus status = GoalStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Priority priority = Priority.MEDIUM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "progress_notes", length = 1000)
    private String progressNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by_id")
    private User assignedBy;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    // Constructors
    public ClientGoal() {}

    public ClientGoal(Client client, GoalType goalType, String title, User createdBy) {
        this.client = client;
        this.goalType = goalType;
        this.title = title;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }

    public GoalCategory getCategory() {
        return category;
    }

    public void setCategory(GoalCategory category) {
        this.category = category;
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

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public GoalStatus getStatus() {
        return status;
    }

    public void setStatus(GoalStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getProgressNotes() {
        return progressNotes;
    }

    public void setProgressNotes(String progressNotes) {
        this.progressNotes = progressNotes;
    }

    public User getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(User assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Long getClientId() {
        return client != null ? client.getId() : null;
    }

    public Long getAssignedById() {
        return assignedBy != null ? assignedBy.getId() : null;
    }

    public void setClientId(Long clientId) {
        // This method is used by services but doesn't actually set the client
        // The client should be set through the setClient method
    }

    public void setAssignedById(Long assignedById) {
        // This method is used by services but doesn't actually set the assignedBy
        // The assignedBy should be set through the setAssignedBy method
    }

    // Enums
    public enum GoalType {
        WEIGHT_LOSS("Redukcja wagi"),
        MUSCLE_GAIN("Budowanie masy mięśniowej"),
        ENDURANCE("Wytrzymałość"),
        FLEXIBILITY("Elastyczność"),
        STRENGTH("Siła"),
        CARDIO("Kardio"),
        NUTRITION("Żywienie"),
        LIFESTYLE("Styl życia"),
        PERFORMANCE("Wydajność"),
        REHABILITATION("Rehabilitacja");

        private final String displayName;

        GoalType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum GoalStatus {
        ACTIVE("Aktywny"),
        COMPLETED("Zakończony"),
        PAUSED("Wstrzymany"),
        CANCELLED("Anulowany");

        private final String displayName;

        GoalStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Priority {
        LOW("Niski"),
        MEDIUM("Średni"),
        HIGH("Wysoki"),
        URGENT("Pilny");

        private final String displayName;

        Priority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum GoalCategory {
        WEIGHT_LOSS("Utrata wagi"),
        WEIGHT_GAIN("Przyrost wagi"),
        MUSCLE_BUILDING("Budowanie mięśni"),
        ENDURANCE("Wytrzymałość"),
        FLEXIBILITY("Elastyczność"),
        STRENGTH("Siła"),
        CARDIO("Kardio"),
        NUTRITION("Odżywianie"),
        LIFESTYLE("Styl życia"),
        HEALTH("Zdrowie"),
        PERFORMANCE("Wydajność"),
        OTHER("Inne");

        private final String displayName;

        GoalCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Helper methods
    public boolean isActive() {
        return status == GoalStatus.ACTIVE;
    }

    public boolean isCompleted() {
        return status == GoalStatus.COMPLETED;
    }

    public void markAsCompleted() {
        this.status = GoalStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void setCompletedDate(LocalDate completedDate) {
        if (completedDate != null) {
            this.completedAt = completedDate.atStartOfDay();
        }
    }

    public Double getProgressPercentage() {
        if (targetValue == null || currentValue == null || targetValue == 0) {
            return 0.0;
        }
        return Math.min(100.0, (currentValue / targetValue) * 100.0);
    }

    public String getFormattedProgress() {
        return String.format("%.1f%%", getProgressPercentage());
    }

    public boolean isOverdue() {
        if (targetDate == null || isCompleted()) {
            return false;
        }
        return LocalDate.now().isAfter(targetDate);
    }

    public long getDaysRemaining() {
        if (targetDate == null) {
            return -1;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
    }

    public String getFormattedTargetValue() {
        if (targetValue == null) {
            return "Nie ustawiono";
        }
        String unitStr = unit != null ? " " + unit : "";
        return String.format("%.1f%s", targetValue, unitStr);
    }

    public String getFormattedCurrentValue() {
        if (currentValue == null) {
            return "Nie ustawiono";
        }
        String unitStr = unit != null ? " " + unit : "";
        return String.format("%.1f%s", currentValue, unitStr);
    }

    @Override
    public String toString() {
        return "ClientGoal{" +
                "id=" + id +
                ", client=" + (client != null ? client.getFullName() : "null") +
                ", goalType=" + goalType +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", targetValue=" + targetValue +
                ", currentValue=" + currentValue +
                '}';
    }
}
