package gym.backend.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class ExerciseDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private String difficulty;
    private Set<String> muscleGroups;
    private Set<String> equipment;
    private String instructions;
    private String tips;
    private String warnings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ExerciseDTO() {}

    public ExerciseDTO(Long id, String name, String description, String category, String difficulty,
                      Set<String> muscleGroups, Set<String> equipment, String instructions,
                      String tips, String warnings, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.muscleGroups = muscleGroups;
        this.equipment = equipment;
        this.instructions = instructions;
        this.tips = tips;
        this.warnings = warnings;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    @Override
    public String toString() {
        return "ExerciseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", muscleGroups=" + muscleGroups +
                ", equipment=" + equipment +
                ", instructions='" + instructions + '\'' +
                ", tips='" + tips + '\'' +
                ", warnings='" + warnings + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
