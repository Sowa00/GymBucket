package gym.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nutrition_plans")
public class NutritionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NutritionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DifficultyLevel difficulty;

    @Column
    private Integer targetCalories;

    @Column
    private Integer targetProtein;

    @Column
    private Integer targetCarbs;

    @Column
    private Integer targetFat;

    @Column
    private Integer duration; // in days

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column
    private Boolean isPublic = false;

    @Column
    private Boolean isTemplate = false;

    @Column
    private Integer usageCount = 0;

    @Column
    private Integer rating = 0;

    @Column
    private Integer ratingCount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "nutritionPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("dayNumber ASC, mealOrder ASC")
    private List<NutritionPlanMeal> meals = new ArrayList<>();

    // Constructors
    public NutritionPlan() {}

    public NutritionPlan(String name, String description, NutritionCategory category, 
                        DifficultyLevel difficulty, User createdBy) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.difficulty = difficulty;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public NutritionCategory getCategory() {
        return category;
    }

    public void setCategory(NutritionCategory category) {
        this.category = category;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
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

    public List<NutritionPlanMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<NutritionPlanMeal> meals) {
        this.meals = meals;
    }

    // Helper methods
    public String getFormattedDuration() {
        if (duration == null || duration <= 0) {
            return "N/A";
        }
        if (duration < 7) {
            return duration + " dni";
        } else if (duration < 30) {
            int weeks = duration / 7;
            int days = duration % 7;
            if (days == 0) {
                return weeks + " tygodni";
            } else {
                return weeks + " tygodni " + days + " dni";
            }
        } else {
            int months = duration / 30;
            int days = duration % 30;
            if (days == 0) {
                return months + " miesięcy";
            } else {
                return months + " miesięcy " + days + " dni";
            }
        }
    }

    public String getFormattedTargetCalories() {
        if (targetCalories == null || targetCalories <= 0) {
            return "N/A";
        }
        return targetCalories + " kcal";
    }

    public Double getAverageRating() {
        if (ratingCount == null || ratingCount == 0) {
            return 0.0;
        }
        return (double) rating / ratingCount;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "NutritionPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", difficulty=" + difficulty +
                ", targetCalories=" + targetCalories +
                ", duration=" + duration +
                '}';
    }

    public enum NutritionCategory {
        WEIGHT_LOSS("Redukcja"),
        MUSCLE_GAIN("Budowanie masy"),
        MAINTENANCE("Utrzymanie"),
        PERFORMANCE("Wydajność"),
        HEALTH("Zdrowie"),
        DETOX("Detoks"),
        VEGETARIAN("Wegetariański"),
        VEGAN("Wegański"),
        KETO("Keto"),
        PALEO("Paleo");

        private final String displayName;

        NutritionCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DifficultyLevel {
        BEGINNER("Początkujący"),
        INTERMEDIATE("Średniozaawansowany"),
        ADVANCED("Zaawansowany"),
        EXPERT("Ekspert");

        private final String displayName;

        DifficultyLevel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
