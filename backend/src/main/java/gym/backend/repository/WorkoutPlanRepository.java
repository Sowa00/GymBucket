package gym.backend.repository;

import gym.backend.model.User;
import gym.backend.model.WorkoutPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    // Find plans by creator
    List<WorkoutPlan> findByCreatedBy(User createdBy);
    
    Page<WorkoutPlan> findByCreatedBy(User createdBy, Pageable pageable);

    // Find public plans
    List<WorkoutPlan> findByIsPublicTrue();
    
    Page<WorkoutPlan> findByIsPublicTrue(Pageable pageable);
    
    // Find plans by public status
    List<WorkoutPlan> findByIsPublic(Boolean isPublic);
    
    Page<WorkoutPlan> findByIsPublic(Boolean isPublic, Pageable pageable);

    // Find templates
    List<WorkoutPlan> findByIsTemplateTrue();
    
    Page<WorkoutPlan> findByIsTemplateTrue(Pageable pageable);

    // Find plans by category
    List<WorkoutPlan> findByCategory(WorkoutPlan.WorkoutCategory category);
    
    Page<WorkoutPlan> findByCategory(WorkoutPlan.WorkoutCategory category, Pageable pageable);

    // Find plans by difficulty
    List<WorkoutPlan> findByDifficulty(WorkoutPlan.DifficultyLevel difficulty);
    
    Page<WorkoutPlan> findByDifficulty(WorkoutPlan.DifficultyLevel difficulty, Pageable pageable);

    // Find plans by category and difficulty
    List<WorkoutPlan> findByCategoryAndDifficulty(WorkoutPlan.WorkoutCategory category, WorkoutPlan.DifficultyLevel difficulty);
    
    Page<WorkoutPlan> findByCategoryAndDifficulty(WorkoutPlan.WorkoutCategory category, WorkoutPlan.DifficultyLevel difficulty, Pageable pageable);

    // Search plans by name
    @Query("SELECT wp FROM WorkoutPlan wp WHERE LOWER(wp.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<WorkoutPlan> findByNameContaining(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT wp FROM WorkoutPlan wp WHERE LOWER(wp.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<WorkoutPlan> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Search plans by description
    @Query("SELECT wp FROM WorkoutPlan wp WHERE LOWER(wp.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<WorkoutPlan> findByDescriptionContaining(@Param("searchTerm") String searchTerm);

    // Find plans by muscle group
    @Query("SELECT wp FROM WorkoutPlan wp JOIN wp.targetMuscleGroups tmg WHERE tmg = :muscleGroup")
    List<WorkoutPlan> findByTargetMuscleGroup(@Param("muscleGroup") String muscleGroup);
    
    @Query("SELECT wp FROM WorkoutPlan wp JOIN wp.targetMuscleGroups tmg WHERE tmg = :muscleGroup")
    Page<WorkoutPlan> findByTargetMuscleGroup(@Param("muscleGroup") String muscleGroup, Pageable pageable);

    // Find plans by equipment
    @Query("SELECT wp FROM WorkoutPlan wp JOIN wp.requiredEquipment re WHERE re = :equipment")
    List<WorkoutPlan> findByRequiredEquipment(@Param("equipment") String equipment);
    
    @Query("SELECT wp FROM WorkoutPlan wp JOIN wp.requiredEquipment re WHERE re = :equipment")
    Page<WorkoutPlan> findByRequiredEquipment(@Param("equipment") String equipment, Pageable pageable);

    // Find plans by tag
    @Query("SELECT wp FROM WorkoutPlan wp JOIN wp.tags t WHERE t = :tag")
    List<WorkoutPlan> findByTag(@Param("tag") String tag);
    
    @Query("SELECT wp FROM WorkoutPlan wp JOIN wp.tags t WHERE t = :tag")
    Page<WorkoutPlan> findByTag(@Param("tag") String tag, Pageable pageable);

    // Find plans by duration range
    @Query("SELECT wp FROM WorkoutPlan wp WHERE wp.estimatedDuration BETWEEN :minDuration AND :maxDuration")
    List<WorkoutPlan> findByDurationBetween(@Param("minDuration") Integer minDuration, @Param("maxDuration") Integer maxDuration);

    // Find most popular plans
    @Query("SELECT wp FROM WorkoutPlan wp ORDER BY wp.usageCount DESC")
    List<WorkoutPlan> findMostPopularPlans(Pageable pageable);

    // Find highest rated plans
    @Query("SELECT wp FROM WorkoutPlan wp WHERE wp.ratingCount > 0 ORDER BY (wp.rating / wp.ratingCount) DESC")
    List<WorkoutPlan> findHighestRatedPlans(Pageable pageable);

    // Find plans by usage count range
    @Query("SELECT wp FROM WorkoutPlan wp WHERE wp.usageCount BETWEEN :minUsage AND :maxUsage")
    List<WorkoutPlan> findByUsageCountBetween(@Param("minUsage") Integer minUsage, @Param("maxUsage") Integer maxUsage);

    // Find plans by rating range
    @Query("SELECT wp FROM WorkoutPlan wp WHERE wp.ratingCount > 0 AND (wp.rating / wp.ratingCount) BETWEEN :minRating AND :maxRating")
    List<WorkoutPlan> findByRatingBetween(@Param("minRating") Double minRating, @Param("maxRating") Double maxRating);

    // Complex search with multiple criteria
    @Query("SELECT wp FROM WorkoutPlan wp WHERE " +
           "(:category IS NULL OR wp.category = :category) AND " +
           "(:difficulty IS NULL OR wp.difficulty = :difficulty) AND " +
           "(:muscleGroup IS NULL OR :muscleGroup MEMBER OF wp.targetMuscleGroups) AND " +
           "(:equipment IS NULL OR :equipment MEMBER OF wp.requiredEquipment) AND " +
           "(:isPublic IS NULL OR wp.isPublic = :isPublic) AND " +
           "(:isTemplate IS NULL OR wp.isTemplate = :isTemplate) AND " +
           "(:minDuration IS NULL OR wp.estimatedDuration >= :minDuration) AND " +
           "(:maxDuration IS NULL OR wp.estimatedDuration <= :maxDuration) AND " +
           "(:searchTerm IS NULL OR LOWER(wp.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(wp.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<WorkoutPlan> findPlansWithFilters(@Param("category") WorkoutPlan.WorkoutCategory category,
                                          @Param("difficulty") WorkoutPlan.DifficultyLevel difficulty,
                                          @Param("muscleGroup") String muscleGroup,
                                          @Param("equipment") String equipment,
                                          @Param("isPublic") Boolean isPublic,
                                          @Param("isTemplate") Boolean isTemplate,
                                          @Param("minDuration") Integer minDuration,
                                          @Param("maxDuration") Integer maxDuration,
                                          @Param("searchTerm") String searchTerm);

    @Query("SELECT wp FROM WorkoutPlan wp WHERE " +
           "(:category IS NULL OR wp.category = :category) AND " +
           "(:difficulty IS NULL OR wp.difficulty = :difficulty) AND " +
           "(:muscleGroup IS NULL OR :muscleGroup MEMBER OF wp.targetMuscleGroups) AND " +
           "(:equipment IS NULL OR :equipment MEMBER OF wp.requiredEquipment) AND " +
           "(:isPublic IS NULL OR wp.isPublic = :isPublic) AND " +
           "(:isTemplate IS NULL OR wp.isTemplate = :isTemplate) AND " +
           "(:minDuration IS NULL OR wp.estimatedDuration >= :minDuration) AND " +
           "(:maxDuration IS NULL OR wp.estimatedDuration <= :maxDuration) AND " +
           "(:searchTerm IS NULL OR LOWER(wp.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(wp.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<WorkoutPlan> findPlansWithFilters(@Param("category") WorkoutPlan.WorkoutCategory category,
                                          @Param("difficulty") WorkoutPlan.DifficultyLevel difficulty,
                                          @Param("muscleGroup") String muscleGroup,
                                          @Param("equipment") String equipment,
                                          @Param("isPublic") Boolean isPublic,
                                          @Param("isTemplate") Boolean isTemplate,
                                          @Param("minDuration") Integer minDuration,
                                          @Param("maxDuration") Integer maxDuration,
                                          @Param("searchTerm") String searchTerm,
                                          Pageable pageable);

    // Find plans by name (exact match)
    Optional<WorkoutPlan> findByName(String name);

    // Check if plan name exists
    boolean existsByName(String name);

    // Find plans available to user (created by user or public)
    @Query("SELECT wp FROM WorkoutPlan wp WHERE wp.createdBy = :user OR wp.isPublic = true")
    List<WorkoutPlan> findAvailablePlansForUser(@Param("user") User user);

    @Query("SELECT wp FROM WorkoutPlan wp WHERE wp.createdBy = :user OR wp.isPublic = true")
    Page<WorkoutPlan> findAvailablePlansForUser(@Param("user") User user, Pageable pageable);

    // Find plans by multiple muscle groups
    @Query("SELECT wp FROM WorkoutPlan wp JOIN wp.targetMuscleGroups tmg WHERE tmg IN :muscleGroups")
    List<WorkoutPlan> findByTargetMuscleGroupsIn(@Param("muscleGroups") List<String> muscleGroups);

    // Find plans by multiple equipment
    @Query("SELECT wp FROM WorkoutPlan wp JOIN wp.requiredEquipment re WHERE re IN :equipment")
    List<WorkoutPlan> findByRequiredEquipmentIn(@Param("equipment") List<String> equipment);

    // Find plans by multiple tags
    @Query("SELECT wp FROM WorkoutPlan wp JOIN wp.tags t WHERE t IN :tags")
    List<WorkoutPlan> findByTagsIn(@Param("tags") List<String> tags);

    // Statistics
    @Query("SELECT COUNT(wp) FROM WorkoutPlan wp WHERE wp.category = :category")
    Long countByCategory(@Param("category") WorkoutPlan.WorkoutCategory category);

    @Query("SELECT COUNT(wp) FROM WorkoutPlan wp WHERE wp.difficulty = :difficulty")
    Long countByDifficulty(@Param("difficulty") WorkoutPlan.DifficultyLevel difficulty);

    @Query("SELECT COUNT(wp) FROM WorkoutPlan wp WHERE wp.isPublic = true")
    Long countPublicPlans();

    @Query("SELECT COUNT(wp) FROM WorkoutPlan wp WHERE wp.isTemplate = true")
    Long countTemplates();

    @Query("SELECT AVG(wp.usageCount) FROM WorkoutPlan wp")
    Double getAverageUsageCount();

    @Query("SELECT AVG(wp.rating / wp.ratingCount) FROM WorkoutPlan wp WHERE wp.ratingCount > 0")
    Double getAverageRating();

    // Find unique muscle groups
    @Query("SELECT DISTINCT tmg FROM WorkoutPlan wp JOIN wp.targetMuscleGroups tmg ORDER BY tmg")
    List<String> findDistinctTargetMuscleGroups();

    // Find unique equipment
    @Query("SELECT DISTINCT re FROM WorkoutPlan wp JOIN wp.requiredEquipment re ORDER BY re")
    List<String> findDistinctRequiredEquipment();

    // Find unique tags
    @Query("SELECT DISTINCT t FROM WorkoutPlan wp JOIN wp.tags t ORDER BY t")
    List<String> findDistinctTags();

    // Find recent plans
    @Query("SELECT wp FROM WorkoutPlan wp ORDER BY wp.createdAt DESC")
    List<WorkoutPlan> findRecentPlans(Pageable pageable);

    // Find plans by creator and public status
    @Query("SELECT wp FROM WorkoutPlan wp WHERE wp.createdBy = :user AND wp.isPublic = :isPublic")
    List<WorkoutPlan> findByCreatedByAndIsPublic(@Param("user") User user, @Param("isPublic") Boolean isPublic);

    // Additional methods needed by WorkoutPlanService
    List<WorkoutPlan> findByNameContainingIgnoreCase(String name);
    Page<WorkoutPlan> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
}
