package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "meal_logs")
public class MealLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrition_plan_id")
    private NutritionPlan nutritionPlan;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "meal_time", nullable = false)
    private LocalTime mealTime;

    @Column(name = "portion_size")
    private Double portionSize = 1.0;

    @Column(name = "actual_calories")
    private Double actualCalories;

    @Column(name = "actual_protein")
    private Double actualProtein;

    @Column(name = "actual_carbs")
    private Double actualCarbs;

    @Column(name = "actual_fat")
    private Double actualFat;

    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logged_by", nullable = false)
    private User loggedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public MealLog() {}

    public MealLog(Client client, Meal meal, LocalDate logDate, LocalTime mealTime, User loggedBy) {
        this.client = client;
        this.meal = meal;
        this.logDate = logDate;
        this.mealTime = mealTime;
        this.loggedBy = loggedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public NutritionPlan getNutritionPlan() {
        return nutritionPlan;
    }

    public void setNutritionPlan(NutritionPlan nutritionPlan) {
        this.nutritionPlan = nutritionPlan;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public LocalTime getMealTime() {
        return mealTime;
    }

    public void setMealTime(LocalTime mealTime) {
        this.mealTime = mealTime;
    }

    public Double getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(Double portionSize) {
        this.portionSize = portionSize;
    }

    public Double getActualCalories() {
        return actualCalories;
    }

    public void setActualCalories(Double actualCalories) {
        this.actualCalories = actualCalories;
    }

    public Double getActualProtein() {
        return actualProtein;
    }

    public void setActualProtein(Double actualProtein) {
        this.actualProtein = actualProtein;
    }

    public Double getActualCarbs() {
        return actualCarbs;
    }

    public void setActualCarbs(Double actualCarbs) {
        this.actualCarbs = actualCarbs;
    }

    public Double getActualFat() {
        return actualFat;
    }

    public void setActualFat(Double actualFat) {
        this.actualFat = actualFat;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getLoggedBy() {
        return loggedBy;
    }

    public void setLoggedBy(User loggedBy) {
        this.loggedBy = loggedBy;
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
    public void calculateActualNutrition() {
        if (meal != null && portionSize != null) {
            this.actualCalories = (meal.getCalories() != null ? meal.getCalories() : 0) * portionSize;
            this.actualProtein = (meal.getProtein() != null ? meal.getProtein() : 0) * portionSize;
            this.actualCarbs = (meal.getCarbs() != null ? meal.getCarbs() : 0) * portionSize;
            this.actualFat = (meal.getFat() != null ? meal.getFat() : 0) * portionSize;
        }
    }

    public Double getTotalCalories() {
        return actualCalories != null ? actualCalories : 0.0;
    }

    public Double getTotalProtein() {
        return actualProtein != null ? actualProtein : 0.0;
    }

    public Double getTotalCarbs() {
        return actualCarbs != null ? actualCarbs : 0.0;
    }

    public Double getTotalFat() {
        return actualFat != null ? actualFat : 0.0;
    }

    public String getFormattedMealTime() {
        if (mealTime == null) {
            return "";
        }
        return mealTime.toString();
    }

    // Additional methods needed by MealLogService
    public Long getClientId() {
        return client != null ? client.getId() : null;
    }

    public void setClientId(Long clientId) {
        // This method is used by services but doesn't actually set the client
        // The client should be set through the setClient method
    }

    public Long getMealId() {
        return meal != null ? meal.getId() : null;
    }

    public void setMealId(Long mealId) {
        // This method is used by services but doesn't actually set the meal
        // The meal should be set through the setMeal method
    }

    public Long getNutritionPlanId() {
        return nutritionPlan != null ? nutritionPlan.getId() : null;
    }

    public void setNutritionPlanId(Long nutritionPlanId) {
        // This method is used by services but doesn't actually set the nutrition plan
        // The nutrition plan should be set through the setNutritionPlan method
    }

    public Long getLoggedById() {
        return loggedBy != null ? loggedBy.getId() : null;
    }

    public void setLoggedById(Long loggedById) {
        // This method is used by services but doesn't actually set the loggedBy
        // The loggedBy should be set through the setLoggedBy method
    }

    public boolean isFromNutritionPlan() {
        return nutritionPlan != null;
    }

    @Override
    public String toString() {
        return "MealLog{" +
                "id=" + id +
                ", client=" + (client != null ? client.getFullName() : "null") +
                ", meal=" + (meal != null ? meal.getName() : "null") +
                ", logDate=" + logDate +
                ", mealTime=" + mealTime +
                ", portionSize=" + portionSize +
                ", actualCalories=" + actualCalories +
                '}';
    }
}
