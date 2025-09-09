package gym.backend.repository;

import gym.backend.model.Client;
import gym.backend.model.ClientWorkoutAssignment;
import gym.backend.model.User;
import gym.backend.model.WorkoutPlan;
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
public interface ClientWorkoutAssignmentRepository extends JpaRepository<ClientWorkoutAssignment, Long> {

    // Find assignments by client
    List<ClientWorkoutAssignment> findByClient(Client client);
    
    // Find assignments by client with pagination
    Page<ClientWorkoutAssignment> findByClient(Client client, Pageable pageable);
    
    // Additional methods needed by ClientWorkoutAssignmentService
    List<ClientWorkoutAssignment> findByClient_Id(Long clientId);
    List<ClientWorkoutAssignment> findByClient_IdAndStatus(Long clientId, ClientWorkoutAssignment.AssignmentStatus status);
    List<ClientWorkoutAssignment> findByWorkoutPlan_Id(Long workoutPlanId);
    List<ClientWorkoutAssignment> findByAssignedBy_Id(Long assignedById);
    List<ClientWorkoutAssignment> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<ClientWorkoutAssignment> findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndStatus(LocalDate startDate, LocalDate endDate, ClientWorkoutAssignment.AssignmentStatus status);
    List<ClientWorkoutAssignment> findByEndDateBeforeAndStatus(LocalDate date, ClientWorkoutAssignment.AssignmentStatus status);
    List<ClientWorkoutAssignment> findByStartDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, ClientWorkoutAssignment.AssignmentStatus status);
    
    // Find assignments by workout plan
    List<ClientWorkoutAssignment> findByWorkoutPlan(WorkoutPlan workoutPlan);
    
    // Find assignments by trainer (assigned by)
    List<ClientWorkoutAssignment> findByAssignedBy(User trainer);
    
    // Find active assignments by client
    List<ClientWorkoutAssignment> findByClientAndStatus(Client client, ClientWorkoutAssignment.AssignmentStatus status);
    
    // Find active assignments by trainer
    List<ClientWorkoutAssignment> findByAssignedByAndStatus(User trainer, ClientWorkoutAssignment.AssignmentStatus status);
    
    // Find assignments by status
    List<ClientWorkoutAssignment> findByStatus(ClientWorkoutAssignment.AssignmentStatus status);
    
    // Find assignments by date range
    @Query("SELECT cwa FROM ClientWorkoutAssignment cwa WHERE cwa.client = :client AND " +
           "cwa.startDate BETWEEN :startDate AND :endDate ORDER BY cwa.startDate DESC")
    List<ClientWorkoutAssignment> findByClientAndDateRange(@Param("client") Client client, 
                                                          @Param("startDate") LocalDate startDate, 
                                                          @Param("endDate") LocalDate endDate);
    
    // Find current active assignment for client and workout plan
    @Query("SELECT cwa FROM ClientWorkoutAssignment cwa WHERE cwa.client = :client AND " +
           "cwa.workoutPlan = :workoutPlan AND cwa.status = 'ACTIVE'")
    Optional<ClientWorkoutAssignment> findActiveAssignment(@Param("client") Client client, 
                                                          @Param("workoutPlan") WorkoutPlan workoutPlan);
    
    // Count active assignments by client
    @Query("SELECT COUNT(cwa) FROM ClientWorkoutAssignment cwa WHERE cwa.client = :client AND cwa.status = 'ACTIVE'")
    Long countActiveAssignmentsByClient(@Param("client") Client client);
    
    // Count assignments by trainer
    @Query("SELECT COUNT(cwa) FROM ClientWorkoutAssignment cwa WHERE cwa.assignedBy = :trainer")
    Long countAssignmentsByTrainer(@Param("trainer") User trainer);
    
    // Count active assignments by trainer
    @Query("SELECT COUNT(cwa) FROM ClientWorkoutAssignment cwa WHERE cwa.assignedBy = :trainer AND cwa.status = 'ACTIVE'")
    Long countActiveAssignmentsByTrainer(@Param("trainer") User trainer);
    
    // Find assignments ending soon
    @Query("SELECT cwa FROM ClientWorkoutAssignment cwa WHERE cwa.assignedBy = :trainer AND " +
           "cwa.status = 'ACTIVE' AND cwa.endDate BETWEEN :startDate AND :endDate")
    List<ClientWorkoutAssignment> findAssignmentsEndingSoon(@Param("trainer") User trainer, 
                                                           @Param("startDate") LocalDate startDate, 
                                                           @Param("endDate") LocalDate endDate);
    
    // Find assignments by completion percentage
    @Query("SELECT cwa FROM ClientWorkoutAssignment cwa WHERE cwa.assignedBy = :trainer AND " +
           "cwa.status = 'ACTIVE' AND cwa.completionPercentage >= :minPercentage")
    List<ClientWorkoutAssignment> findAssignmentsByCompletionPercentage(@Param("trainer") User trainer, 
                                                                       @Param("minPercentage") Integer minPercentage);
    
    // Find assignments with recent activity
    @Query("SELECT cwa FROM ClientWorkoutAssignment cwa WHERE cwa.assignedBy = :trainer AND " +
           "cwa.status = 'ACTIVE' AND cwa.lastWorkoutDate >= :date")
    List<ClientWorkoutAssignment> findAssignmentsWithRecentActivity(@Param("trainer") User trainer, 
                                                                   @Param("date") LocalDate date);
    
    // Find assignments without recent activity
    @Query("SELECT cwa FROM ClientWorkoutAssignment cwa WHERE cwa.assignedBy = :trainer AND " +
           "cwa.status = 'ACTIVE' AND (cwa.lastWorkoutDate < :date OR cwa.lastWorkoutDate IS NULL)")
    List<ClientWorkoutAssignment> findAssignmentsWithoutRecentActivity(@Param("trainer") User trainer, 
                                                                      @Param("date") LocalDate date);
    
    // Find most popular workout plans
    @Query("SELECT cwa.workoutPlan, COUNT(cwa) as assignmentCount FROM ClientWorkoutAssignment cwa " +
           "WHERE cwa.assignedBy = :trainer GROUP BY cwa.workoutPlan ORDER BY assignmentCount DESC")
    List<Object[]> findMostPopularWorkoutPlans(@Param("trainer") User trainer);
    
    // Find assignments by workout plan and status
    List<ClientWorkoutAssignment> findByWorkoutPlanAndStatus(WorkoutPlan workoutPlan, ClientWorkoutAssignment.AssignmentStatus status);
    
    // Find completed assignments by client
    @Query("SELECT cwa FROM ClientWorkoutAssignment cwa WHERE cwa.client = :client AND " +
           "cwa.status = 'COMPLETED' ORDER BY cwa.endDate DESC")
    List<ClientWorkoutAssignment> findCompletedAssignmentsByClient(@Param("client") Client client);
}
