package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "workout_plans")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkoutCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DifficultyLevel difficulty;

    @Column
    private Integer estimatedDuration; // in minutes

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "workout_plan_muscle_groups", joinColumns = @JoinColumn(name = "workout_plan_id"))
    @Column(name = "muscle_group", length = 50)
    private Set<String> targetMuscleGroups;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "workout_plan_equipment", joinColumns = @JoinColumn(name = "workout_plan_id"))
    @Column(name = "equipment", length = 50)
    private Set<String> requiredEquipment;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "workout_plan_tags", joinColumns = @JoinColumn(name = "workout_plan_id"))
    @Column(name = "tag", length = 50)
    private Set<String> tags;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<WorkoutPlanExercise> exercises;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column
    private Boolean isPublic = false;

    @Column
    private Boolean isTemplate = false;

    @Column
    private Integer usageCount = 0;

    @Column
    private Integer rating = 0;

    @Column
    private Integer ratingCount = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public WorkoutPlan() {}

    public WorkoutPlan(String name, String description, WorkoutCategory category, 
                      DifficultyLevel difficulty, User createdBy) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.createdBy = createdBy;
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

    public WorkoutCategory getCategory() {
        return category;
    }

    public void setCategory(WorkoutCategory category) {
        this.category = category;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
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

    public List<WorkoutPlanExercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<WorkoutPlanExercise> exercises) {
        this.exercises = exercises;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
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
    public void incrementUsageCount() {
        this.usageCount = (this.usageCount == null ? 0 : this.usageCount) + 1;
    }

    public Double getAverageRating() {
        if (ratingCount == null || ratingCount == 0) {
            return 0.0;
        }
        return (double) rating / ratingCount;
    }

    public int getExerciseCount() {
        return exercises != null ? exercises.size() : 0;
    }

    @Override
    public String toString() {
        return "WorkoutPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", difficulty=" + difficulty +
                ", estimatedDuration=" + estimatedDuration +
                ", createdBy=" + (createdBy != null ? createdBy.getFullName() : "null") +
                ", isPublic=" + isPublic +
                ", isTemplate=" + isTemplate +
                ", usageCount=" + usageCount +
                ", createdAt=" + createdAt +
                '}';
    }

    // Enums
    public enum WorkoutCategory {
        STRENGTH("Siłowy"),
        CARDIO("Kardio"),
        FLEXIBILITY("Rozciąganie"),
        MIXED("Mieszany"),
        HIIT("HIIT"),
        FUNCTIONAL("Funkcjonalny"),
        REHABILITATION("Rehabilitacyjny");

        private final String displayName;

        WorkoutCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DifficultyLevel {
        BEGINNER("Początkujący"),
        INTERMEDIATE("Średniozaawansowany"),
        ADVANCED("Zaawansowany");

        private final String displayName;

        DifficultyLevel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Alias for WorkoutPlanExercise to match service expectations
    public static class ExerciseInPlan extends WorkoutPlanExercise {
        // This class extends WorkoutPlanExercise to provide the expected name
    }
}
