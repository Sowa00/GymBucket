package gym.backend.dto;

import gym.backend.model.NutritionPlan;

import java.util.List;

public class NutritionPlanRequestDTO {
    private String name;
    private String description;
    private NutritionPlan.NutritionCategory category;
    private NutritionPlan.DifficultyLevel difficulty;
    private Integer targetCalories;
    private Integer targetProtein;
    private Integer targetCarbs;
    private Integer targetFat;
    private Integer duration;
    private Boolean isPublic;
    private Boolean isTemplate;
    private List<NutritionPlanMealRequestDTO> meals;

    // Constructors
    public NutritionPlanRequestDTO() {}

    public NutritionPlanRequestDTO(String name, String description, NutritionPlan.NutritionCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
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

    public List<NutritionPlanMealRequestDTO> getMeals() {
        return meals;
    }

    public void setMeals(List<NutritionPlanMealRequestDTO> meals) {
        this.meals = meals;
    }

    @Override
    public String toString() {
        return "NutritionPlanRequestDTO{" +
                "name='" + name + '\'' +
                ", category=" + category +
                ", difficulty=" + difficulty +
                ", targetCalories=" + targetCalories +
                ", duration=" + duration +
                '}';
    }
}
