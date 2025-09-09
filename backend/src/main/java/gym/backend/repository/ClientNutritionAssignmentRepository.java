package gym.backend.repository;

import gym.backend.model.Client;
import gym.backend.model.ClientNutritionAssignment;
import gym.backend.model.NutritionPlan;
import gym.backend.model.User;
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
public interface ClientNutritionAssignmentRepository extends JpaRepository<ClientNutritionAssignment, Long> {

    // Find assignments by client
    List<ClientNutritionAssignment> findByClient(Client client);
    
    // Find assignments by client with pagination
    Page<ClientNutritionAssignment> findByClient(Client client, Pageable pageable);
    
    
    // Find assignments by nutrition plan
    List<ClientNutritionAssignment> findByNutritionPlan(NutritionPlan nutritionPlan);
    
    // Find assignments by trainer (assigned by)
    List<ClientNutritionAssignment> findByAssignedBy(User trainer);
    
    // Find active assignments by client
    List<ClientNutritionAssignment> findByClientAndStatus(Client client, ClientNutritionAssignment.AssignmentStatus status);
    
    // Find active assignments by trainer
    List<ClientNutritionAssignment> findByAssignedByAndStatus(User trainer, ClientNutritionAssignment.AssignmentStatus status);
    
    // Find assignments by status
    List<ClientNutritionAssignment> findByStatus(ClientNutritionAssignment.AssignmentStatus status);
    
    // Find assignments by date range
    @Query("SELECT cna FROM ClientNutritionAssignment cna WHERE cna.client = :client AND " +
           "cna.startDate BETWEEN :startDate AND :endDate ORDER BY cna.startDate DESC")
    List<ClientNutritionAssignment> findByClientAndDateRange(@Param("client") Client client, 
                                                          @Param("startDate") LocalDate startDate, 
                                                          @Param("endDate") LocalDate endDate);
    
    // Find current active assignment for client and nutrition plan
    @Query("SELECT cna FROM ClientNutritionAssignment cna WHERE cna.client = :client AND " +
           "cna.nutritionPlan = :nutritionPlan AND cna.status = 'ACTIVE'")
    Optional<ClientNutritionAssignment> findActiveAssignment(@Param("client") Client client, 
                                                          @Param("nutritionPlan") NutritionPlan nutritionPlan);
    
    // Count active assignments by client
    @Query("SELECT COUNT(cna) FROM ClientNutritionAssignment cna WHERE cna.client = :client AND cna.status = 'ACTIVE'")
    Long countActiveAssignmentsByClient(@Param("client") Client client);
    
    // Count assignments by trainer
    @Query("SELECT COUNT(cna) FROM ClientNutritionAssignment cna WHERE cna.assignedBy = :trainer")
    Long countAssignmentsByTrainer(@Param("trainer") User trainer);
    
    // Count active assignments by trainer
    @Query("SELECT COUNT(cna) FROM ClientNutritionAssignment cna WHERE cna.assignedBy = :trainer AND cna.status = 'ACTIVE'")
    Long countActiveAssignmentsByTrainer(@Param("trainer") User trainer);
    
    // Find assignments ending soon
    @Query("SELECT cna FROM ClientNutritionAssignment cna WHERE cna.assignedBy = :trainer AND " +
           "cna.status = 'ACTIVE' AND cna.endDate BETWEEN :startDate AND :endDate")
    List<ClientNutritionAssignment> findAssignmentsEndingSoon(@Param("trainer") User trainer, 
                                                           @Param("startDate") LocalDate startDate, 
                                                           @Param("endDate") LocalDate endDate);
    
    // Find assignments by completion percentage
    @Query("SELECT cna FROM ClientNutritionAssignment cna WHERE cna.assignedBy = :trainer AND " +
           "cna.status = 'ACTIVE' AND cna.completionPercentage >= :minPercentage")
    List<ClientNutritionAssignment> findAssignmentsByCompletionPercentage(@Param("trainer") User trainer, 
                                                                       @Param("minPercentage") Integer minPercentage);
    
    // Find assignments with recent activity
    @Query("SELECT cna FROM ClientNutritionAssignment cna WHERE cna.assignedBy = :trainer AND " +
           "cna.status = 'ACTIVE' AND cna.lastMealLoggedDate >= :date")
    List<ClientNutritionAssignment> findAssignmentsWithRecentActivity(@Param("trainer") User trainer, 
                                                                   @Param("date") LocalDate date);
    
    // Find assignments without recent activity
    @Query("SELECT cna FROM ClientNutritionAssignment cna WHERE cna.assignedBy = :trainer AND " +
           "cna.status = 'ACTIVE' AND (cna.lastMealLoggedDate < :date OR cna.lastMealLoggedDate IS NULL)")
    List<ClientNutritionAssignment> findAssignmentsWithoutRecentActivity(@Param("trainer") User trainer, 
                                                                      @Param("date") LocalDate date);
    
    // Find most popular nutrition plans
    @Query("SELECT cna.nutritionPlan, COUNT(cna) as assignmentCount FROM ClientNutritionAssignment cna " +
           "WHERE cna.assignedBy = :trainer GROUP BY cna.nutritionPlan ORDER BY assignmentCount DESC")
    List<Object[]> findMostPopularNutritionPlans(@Param("trainer") User trainer);
    
    // Find assignments by nutrition plan and status
    List<ClientNutritionAssignment> findByNutritionPlanAndStatus(NutritionPlan nutritionPlan, ClientNutritionAssignment.AssignmentStatus status);
    
    // Find completed assignments by client
    @Query("SELECT cna FROM ClientNutritionAssignment cna WHERE cna.client = :client AND " +
           "cna.status = 'COMPLETED' ORDER BY cna.endDate DESC")
    List<ClientNutritionAssignment> findCompletedAssignmentsByClient(@Param("client") Client client);

    // Additional methods needed by ClientNutritionAssignmentService
    List<ClientNutritionAssignment> findByClient_Id(Long clientId);
    Page<ClientNutritionAssignment> findByClient_Id(Long clientId, Pageable pageable);
    List<ClientNutritionAssignment> findByClient_IdAndStatus(Long clientId, ClientNutritionAssignment.AssignmentStatus status);
    List<ClientNutritionAssignment> findByNutritionPlan_Id(Long nutritionPlanId);
    List<ClientNutritionAssignment> findByAssignedBy_Id(Long assignedById);
    List<ClientNutritionAssignment> findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndStatus(LocalDate startDate, LocalDate endDate, ClientNutritionAssignment.AssignmentStatus status);
    List<ClientNutritionAssignment> findByEndDateBeforeAndStatus(LocalDate date, ClientNutritionAssignment.AssignmentStatus status);
    List<ClientNutritionAssignment> findByStartDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, ClientNutritionAssignment.AssignmentStatus status);
    List<ClientNutritionAssignment> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
