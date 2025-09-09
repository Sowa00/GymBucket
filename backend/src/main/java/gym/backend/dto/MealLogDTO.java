package gym.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class MealLogDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private Long mealId;
    private String mealName;
    private Long nutritionPlanId;
    private String nutritionPlanName;
    private LocalDate logDate;
    private LocalTime mealTime;
    private Double portionSize;
    private Double actualCalories;
    private Double actualProtein;
    private Double actualCarbs;
    private Double actualFat;
    private String notes;
    private Long loggedById;
    private String loggedByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public MealLogDTO() {}

    public MealLogDTO(Long id, Long clientId, String clientName, 
                     Long mealId, String mealName, LocalDate logDate, LocalTime mealTime) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.mealId = mealId;
        this.mealName = mealName;
        this.logDate = logDate;
        this.mealTime = mealTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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

    public Long getNutritionPlanId() {
        return nutritionPlanId;
    }

    public void setNutritionPlanId(Long nutritionPlanId) {
        this.nutritionPlanId = nutritionPlanId;
    }

    public String getNutritionPlanName() {
        return nutritionPlanName;
    }

    public void setNutritionPlanName(String nutritionPlanName) {
        this.nutritionPlanName = nutritionPlanName;
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

    public String getLoggedByName() {
        return loggedByName;
    }

    public void setLoggedByName(String loggedByName) {
        this.loggedByName = loggedByName;
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
    public boolean hasNutritionPlan() {
        return nutritionPlanId != null;
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public boolean hasActualNutrition() {
        return actualCalories != null || actualProtein != null || actualCarbs != null || actualFat != null;
    }

    public String getFormattedMealTime() {
        if (mealTime == null) {
            return "N/A";
        }
        return mealTime.toString();
    }

    public String getFormattedPortionSize() {
        if (portionSize == null) {
            return "1.0";
        }
        return String.format("%.1f", portionSize);
    }

    public String getFormattedActualCalories() {
        if (actualCalories == null) {
            return "N/A";
        }
        return String.format("%.1f kcal", actualCalories);
    }

    public String getFormattedActualProtein() {
        if (actualProtein == null) {
            return "N/A";
        }
        return String.format("%.1f g", actualProtein);
    }

    public String getFormattedActualCarbs() {
        if (actualCarbs == null) {
            return "N/A";
        }
        return String.format("%.1f g", actualCarbs);
    }

    public String getFormattedActualFat() {
        if (actualFat == null) {
            return "N/A";
        }
        return String.format("%.1f g", actualFat);
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

    public boolean isToday() {
        return logDate != null && logDate.equals(LocalDate.now());
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

    public boolean isRecent() {
        if (logDate == null) {
            return false;
        }
        return logDate.isAfter(LocalDate.now().minusDays(7));
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

    @Override
    public String toString() {
        return "MealLogDTO{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", mealName='" + mealName + '\'' +
                ", logDate=" + logDate +
                ", mealTime=" + mealTime +
                ", portionSize=" + portionSize +
                ", actualCalories=" + actualCalories +
                '}';
    }
}
