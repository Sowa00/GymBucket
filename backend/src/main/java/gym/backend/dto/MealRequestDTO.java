package gym.backend.dto;

import gym.backend.model.Meal;

import java.util.List;

public class MealRequestDTO {
    private String name;
    private String description;
    private Meal.MealCategory category;
    private Integer calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double fiber;
    private Double sugar;
    private Double sodium;
    private String instructions;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private Boolean isCustom;
    private Boolean isPublic;
    private List<MealIngredientRequestDTO> ingredients;

    // Constructors
    public MealRequestDTO() {}

    public MealRequestDTO(String name, String description, Meal.MealCategory category) {
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

    public Meal.MealCategory getCategory() {
        return category;
    }

    public void setCategory(Meal.MealCategory category) {
        this.category = category;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getFiber() {
        return fiber;
    }

    public void setFiber(Double fiber) {
        this.fiber = fiber;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getSodium() {
        return sodium;
    }

    public void setSodium(Double sodium) {
        this.sodium = sodium;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public Boolean getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Boolean isCustom) {
        this.isCustom = isCustom;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<MealIngredientRequestDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<MealIngredientRequestDTO> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "MealRequestDTO{" +
                "name='" + name + '\'' +
                ", category=" + category +
                ", calories=" + calories +
                '}';
    }
}
