package gym.backend.dto;

import java.util.List;
import java.util.Set;

public class WorkoutPlanRequestDTO {
    private String name;
    private String description;
    private String category;
    private String difficulty;
    private Integer estimatedDuration;
    private Set<String> targetMuscleGroups;
    private Set<String> requiredEquipment;
    private Set<String> tags;
    private Boolean isPublic;
    private List<ExerciseInPlanRequest> exercises;

    // Constructors
    public WorkoutPlanRequestDTO() {}

    public WorkoutPlanRequestDTO(String name, String description, String category, String difficulty,
                               Integer estimatedDuration, Set<String> targetMuscleGroups, Set<String> requiredEquipment,
                               Set<String> tags, Boolean isPublic, List<ExerciseInPlanRequest> exercises) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.estimatedDuration = estimatedDuration;
        this.targetMuscleGroups = targetMuscleGroups;
        this.requiredEquipment = requiredEquipment;
        this.tags = tags;
        this.isPublic = isPublic;
        this.exercises = exercises;
    }

    // Getters and Setters
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<ExerciseInPlanRequest> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseInPlanRequest> exercises) {
        this.exercises = exercises;
    }

    // Inner class for Exercise in Plan Request
    public static class ExerciseInPlanRequest {
        private Long exerciseId;
        private Integer sets;
        private String reps;
        private Double weight;
        private Integer duration;
        private Integer restTime;
        private String notes;
        private Integer order;

        // Constructors
        public ExerciseInPlanRequest() {}

        public ExerciseInPlanRequest(Long exerciseId, Integer sets, String reps, Double weight, 
                                   Integer duration, Integer restTime, String notes, Integer order) {
            this.exerciseId = exerciseId;
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
            return "ExerciseInPlanRequest{" +
                    "exerciseId=" + exerciseId +
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
        return "WorkoutPlanRequestDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", estimatedDuration=" + estimatedDuration +
                ", targetMuscleGroups=" + targetMuscleGroups +
                ", requiredEquipment=" + requiredEquipment +
                ", tags=" + tags +
                ", isPublic=" + isPublic +
                ", exercises=" + exercises +
                '}';
    }
}
