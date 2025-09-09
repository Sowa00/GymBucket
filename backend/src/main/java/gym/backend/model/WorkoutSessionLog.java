package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "workout_session_logs")
public class WorkoutSessionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @Column(name = "session_date", nullable = false)
    private LocalDate sessionDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "total_duration")
    private Integer totalDuration; // in minutes

    @Column(name = "exercises_completed", columnDefinition = "JSON")
    private String exercisesCompleted; // JSON array of completed exercises with sets/reps

    @Column(name = "calories_burned")
    private Integer caloriesBurned;

    @Column(length = 1000)
    private String notes;

    @Column
    private Integer rating; // 1-5 rating of the workout

    @Column(name = "difficulty_rating")
    private Integer difficultyRating; // 1-5 how difficult it felt

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logged_by", nullable = false)
    private User loggedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public WorkoutSessionLog() {}

    public WorkoutSessionLog(Client client, WorkoutPlan workoutPlan, LocalDate sessionDate, User loggedBy) {
        this.client = client;
        this.workoutPlan = workoutPlan;
        this.sessionDate = sessionDate;
        this.loggedBy = loggedBy;
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

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getExercisesCompleted() {
        return exercisesCompleted;
    }

    public void setExercisesCompleted(String exercisesCompleted) {
        this.exercisesCompleted = exercisesCompleted;
    }

    public Integer getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(Integer caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getDifficultyRating() {
        return difficultyRating;
    }

    public void setDifficultyRating(Integer difficultyRating) {
        this.difficultyRating = difficultyRating;
    }

    public User getLoggedBy() {
        return loggedBy;
    }

    public void setLoggedBy(User loggedBy) {
        this.loggedBy = loggedBy;
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
    public void calculateDuration() {
        if (startTime != null && endTime != null) {
            this.totalDuration = (int) java.time.Duration.between(startTime, endTime).toMinutes();
        }
    }

    public boolean isCompleted() {
        return endTime != null && totalDuration != null;
    }

    public String getFormattedDuration() {
        if (totalDuration == null) {
            return "0 min";
        }
        
        int hours = totalDuration / 60;
        int minutes = totalDuration % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%d min", minutes);
        }
    }

    public Double getAverageRating() {
        if (rating == null) {
            return null;
        }
        return rating.doubleValue();
    }

    // Additional methods needed by WorkoutSessionLogService
    public Long getClientId() {
        return client != null ? client.getId() : null;
    }

    public void setClientId(Long clientId) {
        // This method is used by services but doesn't actually set the client
        // The client should be set through the setClient method
    }

    public Long getWorkoutPlanId() {
        return workoutPlan != null ? workoutPlan.getId() : null;
    }

    public void setWorkoutPlanId(Long workoutPlanId) {
        // This method is used by services but doesn't actually set the workout plan
        // The workout plan should be set through the setWorkoutPlan method
    }

    public Long getLoggedById() {
        return loggedBy != null ? loggedBy.getId() : null;
    }

    public void setLoggedById(Long loggedById) {
        // This method is used by services but doesn't actually set the loggedBy
        // The loggedBy should be set through the setLoggedBy method
    }

    @Override
    public String toString() {
        return "WorkoutSessionLog{" +
                "id=" + id +
                ", client=" + (client != null ? client.getFullName() : "null") +
                ", workoutPlan=" + (workoutPlan != null ? workoutPlan.getName() : "null") +
                ", sessionDate=" + sessionDate +
                ", totalDuration=" + totalDuration +
                ", rating=" + rating +
                '}';
    }
}
