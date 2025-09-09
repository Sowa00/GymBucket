package gym.backend.repository;

import gym.backend.model.Client;
import gym.backend.model.Meal;
import gym.backend.model.MealLog;
import gym.backend.model.NutritionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {

    // Find logs by client
    List<MealLog> findByClient(Client client);
    
    // Find logs by client with pagination
    Page<MealLog> findByClient(Client client, Pageable pageable);
    
    // Find logs by meal
    List<MealLog> findByMeal(Meal meal);
    
    // Find logs by nutrition plan
    List<MealLog> findByNutritionPlan(NutritionPlan nutritionPlan);
    
    // Additional methods needed by MealLogService
    List<MealLog> findByClient_Id(Long clientId);
    Page<MealLog> findByClient_Id(Long clientId, Pageable pageable);
    List<MealLog> findByMeal_Id(Long mealId);
    List<MealLog> findByNutritionPlan_Id(Long nutritionPlanId);
    List<MealLog> findByLoggedBy_Id(Long loggedById);
    List<MealLog> findByLogDateBetween(LocalDate startDate, LocalDate endDate);
    List<MealLog> findByClient_IdAndLogDateBetween(Long clientId, LocalDate startDate, LocalDate endDate);
    List<MealLog> findByLogDateAfter(LocalDate date);
    List<MealLog> findByLogDate(LocalDate date);
    List<MealLog> findByClient_IdAndActualCaloriesIsNotNull(Long clientId);
    List<MealLog> findByClient_IdAndActualProteinIsNotNull(Long clientId);
    List<MealLog> findByClient_IdAndActualCarbsIsNotNull(Long clientId);
    List<MealLog> findByClient_IdAndActualFatIsNotNull(Long clientId);
    
    // Find logs by client and meal
    List<MealLog> findByClientAndMeal(Client client, Meal meal);
    
    // Find logs by client and nutrition plan
    List<MealLog> findByClientAndNutritionPlan(Client client, NutritionPlan nutritionPlan);
    
    // Find logs by date
    List<MealLog> findByClientAndLogDate(Client client, LocalDate logDate);
    
    // Find logs by date range
    @Query("SELECT ml FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.logDate BETWEEN :startDate AND :endDate ORDER BY ml.logDate DESC, ml.mealTime DESC")
    List<MealLog> findByClientAndDateRange(@Param("client") Client client, 
                                         @Param("startDate") LocalDate startDate, 
                                         @Param("endDate") LocalDate endDate);
    
    // Find logs by meal time
    @Query("SELECT ml FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.mealTime BETWEEN :startTime AND :endTime ORDER BY ml.logDate DESC")
    List<MealLog> findByClientAndMealTimeRange(@Param("client") Client client, 
                                             @Param("startTime") java.time.LocalTime startTime, 
                                             @Param("endTime") java.time.LocalTime endTime);
    
    // Find logs with notes
    @Query("SELECT ml FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.notes IS NOT NULL AND ml.notes != '' ORDER BY ml.logDate DESC")
    List<MealLog> findWithNotes(@Param("client") Client client);
    
    // Find logs with actual nutrition data
    @Query("SELECT ml FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.actualCalories IS NOT NULL ORDER BY ml.logDate DESC")
    List<MealLog> findWithActualNutrition(@Param("client") Client client);
    
    // Count logs by client
    Long countByClient(Client client);
    
    // Count logs by meal
    Long countByMeal(Meal meal);
    
    // Count logs by nutrition plan
    Long countByNutritionPlan(NutritionPlan nutritionPlan);
    
    // Find logs by portion size range
    @Query("SELECT ml FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.portionSize BETWEEN :minPortion AND :maxPortion ORDER BY ml.logDate DESC")
    List<MealLog> findByPortionSizeRange(@Param("client") Client client, 
                                       @Param("minPortion") Double minPortion, 
                                       @Param("maxPortion") Double maxPortion);
    
    // Find logs by calories range
    @Query("SELECT ml FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.actualCalories BETWEEN :minCalories AND :maxCalories ORDER BY ml.logDate DESC")
    List<MealLog> findByCaloriesRange(@Param("client") Client client, 
                                    @Param("minCalories") Double minCalories, 
                                    @Param("maxCalories") Double maxCalories);
    
    // Find recent logs (last 30 days)
    @Query("SELECT ml FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.logDate >= :date ORDER BY ml.logDate DESC, ml.mealTime DESC")
    List<MealLog> findRecentLogs(@Param("client") Client client, 
                               @Param("date") LocalDate date);
    
    // Find logs for specific month
    @Query("SELECT ml FROM MealLog ml WHERE ml.client = :client AND " +
           "YEAR(ml.logDate) = :year AND MONTH(ml.logDate) = :month " +
           "ORDER BY ml.logDate DESC, ml.mealTime DESC")
    List<MealLog> findByClientAndMonth(@Param("client") Client client, 
                                     @Param("year") int year, 
                                     @Param("month") int month);
    
    // Calculate total calories for client in date range
    @Query("SELECT SUM(ml.actualCalories) FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.logDate BETWEEN :startDate AND :endDate AND ml.actualCalories IS NOT NULL")
    Double calculateTotalCalories(@Param("client") Client client, 
                                @Param("startDate") LocalDate startDate, 
                                @Param("endDate") LocalDate endDate);
    
    // Calculate total protein for client in date range
    @Query("SELECT SUM(ml.actualProtein) FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.logDate BETWEEN :startDate AND :endDate AND ml.actualProtein IS NOT NULL")
    Double calculateTotalProtein(@Param("client") Client client, 
                               @Param("startDate") LocalDate startDate, 
                               @Param("endDate") LocalDate endDate);
    
    // Calculate total carbs for client in date range
    @Query("SELECT SUM(ml.actualCarbs) FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.logDate BETWEEN :startDate AND :endDate AND ml.actualCarbs IS NOT NULL")
    Double calculateTotalCarbs(@Param("client") Client client, 
                             @Param("startDate") LocalDate startDate, 
                             @Param("endDate") LocalDate endDate);
    
    // Calculate total fat for client in date range
    @Query("SELECT SUM(ml.actualFat) FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.logDate BETWEEN :startDate AND :endDate AND ml.actualFat IS NOT NULL")
    Double calculateTotalFat(@Param("client") Client client, 
                           @Param("startDate") LocalDate startDate, 
                           @Param("endDate") LocalDate endDate);
    
    // Find logs from nutrition plan
    @Query("SELECT ml FROM MealLog ml WHERE ml.nutritionPlan = :nutritionPlan ORDER BY ml.logDate DESC")
    List<MealLog> findByNutritionPlanOrderByDate(@Param("nutritionPlan") NutritionPlan nutritionPlan);
    
    // Find logs by meal category
    @Query("SELECT ml FROM MealLog ml WHERE ml.client = :client AND " +
           "ml.meal.category = :category ORDER BY ml.logDate DESC")
    List<MealLog> findByClientAndMealCategory(@Param("client") Client client, 
                                            @Param("category") String category);
}
