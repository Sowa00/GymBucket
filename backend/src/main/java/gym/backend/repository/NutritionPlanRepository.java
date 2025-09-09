package gym.backend.repository;

import gym.backend.model.NutritionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {

    // Find plans by category
    List<NutritionPlan> findByCategory(NutritionPlan.NutritionCategory category);
    Page<NutritionPlan> findByCategory(NutritionPlan.NutritionCategory category, Pageable pageable);

    // Find plans by difficulty
    List<NutritionPlan> findByDifficulty(NutritionPlan.DifficultyLevel difficulty);
    Page<NutritionPlan> findByDifficulty(NutritionPlan.DifficultyLevel difficulty, Pageable pageable);

    // Find plans by creator
    List<NutritionPlan> findByCreatedBy_Id(Long userId);
    Page<NutritionPlan> findByCreatedBy_Id(Long userId, Pageable pageable);

    // Find public plans
    List<NutritionPlan> findByIsPublicTrue();
    Page<NutritionPlan> findByIsPublicTrue(Pageable pageable);

    // Find template plans
    List<NutritionPlan> findByIsTemplateTrue();
    Page<NutritionPlan> findByIsTemplateTrue(Pageable pageable);

    // Search plans by name
    List<NutritionPlan> findByNameContainingIgnoreCase(String name);
    Page<NutritionPlan> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Find plans by calorie range
    List<NutritionPlan> findByTargetCaloriesBetween(Integer minCalories, Integer maxCalories);
    Page<NutritionPlan> findByTargetCaloriesBetween(Integer minCalories, Integer maxCalories, Pageable pageable);

    // Find plans by duration
    List<NutritionPlan> findByDurationLessThanEqual(Integer maxDuration);
    Page<NutritionPlan> findByDurationLessThanEqual(Integer maxDuration, Pageable pageable);

    // Complex search with filters
    @Query("SELECT np FROM NutritionPlan np WHERE " +
           "(:category IS NULL OR np.category = :category) AND " +
           "(:difficulty IS NULL OR np.difficulty = :difficulty) AND " +
           "(:minCalories IS NULL OR np.targetCalories >= :minCalories) AND " +
           "(:maxCalories IS NULL OR np.targetCalories <= :maxCalories) AND " +
           "(:maxDuration IS NULL OR np.duration <= :maxDuration) AND " +
           "(:isPublic IS NULL OR np.isPublic = :isPublic) AND " +
           "(:isTemplate IS NULL OR np.isTemplate = :isTemplate) AND " +
           "(:searchTerm IS NULL OR LOWER(np.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(np.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<NutritionPlan> findPlansWithFilters(@Param("category") NutritionPlan.NutritionCategory category,
                                           @Param("difficulty") NutritionPlan.DifficultyLevel difficulty,
                                           @Param("minCalories") Integer minCalories,
                                           @Param("maxCalories") Integer maxCalories,
                                           @Param("maxDuration") Integer maxDuration,
                                           @Param("isPublic") Boolean isPublic,
                                           @Param("isTemplate") Boolean isTemplate,
                                           @Param("searchTerm") String searchTerm);

    @Query("SELECT np FROM NutritionPlan np WHERE " +
           "(:category IS NULL OR np.category = :category) AND " +
           "(:difficulty IS NULL OR np.difficulty = :difficulty) AND " +
           "(:minCalories IS NULL OR np.targetCalories >= :minCalories) AND " +
           "(:maxCalories IS NULL OR np.targetCalories <= :maxCalories) AND " +
           "(:maxDuration IS NULL OR np.duration <= :maxDuration) AND " +
           "(:isPublic IS NULL OR np.isPublic = :isPublic) AND " +
           "(:isTemplate IS NULL OR np.isTemplate = :isTemplate) AND " +
           "(:searchTerm IS NULL OR LOWER(np.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(np.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<NutritionPlan> findPlansWithFilters(@Param("category") NutritionPlan.NutritionCategory category,
                                           @Param("difficulty") NutritionPlan.DifficultyLevel difficulty,
                                           @Param("minCalories") Integer minCalories,
                                           @Param("maxCalories") Integer maxCalories,
                                           @Param("maxDuration") Integer maxDuration,
                                           @Param("isPublic") Boolean isPublic,
                                           @Param("isTemplate") Boolean isTemplate,
                                           @Param("searchTerm") String searchTerm,
                                           Pageable pageable);

    // Count plans by category
    @Query("SELECT COUNT(np) FROM NutritionPlan np WHERE np.category = :category")
    Long countByCategory(@Param("category") NutritionPlan.NutritionCategory category);

    // Count plans by difficulty
    @Query("SELECT COUNT(np) FROM NutritionPlan np WHERE np.difficulty = :difficulty")
    Long countByDifficulty(@Param("difficulty") NutritionPlan.DifficultyLevel difficulty);

    // Count plans by creator
    @Query("SELECT COUNT(np) FROM NutritionPlan np WHERE np.createdBy.id = :userId")
    Long countByCreatedBy(@Param("userId") Long userId);

    // Find most popular plans
    @Query("SELECT np FROM NutritionPlan np ORDER BY np.usageCount DESC")
    List<NutritionPlan> findMostPopularPlans(Pageable pageable);

    // Find highest rated plans
    @Query("SELECT np FROM NutritionPlan np WHERE np.ratingCount > 0 ORDER BY (np.rating / np.ratingCount) DESC")
    List<NutritionPlan> findHighestRatedPlans(Pageable pageable);

    // Find recent plans
    @Query("SELECT np FROM NutritionPlan np ORDER BY np.createdAt DESC")
    List<NutritionPlan> findRecentPlans(Pageable pageable);
}
