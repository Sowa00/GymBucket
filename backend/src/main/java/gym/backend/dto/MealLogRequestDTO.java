package gym.backend.dto;

// import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class MealLogRequestDTO {
    
    // @NotNull(message = "Client ID is required")
    private Long clientId;
    
    // @NotNull(message = "Meal ID is required")
    private Long mealId;
    
    private Long nutritionPlanId;
    
    // @NotNull(message = "Log date is required")
    private LocalDate logDate;
    
    // @NotNull(message = "Meal time is required")
    private LocalTime mealTime;
    
    private Double portionSize = 1.0;
    
    private Double actualCalories;
    
    private Double actualProtein;
    
    private Double actualCarbs;
    
    private Double actualFat;
    
    private String notes;
    
    // @NotNull(message = "Logged by ID is required")
    private Long loggedById;

    // Constructors
    public MealLogRequestDTO() {}

    public MealLogRequestDTO(Long clientId, Long mealId, LocalDate logDate, 
                           LocalTime mealTime, Long loggedById) {
        this.clientId = clientId;
        this.mealId = mealId;
        this.logDate = logDate;
        this.mealTime = mealTime;
        this.loggedById = loggedById;
    }

    // Getters and Setters
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public Long getNutritionPlanId() {
        return nutritionPlanId;
    }

    public void setNutritionPlanId(Long nutritionPlanId) {
        this.nutritionPlanId = nutritionPlanId;
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

    public Long getLoggedById() {
        return loggedById;
    }

    public void setLoggedById(Long loggedById) {
        this.loggedById = loggedById;
    }

    // Helper methods
    public boolean hasNutritionPlan() {
        return nutritionPlanId != null;
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public boolean hasActualNutrition() {
        return actualCalories != null || actualProtein != null || actualCarbs != null || actualFat != null;
    }

    public boolean hasActualCalories() {
        return actualCalories != null;
    }

    public boolean hasActualProtein() {
        return actualProtein != null;
    }

    public boolean hasActualCarbs() {
        return actualCarbs != null;
    }

    public boolean hasActualFat() {
        return actualFat != null;
    }

    public boolean isToday() {
        return logDate != null && logDate.equals(LocalDate.now());
    }

    public boolean isFuture() {
        return logDate != null && logDate.isAfter(LocalDate.now());
    }

    public boolean isPast() {
        return logDate != null && logDate.isBefore(LocalDate.now());
    }

    public boolean isRecent() {
        if (logDate == null) {
            return false;
        }
        return logDate.isAfter(LocalDate.now().minusDays(7));
    }

    public boolean isThisWeek() {
        if (logDate == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return !logDate.isBefore(startOfWeek) && !logDate.isAfter(endOfWeek);
    }

    public boolean isThisMonth() {
        if (logDate == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return logDate.getYear() == now.getYear() && 
               logDate.getMonth() == now.getMonth();
    }

    public boolean isBreakfast() {
        if (mealTime == null) {
            return false;
        }
        return mealTime.isBefore(LocalTime.of(11, 0));
    }

    public boolean isLunch() {
        if (mealTime == null) {
            return false;
        }
        return mealTime.isAfter(LocalTime.of(11, 0)) && mealTime.isBefore(LocalTime.of(15, 0));
    }

    public boolean isDinner() {
        if (mealTime == null) {
            return false;
        }
        return mealTime.isAfter(LocalTime.of(15, 0));
    }

    public boolean hasValidPortionSize() {
        return portionSize != null && portionSize > 0;
    }

    public boolean hasValidActualCalories() {
        return actualCalories != null && actualCalories >= 0;
    }

    public boolean hasValidActualProtein() {
        return actualProtein != null && actualProtein >= 0;
    }

    public boolean hasValidActualCarbs() {
        return actualCarbs != null && actualCarbs >= 0;
    }

    public boolean hasValidActualFat() {
        return actualFat != null && actualFat >= 0;
    }

    @Override
    public String toString() {
        return "MealLogRequestDTO{" +
                "clientId=" + clientId +
                ", mealId=" + mealId +
                ", nutritionPlanId=" + nutritionPlanId +
                ", logDate=" + logDate +
                ", mealTime=" + mealTime +
                ", portionSize=" + portionSize +
                ", actualCalories=" + actualCalories +
                '}';
    }
}
