package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ExerciseCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DifficultyLevel difficulty;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "exercise_muscle_groups", joinColumns = @JoinColumn(name = "exercise_id"))
    @Column(name = "muscle_group", length = 50)
    private Set<String> muscleGroups;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "exercise_equipment", joinColumns = @JoinColumn(name = "exercise_id"))
    @Column(name = "equipment", length = 50)
    private Set<String> equipment;

    @Column(length = 2000)
    private String instructions;

    @Column(length = 500)
    private String tips;

    @Column(length = 500)
    private String warnings;

    @Column(length = 200)
    private String imageUrl;

    @Column(length = 200)
    private String videoUrl;

    @Column
    private Boolean isCustom = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column
    private Boolean isPublic = true;

    @Column
    private Integer usageCount = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Exercise() {}

    public Exercise(String name, String description, ExerciseCategory category, 
                   DifficultyLevel difficulty, Set<String> muscleGroups, Set<String> equipment) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.muscleGroups = muscleGroups;
        this.equipment = equipment;
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

    public ExerciseCategory getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategory category) {
        this.category = category;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public Set<String> getMuscleGroups() {
        return muscleGroups;
    }

    public void setMuscleGroups(Set<String> muscleGroups) {
        this.muscleGroups = muscleGroups;
    }

    public Set<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(Set<String> equipment) {
        this.equipment = equipment;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Boolean getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Boolean isCustom) {
        this.isCustom = isCustom;
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

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
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

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", difficulty=" + difficulty +
                ", muscleGroups=" + muscleGroups +
                ", equipment=" + equipment +
                ", isCustom=" + isCustom +
                ", isPublic=" + isPublic +
                ", usageCount=" + usageCount +
                ", createdAt=" + createdAt +
                '}';
    }

    // Enums
    public enum ExerciseCategory {
        STRENGTH("Siłowy"),
        CARDIO("Kardio"),
        FLEXIBILITY("Rozciąganie"),
        BALANCE("Równowaga"),
        FUNCTIONAL("Funkcjonalny"),
        SPORTS("Sportowy"),
        REHABILITATION("Rehabilitacyjny");

        private final String displayName;

        ExerciseCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DifficultyLevel {
        BEGINNER("Początkujący"),
        INTERMEDIATE("Średniozaawansowany"),
        ADVANCED("Zaawansowany"),
        EXPERT("Ekspert");

        private final String displayName;

        DifficultyLevel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
