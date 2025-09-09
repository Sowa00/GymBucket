package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_workout_assignments")
public class ClientWorkoutAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by", nullable = false)
    private User assignedBy;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssignmentStatus status = AssignmentStatus.ACTIVE;

    @Column(length = 500)
    private String notes;

    @Column(name = "progress_notes", length = 1000)
    private String progressNotes;

    @Column(name = "completion_percentage")
    private Integer completionPercentage = 0;

    @Column(name = "last_workout_date")
    private LocalDate lastWorkoutDate;

    @Column(name = "total_workouts_completed")
    private Integer totalWorkoutsCompleted = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public ClientWorkoutAssignment() {}

    public ClientWorkoutAssignment(Client client, WorkoutPlan workoutPlan, User assignedBy, 
                                  LocalDate startDate) {
        this.client = client;
        this.workoutPlan = workoutPlan;
        this.assignedBy = assignedBy;
        this.assignedDate = LocalDate.now();
        this.startDate = startDate;
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

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public User getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(User assignedBy) {
        this.assignedBy = assignedBy;
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

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
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

    public LocalDate getLastWorkoutDate() {
        return lastWorkoutDate;
    }

    public void setLastWorkoutDate(LocalDate lastWorkoutDate) {
        this.lastWorkoutDate = lastWorkoutDate;
    }

    public Integer getTotalWorkoutsCompleted() {
        return totalWorkoutsCompleted;
    }

    public void setTotalWorkoutsCompleted(Integer totalWorkoutsCompleted) {
        this.totalWorkoutsCompleted = totalWorkoutsCompleted;
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

    // Enums
    public enum AssignmentStatus {
        ACTIVE("Aktywne"),
        COMPLETED("Zakończone"),
        PAUSED("Wstrzymane"),
        CANCELLED("Anulowane");

        private final String displayName;

        AssignmentStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Helper methods
    public boolean isActive() {
        return status == AssignmentStatus.ACTIVE;
    }

    public boolean isCompleted() {
        return status == AssignmentStatus.COMPLETED;
    }

    public void markAsCompleted() {
        this.status = AssignmentStatus.COMPLETED;
        this.endDate = LocalDate.now();
    }

    public void incrementWorkoutsCompleted() {
        this.totalWorkoutsCompleted = (this.totalWorkoutsCompleted == null ? 0 : this.totalWorkoutsCompleted) + 1;
        this.lastWorkoutDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return "ClientWorkoutAssignment{" +
                "id=" + id +
                ", client=" + (client != null ? client.getFullName() : "null") +
                ", workoutPlan=" + (workoutPlan != null ? workoutPlan.getName() : "null") +
                ", status=" + status +
                ", completionPercentage=" + completionPercentage +
                ", startDate=" + startDate +
                '}';
    }

    // Additional methods needed by services
    public Long getClientId() {
        return client != null ? client.getId() : null;
    }

    public Long getWorkoutPlanId() {
        return workoutPlan != null ? workoutPlan.getId() : null;
    }

    public Long getAssignedById() {
        return assignedBy != null ? assignedBy.getId() : null;
    }

    public void setClientId(Long clientId) {
        // This method is used by services but doesn't actually set the client
        // The client should be set through the setClient method
    }

    public void setWorkoutPlanId(Long workoutPlanId) {
        // This method is used by services but doesn't actually set the workout plan
        // The workout plan should be set through the setWorkoutPlan method
    }

    public void setAssignedById(Long assignedById) {
        // This method is used by services but doesn't actually set the assignedBy
        // The assignedBy should be set through the setAssignedBy method
    }
}
