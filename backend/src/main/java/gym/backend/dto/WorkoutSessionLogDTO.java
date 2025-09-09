package gym.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class WorkoutSessionLogDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long workoutPlanId;
    private String workoutPlanName;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer totalDuration; // in minutes
    private String exercisesCompleted; // JSON string
    private Integer caloriesBurned;
    private String notes;
    private Integer rating; // 1-5 rating of the workout
    private Integer difficultyRating; // 1-5 how difficult it felt
    private Long loggedById;
    private String loggedByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public WorkoutSessionLogDTO() {}

    public WorkoutSessionLogDTO(Long id, Long clientId, String clientName, 
                               Long workoutPlanId, String workoutPlanName, 
                               LocalDate sessionDate) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.workoutPlanId = workoutPlanId;
        this.workoutPlanName = workoutPlanName;
        this.sessionDate = sessionDate;
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

    public Long getWorkoutPlanId() {
        return workoutPlanId;
    }

    public void setWorkoutPlanId(Long workoutPlanId) {
        this.workoutPlanId = workoutPlanId;
    }

    public String getWorkoutPlanName() {
        return workoutPlanName;
    }

    public void setWorkoutPlanName(String workoutPlanName) {
        this.workoutPlanName = workoutPlanName;
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

    public String getLoggedByName() {
        return loggedByName;
    }

    public void setLoggedByName(String loggedByName) {
        this.loggedByName = loggedByName;
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
    public boolean isCompleted() {
        return endTime != null && totalDuration != null;
    }

    public boolean hasRating() {
        return rating != null;
    }

    public boolean hasDifficultyRating() {
        return difficultyRating != null;
    }

    public boolean hasCaloriesBurned() {
        return caloriesBurned != null;
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
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

    public String getFormattedStartTime() {
        if (startTime == null) {
            return "N/A";
        }
        return startTime.toString();
    }

    public String getFormattedEndTime() {
        if (endTime == null) {
            return "N/A";
        }
        return endTime.toString();
    }

    public String getFormattedCaloriesBurned() {
        if (caloriesBurned == null) {
            return "N/A";
        }
        return caloriesBurned + " kcal";
    }

    public String getFormattedRating() {
        if (rating == null) {
            return "N/A";
        }
        return rating + "/5";
    }

    public String getFormattedDifficultyRating() {
        if (difficultyRating == null) {
            return "N/A";
        }
        return difficultyRating + "/5";
    }

    public boolean isToday() {
        return sessionDate != null && sessionDate.equals(LocalDate.now());
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

    public boolean isRecent() {
        if (sessionDate == null) {
            return false;
        }
        return sessionDate.isAfter(LocalDate.now().minusDays(7));
    }

    @Override
    public String toString() {
        return "WorkoutSessionLogDTO{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", workoutPlanName='" + workoutPlanName + '\'' +
                ", sessionDate=" + sessionDate +
                ", totalDuration=" + totalDuration +
                ", rating=" + rating +
                '}';
    }
}
