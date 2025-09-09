package gym.backend.repository;

import gym.backend.model.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    // Find meals by category
    List<Meal> findByCategory(Meal.MealCategory category);
    Page<Meal> findByCategory(Meal.MealCategory category, Pageable pageable);

    // Find meals by creator
    List<Meal> findByCreatedBy_Id(Long userId);
    Page<Meal> findByCreatedBy_Id(Long userId, Pageable pageable);

    // Find public meals
    List<Meal> findByIsPublicTrue();
    Page<Meal> findByIsPublicTrue(Pageable pageable);

    // Find custom meals
    List<Meal> findByIsCustomTrue();
    Page<Meal> findByIsCustomTrue(Pageable pageable);

    // Search meals by name
    List<Meal> findByNameContainingIgnoreCase(String name);
    Page<Meal> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Find meals by calorie range
    List<Meal> findByCaloriesBetween(Integer minCalories, Integer maxCalories);
    Page<Meal> findByCaloriesBetween(Integer minCalories, Integer maxCalories, Pageable pageable);

    // Find meals by prep time
    List<Meal> findByPrepTimeLessThanEqual(Integer maxPrepTime);
    Page<Meal> findByPrepTimeLessThanEqual(Integer maxPrepTime, Pageable pageable);

    // Complex search with filters
    @Query("SELECT m FROM Meal m WHERE " +
           "(:category IS NULL OR m.category = :category) AND " +
           "(:minCalories IS NULL OR m.calories >= :minCalories) AND " +
           "(:maxCalories IS NULL OR m.calories <= :maxCalories) AND " +
           "(:maxPrepTime IS NULL OR m.prepTime <= :maxPrepTime) AND " +
           "(:isPublic IS NULL OR m.isPublic = :isPublic) AND " +
           "(:isCustom IS NULL OR m.isCustom = :isCustom) AND " +
           "(:searchTerm IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Meal> findMealsWithFilters(@Param("category") Meal.MealCategory category,
                                   @Param("minCalories") Integer minCalories,
                                   @Param("maxCalories") Integer maxCalories,
                                   @Param("maxPrepTime") Integer maxPrepTime,
                                   @Param("isPublic") Boolean isPublic,
                                   @Param("isCustom") Boolean isCustom,
                                   @Param("searchTerm") String searchTerm);

    @Query("SELECT m FROM Meal m WHERE " +
           "(:category IS NULL OR m.category = :category) AND " +
           "(:minCalories IS NULL OR m.calories >= :minCalories) AND " +
           "(:maxCalories IS NULL OR m.calories <= :maxCalories) AND " +
           "(:maxPrepTime IS NULL OR m.prepTime <= :maxPrepTime) AND " +
           "(:isPublic IS NULL OR m.isPublic = :isPublic) AND " +
           "(:isCustom IS NULL OR m.isCustom = :isCustom) AND " +
           "(:searchTerm IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Meal> findMealsWithFilters(@Param("category") Meal.MealCategory category,
                                   @Param("minCalories") Integer minCalories,
                                   @Param("maxCalories") Integer maxCalories,
                                   @Param("maxPrepTime") Integer maxPrepTime,
                                   @Param("isPublic") Boolean isPublic,
                                   @Param("isCustom") Boolean isCustom,
                                   @Param("searchTerm") String searchTerm,
                                   Pageable pageable);

    // Count meals by category
    @Query("SELECT COUNT(m) FROM Meal m WHERE m.category = :category")
    Long countByCategory(@Param("category") Meal.MealCategory category);

    // Count meals by creator
    @Query("SELECT COUNT(m) FROM Meal m WHERE m.createdBy.id = :userId")
    Long countByCreatedBy(@Param("userId") Long userId);

    // Find most popular meals
    @Query("SELECT m FROM Meal m ORDER BY m.usageCount DESC")
    List<Meal> findMostPopularMeals(Pageable pageable);

    // Find recent meals
    @Query("SELECT m FROM Meal m ORDER BY m.createdAt DESC")
    List<Meal> findRecentMeals(Pageable pageable);
}
