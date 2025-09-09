package gym.backend.dto;

public class NutritionPlanMealDTO {
    private Long id;
    private Long mealId;
    private String mealName;
    private Integer dayNumber;
    private Integer mealOrder;
    private String mealOrderName;
    private Double portionSize;
    private String notes;
    private Boolean isOptional;
    private MealDTO meal;

    // Constructors
    public NutritionPlanMealDTO() {}

    public NutritionPlanMealDTO(Long mealId, String mealName, Integer dayNumber, Integer mealOrder) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.dayNumber = dayNumber;
        this.mealOrder = mealOrder;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
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

    public String getMealOrderName() {
        return mealOrderName;
    }

    public void setMealOrderName(String mealOrderName) {
        this.mealOrderName = mealOrderName;
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

    public MealDTO getMeal() {
        return meal;
    }

    public void setMeal(MealDTO meal) {
        this.meal = meal;
    }

    @Override
    public String toString() {
        return "NutritionPlanMealDTO{" +
                "id=" + id +
                ", mealId=" + mealId +
                ", mealName='" + mealName + '\'' +
                ", dayNumber=" + dayNumber +
                ", mealOrder=" + mealOrder +
                ", portionSize=" + portionSize +
                '}';
    }
}
