package gym.backend.dto;

public class NutritionPlanMealRequestDTO {
    private Long mealId;
    private Integer dayNumber;
    private Integer mealOrder;
    private Double portionSize;
    private String notes;
    private Boolean isOptional;

    // Constructors
    public NutritionPlanMealRequestDTO() {}

    public NutritionPlanMealRequestDTO(Long mealId, Integer dayNumber, Integer mealOrder) {
        this.mealId = mealId;
        this.dayNumber = dayNumber;
        this.mealOrder = mealOrder;
    }

    // Getters and Setters
    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Integer getMealOrder() {
        return mealOrder;
    }

    public void setMealOrder(Integer mealOrder) {
        this.mealOrder = mealOrder;
    }

    public Double getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(Double portionSize) {
        this.portionSize = portionSize;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(Boolean isOptional) {
        this.isOptional = isOptional;
    }

    @Override
    public String toString() {
        return "NutritionPlanMealRequestDTO{" +
                "mealId=" + mealId +
                ", dayNumber=" + dayNumber +
                ", mealOrder=" + mealOrder +
                ", portionSize=" + portionSize +
                '}';
    }
}
