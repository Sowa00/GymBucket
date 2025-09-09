package gym.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class WorkoutPlanDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String difficulty;
    private Integer estimatedDuration;
    private Set<String> targetMuscleGroups;
    private Set<String> requiredEquipment;
    private Set<String> tags;
    private String createdBy;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ExerciseInPlanDTO> exercises;

    // Constructors
    public WorkoutPlanDTO() {}

    public WorkoutPlanDTO(Long id, String name, String description, String category, String difficulty,
                         Integer estimatedDuration, Set<String> targetMuscleGroups, Set<String> requiredEquipment,
                         Set<String> tags, String createdBy, Boolean isPublic, LocalDateTime createdAt, 
                         LocalDateTime updatedAt, List<ExerciseInPlanDTO> exercises) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.estimatedDuration = estimatedDuration;
        this.targetMuscleGroups = targetMuscleGroups;
        this.requiredEquipment = requiredEquipment;
        this.tags = tags;
        this.createdBy = createdBy;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.exercises = exercises;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(Integer estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Set<String> getTargetMuscleGroups() {
        return targetMuscleGroups;
    }

    public void setTargetMuscleGroups(Set<String> targetMuscleGroups) {
        this.targetMuscleGroups = targetMuscleGroups;
    }

    public Set<String> getRequiredEquipment() {
        return requiredEquipment;
    }

    public void setRequiredEquipment(Set<String> requiredEquipment) {
        this.requiredEquipment = requiredEquipment;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public List<ExerciseInPlanDTO> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseInPlanDTO> exercises) {
        this.exercises = exercises;
    }

    // Inner class for Exercise in Plan
    public static class ExerciseInPlanDTO {
        private Long exerciseId;
        private String exerciseName;
        private Integer sets;
        private String reps;
        private Double weight;
        private Integer duration;
        private Integer restTime;
        private String notes;
        private Integer order;

        // Constructors
        public ExerciseInPlanDTO() {}

        public ExerciseInPlanDTO(Long exerciseId, String exerciseName, Integer sets, String reps, 
                               Double weight, Integer duration, Integer restTime, String notes, Integer order) {
            this.exerciseId = exerciseId;
            this.exerciseName = exerciseName;
            this.sets = sets;
            this.reps = reps;
            this.weight = weight;
            this.duration = duration;
            this.restTime = restTime;
            this.notes = notes;
            this.order = order;
        }

        // Getters and Setters
        public Long getExerciseId() {
            return exerciseId;
        }

        public void setExerciseId(Long exerciseId) {
            this.exerciseId = exerciseId;
        }

        public String getExerciseName() {
            return exerciseName;
        }

        public void setExerciseName(String exerciseName) {
            this.exerciseName = exerciseName;
        }

        public Integer getSets() {
            return sets;
        }

        public void setSets(Integer sets) {
            this.sets = sets;
        }

        public String getReps() {
            return reps;
        }

        public void setReps(String reps) {
            this.reps = reps;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public Integer getRestTime() {
            return restTime;
        }

        public void setRestTime(Integer restTime) {
            this.restTime = restTime;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        @Override
        public String toString() {
            return "ExerciseInPlanDTO{" +
                    "exerciseId=" + exerciseId +
                    ", exerciseName='" + exerciseName + '\'' +
                    ", sets=" + sets +
                    ", reps='" + reps + '\'' +
                    ", weight=" + weight +
                    ", duration=" + duration +
                    ", restTime=" + restTime +
                    ", notes='" + notes + '\'' +
                    ", order=" + order +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WorkoutPlanDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", estimatedDuration=" + estimatedDuration +
                ", targetMuscleGroups=" + targetMuscleGroups +
                ", requiredEquipment=" + requiredEquipment +
                ", tags=" + tags +
                ", createdBy='" + createdBy + '\'' +
                ", isPublic=" + isPublic +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", exercises=" + exercises +
                '}';
    }
}
