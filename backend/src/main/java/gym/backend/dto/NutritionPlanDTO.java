package gym.backend.dto;

import gym.backend.model.NutritionPlan;

import java.time.LocalDateTime;
import java.util.List;

public class NutritionPlanDTO {
    private Long id;
    private String name;
    private String description;
    private NutritionPlan.NutritionCategory category;
    private NutritionPlan.DifficultyLevel difficulty;
    private Integer targetCalories;
    private Integer targetProtein;
    private Integer targetCarbs;
    private Integer targetFat;
    private Integer duration;
    private String createdBy;
    private Boolean isPublic;
    private Boolean isTemplate;
    private Integer usageCount;
    private Integer rating;
    private Integer ratingCount;
    private Double averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<NutritionPlanMealDTO> meals;

    // Constructors
    public NutritionPlanDTO() {}

    public NutritionPlanDTO(Long id, String name, String description, NutritionPlan.NutritionCategory category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
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

    public NutritionPlan.NutritionCategory getCategory() {
        return category;
    }

    public void setCategory(NutritionPlan.NutritionCategory category) {
        this.category = category;
    }

    public NutritionPlan.DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(NutritionPlan.DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(Integer targetCalories) {
        this.targetCalories = targetCalories;
    }

    public Integer getTargetProtein() {
        return targetProtein;
    }

    public void setTargetProtein(Integer targetProtein) {
        this.targetProtein = targetProtein;
    }

    public Integer getTargetCarbs() {
        return targetCarbs;
    }

    public void setTargetCarbs(Integer targetCarbs) {
        this.targetCarbs = targetCarbs;
    }

    public Integer getTargetFat() {
        return targetFat;
    }

    public void setTargetFat(Integer targetFat) {
        this.targetFat = targetFat;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
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

    public List<NutritionPlanMealDTO> getMeals() {
        return meals;
    }

    public void setMeals(List<NutritionPlanMealDTO> meals) {
        this.meals = meals;
    }

    @Override
    public String toString() {
        return "NutritionPlanDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", difficulty=" + difficulty +
                ", targetCalories=" + targetCalories +
                ", duration=" + duration +
                '}';
    }
}
