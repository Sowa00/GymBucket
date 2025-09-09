package gym.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "meal_ingredients")
public class MealIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, length = 20)
    private String unit; // g, ml, cup, tbsp, tsp, piece, etc.

    @Column
    private Double calories;

    @Column
    private Double protein;

    @Column
    private Double carbs;

    @Column
    private Double fat;

    @Column
    private Double fiber;

    @Column
    private Double sugar;

    @Column
    private Double sodium;

    // Constructors
    public MealIngredient() {}

    public MealIngredient(Meal meal, String name, Double amount, String unit) {
        this.meal = meal;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
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

    // Helper methods
    public String getFormattedAmount() {
        if (amount == null || unit == null) {
            return "N/A";
        }
        return amount + " " + unit;
    }

    public String getFormattedCalories() {
        if (calories == null || calories <= 0) {
            return "N/A";
        }
        return String.format("%.1f kcal", calories);
    }

    @Override
    public String toString() {
        return "MealIngredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", calories=" + calories +
                '}';
    }
}
