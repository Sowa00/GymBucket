package gym.backend.dto;

// import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkoutSessionLogRequestDTO {
    
    // @NotNull(message = "Client ID is required")
    private Long clientId;
    
    // @NotNull(message = "Workout plan ID is required")
    private Long workoutPlanId;
    
    // @NotNull(message = "Session date is required")
    private LocalDate sessionDate;
    
    private LocalTime startTime;
    
    private LocalTime endTime;
    
    private Integer totalDuration; // in minutes
    
    private String exercisesCompleted; // JSON string
    
    private Integer caloriesBurned;
    
    private String notes;
    
    private Integer rating; // 1-5 rating of the workout
    
    private Integer difficultyRating; // 1-5 how difficult it felt
    
    // @NotNull(message = "Logged by ID is required")
    private Long loggedById;

    // Constructors
    public WorkoutSessionLogRequestDTO() {}

    public WorkoutSessionLogRequestDTO(Long clientId, Long workoutPlanId, 
                                     LocalDate sessionDate, Long loggedById) {
        this.clientId = clientId;
        this.workoutPlanId = workoutPlanId;
        this.sessionDate = sessionDate;
        this.loggedById = loggedById;
    }

    // Getters and Setters
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getWorkoutPlanId() {
        return workoutPlanId;
    }

    public void setWorkoutPlanId(Long workoutPlanId) {
        this.workoutPlanId = workoutPlanId;
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

    public Long getLoggedById() {
        return loggedById;
    }

    public void setLoggedById(Long loggedById) {
        this.loggedById = loggedById;
    }

    // Helper methods
    public boolean hasStartTime() {
        return startTime != null;
    }

    public boolean hasEndTime() {
        return endTime != null;
    }

    public boolean hasDuration() {
        return totalDuration != null;
    }

    public boolean hasExercisesCompleted() {
        return exercisesCompleted != null && !exercisesCompleted.trim().isEmpty();
    }

    public boolean hasCaloriesBurned() {
        return caloriesBurned != null;
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public boolean hasRating() {
        return rating != null;
    }

    public boolean hasDifficultyRating() {
        return difficultyRating != null;
    }

    public boolean isCompleted() {
        return hasEndTime() && hasDuration();
    }

    public boolean isToday() {
        return sessionDate != null && sessionDate.equals(LocalDate.now());
    }

    public boolean isFuture() {
        return sessionDate != null && sessionDate.isAfter(LocalDate.now());
    }

    public boolean isPast() {
        return sessionDate != null && sessionDate.isBefore(LocalDate.now());
    }

    public boolean isRecent() {
        if (sessionDate == null) {
            return false;
        }
        return sessionDate.isAfter(LocalDate.now().minusDays(7));
    }

    public boolean isThisWeek() {
        if (sessionDate == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return !sessionDate.isBefore(startOfWeek) && !sessionDate.isAfter(endOfWeek);
    }

    public boolean isThisMonth() {
        if (sessionDate == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return sessionDate.getYear() == now.getYear() && 
               sessionDate.getMonth() == now.getMonth();
    }

    public boolean hasValidRating() {
        return rating != null && rating >= 1 && rating <= 5;
    }

    public boolean hasValidDifficultyRating() {
        return difficultyRating != null && difficultyRating >= 1 && difficultyRating <= 5;
    }

    @Override
    public String toString() {
        return "WorkoutSessionLogRequestDTO{" +
                "clientId=" + clientId +
                ", workoutPlanId=" + workoutPlanId +
                ", sessionDate=" + sessionDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", totalDuration=" + totalDuration +
                ", rating=" + rating +
                ", difficultyRating=" + difficultyRating +
                '}';
    }
}
