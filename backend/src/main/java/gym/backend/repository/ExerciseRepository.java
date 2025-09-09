package gym.backend.repository;

import gym.backend.model.Exercise;
import gym.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    // Find exercises by category
    List<Exercise> findByCategory(Exercise.ExerciseCategory category);
    
    Page<Exercise> findByCategory(Exercise.ExerciseCategory category, Pageable pageable);

    // Find exercises by difficulty
    List<Exercise> findByDifficulty(Exercise.DifficultyLevel difficulty);
    
    Page<Exercise> findByDifficulty(Exercise.DifficultyLevel difficulty, Pageable pageable);

    // Find exercises by category and difficulty
    List<Exercise> findByCategoryAndDifficulty(Exercise.ExerciseCategory category, Exercise.DifficultyLevel difficulty);
    
    Page<Exercise> findByCategoryAndDifficulty(Exercise.ExerciseCategory category, Exercise.DifficultyLevel difficulty, Pageable pageable);

    // Find public exercises
    List<Exercise> findByIsPublicTrue();
    
    Page<Exercise> findByIsPublicTrue(Pageable pageable);

    // Find custom exercises by creator
    List<Exercise> findByCreatedByAndIsCustomTrue(User createdBy);
    
    Page<Exercise> findByCreatedByAndIsCustomTrue(User createdBy, Pageable pageable);

    // Find exercises by creator (including public ones)
    List<Exercise> findByCreatedBy(User createdBy);
    
    Page<Exercise> findByCreatedBy(User createdBy, Pageable pageable);

    // Search exercises by name
    @Query("SELECT e FROM Exercise e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Exercise> findByNameContaining(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT e FROM Exercise e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Exercise> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Search exercises by description
    @Query("SELECT e FROM Exercise e WHERE LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Exercise> findByDescriptionContaining(@Param("searchTerm") String searchTerm);

    // Find exercises by muscle group
    @Query("SELECT e FROM Exercise e JOIN e.muscleGroups mg WHERE mg = :muscleGroup")
    List<Exercise> findByMuscleGroup(@Param("muscleGroup") String muscleGroup);
    
    @Query("SELECT e FROM Exercise e JOIN e.muscleGroups mg WHERE mg = :muscleGroup")
    Page<Exercise> findByMuscleGroup(@Param("muscleGroup") String muscleGroup, Pageable pageable);

    // Find exercises by equipment
    @Query("SELECT e FROM Exercise e JOIN e.equipment eq WHERE eq = :equipment")
    List<Exercise> findByEquipment(@Param("equipment") String equipment);
    
    @Query("SELECT e FROM Exercise e JOIN e.equipment eq WHERE eq = :equipment")
    Page<Exercise> findByEquipment(@Param("equipment") String equipment, Pageable pageable);

    // Find exercises by multiple muscle groups
    @Query("SELECT e FROM Exercise e JOIN e.muscleGroups mg WHERE mg IN :muscleGroups")
    List<Exercise> findByMuscleGroupsIn(@Param("muscleGroups") List<String> muscleGroups);

    // Find exercises by multiple equipment
    @Query("SELECT e FROM Exercise e JOIN e.equipment eq WHERE eq IN :equipment")
    List<Exercise> findByEquipmentIn(@Param("equipment") List<String> equipment);

    // Find exercises without equipment (bodyweight)
    @Query("SELECT e FROM Exercise e WHERE SIZE(e.equipment) = 0 OR 'Własny ciężar ciała' MEMBER OF e.equipment")
    List<Exercise> findBodyweightExercises();

    // Find most popular exercises
    @Query("SELECT e FROM Exercise e ORDER BY e.usageCount DESC")
    List<Exercise> findMostPopularExercises(Pageable pageable);

    // Find exercises by usage count range
    @Query("SELECT e FROM Exercise e WHERE e.usageCount BETWEEN :minUsage AND :maxUsage")
    List<Exercise> findByUsageCountBetween(@Param("minUsage") Integer minUsage, @Param("maxUsage") Integer maxUsage);

    // Find exercises with images
    @Query("SELECT e FROM Exercise e WHERE e.imageUrl IS NOT NULL AND e.imageUrl != ''")
    List<Exercise> findExercisesWithImages();

    // Find exercises with videos
    @Query("SELECT e FROM Exercise e WHERE e.videoUrl IS NOT NULL AND e.videoUrl != ''")
    List<Exercise> findExercisesWithVideos();

    // Complex search with multiple criteria
    @Query("SELECT e FROM Exercise e WHERE " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:difficulty IS NULL OR e.difficulty = :difficulty) AND " +
           "(:muscleGroup IS NULL OR :muscleGroup MEMBER OF e.muscleGroups) AND " +
           "(:equipment IS NULL OR :equipment MEMBER OF e.equipment) AND " +
           "(:isPublic IS NULL OR e.isPublic = :isPublic) AND " +
           "(:searchTerm IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Exercise> findExercisesWithFilters(@Param("category") Exercise.ExerciseCategory category,
                                           @Param("difficulty") Exercise.DifficultyLevel difficulty,
                                           @Param("muscleGroup") String muscleGroup,
                                           @Param("equipment") String equipment,
                                           @Param("isPublic") Boolean isPublic,
                                           @Param("searchTerm") String searchTerm);

    @Query("SELECT e FROM Exercise e WHERE " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:difficulty IS NULL OR e.difficulty = :difficulty) AND " +
           "(:muscleGroup IS NULL OR :muscleGroup MEMBER OF e.muscleGroups) AND " +
           "(:equipment IS NULL OR :equipment MEMBER OF e.equipment) AND " +
           "(:isPublic IS NULL OR e.isPublic = :isPublic) AND " +
           "(:searchTerm IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Exercise> findExercisesWithFilters(@Param("category") Exercise.ExerciseCategory category,
                                           @Param("difficulty") Exercise.DifficultyLevel difficulty,
                                           @Param("muscleGroup") String muscleGroup,
                                           @Param("equipment") String equipment,
                                           @Param("isPublic") Boolean isPublic,
                                           @Param("searchTerm") String searchTerm,
                                           Pageable pageable);

    // Find exercises by name (exact match)
    Optional<Exercise> findByName(String name);

    // Check if exercise name exists
    boolean existsByName(String name);

    // Find exercises created by user or public
    @Query("SELECT e FROM Exercise e WHERE e.createdBy = :user OR e.isPublic = true")
    List<Exercise> findAvailableExercisesForUser(@Param("user") User user);

    @Query("SELECT e FROM Exercise e WHERE e.createdBy = :user OR e.isPublic = true")
    Page<Exercise> findAvailableExercisesForUser(@Param("user") User user, Pageable pageable);

    // Statistics
    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.category = :category")
    Long countByCategory(@Param("category") Exercise.ExerciseCategory category);

    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.difficulty = :difficulty")
    Long countByDifficulty(@Param("difficulty") Exercise.DifficultyLevel difficulty);

    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.isPublic = true")
    Long countPublicExercises();

    @Query("SELECT COUNT(e) FROM Exercise e WHERE e.isCustom = true")
    Long countCustomExercises();

    @Query("SELECT AVG(e.usageCount) FROM Exercise e")
    Double getAverageUsageCount();

    // Find unique muscle groups
    @Query("SELECT DISTINCT mg FROM Exercise e JOIN e.muscleGroups mg ORDER BY mg")
    List<String> findDistinctMuscleGroups();

    // Find unique equipment
    @Query("SELECT DISTINCT eq FROM Exercise e JOIN e.equipment eq ORDER BY eq")
    List<String> findDistinctEquipment();

    // Additional methods needed by ExerciseService
    List<Exercise> findByNameContainingIgnoreCase(String name);
    Page<Exercise> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    List<Exercise> findByMuscleGroupsContaining(String muscleGroup);
    Page<Exercise> findByMuscleGroupsContaining(String muscleGroup, Pageable pageable);
    
    List<Exercise> findByEquipmentContaining(String equipment);
    Page<Exercise> findByEquipmentContaining(String equipment, Pageable pageable);
}
