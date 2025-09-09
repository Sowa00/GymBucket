package gym.backend.repository;

import gym.backend.model.Client;
import gym.backend.model.WorkoutPlan;
import gym.backend.model.WorkoutSessionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSessionLogRepository extends JpaRepository<WorkoutSessionLog, Long> {

    // Find logs by client
    List<WorkoutSessionLog> findByClient(Client client);
    
    // Find logs by client with pagination
    Page<WorkoutSessionLog> findByClient(Client client, Pageable pageable);
    
    // Additional methods needed by DashboardAnalyticsService
    @Query("SELECT AVG(wsl.rating) FROM WorkoutSessionLog wsl WHERE wsl.rating IS NOT NULL")
    Double getAverageRating();
    
    // Additional methods needed by WorkoutSessionLogService
    List<WorkoutSessionLog> findByClient_Id(Long clientId);
    Page<WorkoutSessionLog> findByClient_Id(Long clientId, Pageable pageable);
    List<WorkoutSessionLog> findByWorkoutPlan_Id(Long workoutPlanId);
    List<WorkoutSessionLog> findByLoggedBy_Id(Long loggedById);
    List<WorkoutSessionLog> findBySessionDateBetween(LocalDate startDate, LocalDate endDate);
    List<WorkoutSessionLog> findByClient_IdAndSessionDateBetween(Long clientId, LocalDate startDate, LocalDate endDate);
    List<WorkoutSessionLog> findBySessionDateAfter(LocalDate date);
    List<WorkoutSessionLog> findBySessionDate(LocalDate date);
    List<WorkoutSessionLog> findByRating(Integer rating);
    List<WorkoutSessionLog> findByDifficultyRating(Integer difficultyRating);
    List<WorkoutSessionLog> findByClient_IdAndRatingIsNotNull(Long clientId);
    List<WorkoutSessionLog> findByClient_IdAndCaloriesBurnedIsNotNull(Long clientId);
    List<WorkoutSessionLog> findByClient_IdAndTotalDurationIsNotNull(Long clientId);
    
    // Find logs by workout plan
    List<WorkoutSessionLog> findByWorkoutPlan(WorkoutPlan workoutPlan);
    
    // Find logs by client and workout plan
    List<WorkoutSessionLog> findByClientAndWorkoutPlan(Client client, WorkoutPlan workoutPlan);
    
    // Find logs by date
    List<WorkoutSessionLog> findByClientAndSessionDate(Client client, LocalDate sessionDate);
    
    // Find logs by date range
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "wsl.sessionDate BETWEEN :startDate AND :endDate ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findByClientAndDateRange(@Param("client") Client client, 
                                                   @Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate);
    
    // Find latest log for client
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client " +
           "ORDER BY wsl.sessionDate DESC, wsl.startTime DESC LIMIT 1")
    Optional<WorkoutSessionLog> findLatestByClient(@Param("client") Client client);
    
    // Find logs with ratings
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "wsl.rating IS NOT NULL ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findWithRatings(@Param("client") Client client);
    
    // Find logs with difficulty ratings
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "wsl.difficultyRating IS NOT NULL ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findWithDifficultyRatings(@Param("client") Client client);
    
    // Find logs with calories burned
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "wsl.caloriesBurned IS NOT NULL ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findWithCaloriesBurned(@Param("client") Client client);
    
    // Find logs with notes
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "wsl.notes IS NOT NULL AND wsl.notes != '' ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findWithNotes(@Param("client") Client client);
    
    // Count logs by client
    Long countByClient(Client client);
    
    // Count logs by workout plan
    Long countByWorkoutPlan(WorkoutPlan workoutPlan);
    
    // Find logs by rating range
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "wsl.rating BETWEEN :minRating AND :maxRating ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findByRatingRange(@Param("client") Client client, 
                                            @Param("minRating") Integer minRating, 
                                            @Param("maxRating") Integer maxRating);
    
    // Find logs by duration range
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "wsl.totalDuration BETWEEN :minDuration AND :maxDuration ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findByDurationRange(@Param("client") Client client, 
                                              @Param("minDuration") Integer minDuration, 
                                              @Param("maxDuration") Integer maxDuration);
    
    // Find logs by calories range
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "wsl.caloriesBurned BETWEEN :minCalories AND :maxCalories ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findByCaloriesRange(@Param("client") Client client, 
                                              @Param("minCalories") Integer minCalories, 
                                              @Param("maxCalories") Integer maxCalories);
    
    // Find recent logs (last 30 days)
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "wsl.sessionDate >= :date ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findRecentLogs(@Param("client") Client client, 
                                         @Param("date") LocalDate date);
    
    // Find logs for specific month
    @Query("SELECT wsl FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND " +
           "YEAR(wsl.sessionDate) = :year AND MONTH(wsl.sessionDate) = :month " +
           "ORDER BY wsl.sessionDate DESC")
    List<WorkoutSessionLog> findByClientAndMonth(@Param("client") Client client, 
                                                @Param("year") int year, 
                                                @Param("month") int month);
    
    // Calculate average rating for client
    @Query("SELECT AVG(wsl.rating) FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND wsl.rating IS NOT NULL")
    Double calculateAverageRating(@Param("client") Client client);
    
    // Calculate average duration for client
    @Query("SELECT AVG(wsl.totalDuration) FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND wsl.totalDuration IS NOT NULL")
    Double calculateAverageDuration(@Param("client") Client client);
    
    // Calculate total calories burned for client
    @Query("SELECT SUM(wsl.caloriesBurned) FROM WorkoutSessionLog wsl WHERE wsl.client = :client AND wsl.caloriesBurned IS NOT NULL")
    Long calculateTotalCaloriesBurned(@Param("client") Client client);
    
}
