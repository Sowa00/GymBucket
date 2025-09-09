package gym.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nutrition_plan_meals")
public class NutritionPlanMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrition_plan_id", nullable = false)
    private NutritionPlan nutritionPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @Column(nullable = false)
    private Integer dayNumber;

    @Column(nullable = false)
    private Integer mealOrder; // 1=breakfast, 2=lunch, 3=dinner, 4=snack

    @Column
    private Double portionSize = 1.0; // multiplier for the meal

    @Column(length = 500)
    private String notes;

    @Column
    private Boolean isOptional = false;

    // Constructors
    public NutritionPlanMeal() {}

    public NutritionPlanMeal(NutritionPlan nutritionPlan, Meal meal, Integer dayNumber, Integer mealOrder) {
        this.nutritionPlan = nutritionPlan;
        this.meal = meal;
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

    public NutritionPlan getNutritionPlan() {
        return nutritionPlan;
    }

    public void setNutritionPlan(NutritionPlan nutritionPlan) {
        this.nutritionPlan = nutritionPlan;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
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

    // Helper methods
    public String getFormattedPortionSize() {
        if (portionSize == null || portionSize == 1.0) {
            return "1x";
        }
        return String.format("%.1fx", portionSize);
    }

    public String getMealOrderName() {
        if (mealOrder == null) {
            return "N/A";
        }
        switch (mealOrder) {
            case 1: return "Śniadanie";
            case 2: return "Obiad";
            case 3: return "Kolacja";
            case 4: return "Przekąska";
            default: return "Posiłek " + mealOrder;
        }
    }

    public String getFormattedDayNumber() {
        if (dayNumber == null) {
            return "N/A";
        }
        return "Dzień " + dayNumber;
    }

    @Override
    public String toString() {
        return "NutritionPlanMeal{" +
                "id=" + id +
                ", dayNumber=" + dayNumber +
                ", mealOrder=" + mealOrder +
                ", portionSize=" + portionSize +
                ", isOptional=" + isOptional +
                '}';
    }
}
