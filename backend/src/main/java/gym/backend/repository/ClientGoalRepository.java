package gym.backend.repository;

import gym.backend.model.Client;
import gym.backend.model.ClientGoal;
import gym.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientGoalRepository extends JpaRepository<ClientGoal, Long> {

    // Find goals by client
    List<ClientGoal> findByClient(Client client);
    
    // Find goals by client with pagination
    Page<ClientGoal> findByClient(Client client, Pageable pageable);
    
    // Find goals by created by user
    List<ClientGoal> findByCreatedBy(User user);
    
    // Find goals by status
    List<ClientGoal> findByStatus(ClientGoal.GoalStatus status);
    
    // Find goals by client and status
    List<ClientGoal> findByClientAndStatus(Client client, ClientGoal.GoalStatus status);
    
    // Find goals by goal type
    List<ClientGoal> findByGoalType(ClientGoal.GoalType goalType);
    
    // Find goals by client and goal type
    List<ClientGoal> findByClientAndGoalType(Client client, ClientGoal.GoalType goalType);
    
    // Find goals by priority
    List<ClientGoal> findByPriority(ClientGoal.Priority priority);
    
    // Find goals by client and priority
    List<ClientGoal> findByClientAndPriority(Client client, ClientGoal.Priority priority);
    
    // Find active goals by client
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND cg.status = 'ACTIVE' " +
           "ORDER BY cg.priority DESC, cg.targetDate ASC")
    List<ClientGoal> findActiveGoalsByClient(@Param("client") Client client);
    
    // Find completed goals by client
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND cg.status = 'COMPLETED' " +
           "ORDER BY cg.completedAt DESC")
    List<ClientGoal> findCompletedGoalsByClient(@Param("client") Client client);
    
    // Find goals by target date range
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND " +
           "cg.targetDate BETWEEN :startDate AND :endDate ORDER BY cg.targetDate ASC")
    List<ClientGoal> findByClientAndTargetDateRange(@Param("client") Client client, 
                                                  @Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);
    
    // Find overdue goals
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND " +
           "cg.status = 'ACTIVE' AND cg.targetDate < :date ORDER BY cg.targetDate ASC")
    List<ClientGoal> findOverdueGoals(@Param("client") Client client, @Param("date") LocalDate date);
    
    // Find goals due soon
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND " +
           "cg.status = 'ACTIVE' AND cg.targetDate BETWEEN :startDate AND :endDate " +
           "ORDER BY cg.targetDate ASC")
    List<ClientGoal> findGoalsDueSoon(@Param("client") Client client, 
                                    @Param("startDate") LocalDate startDate, 
                                    @Param("endDate") LocalDate endDate);
    
    // Find goals by created by user and status
    List<ClientGoal> findByCreatedByAndStatus(User user, ClientGoal.GoalStatus status);
    
    // Find goals by created by user and goal type
    List<ClientGoal> findByCreatedByAndGoalType(User user, ClientGoal.GoalType goalType);
    
    // Count goals by client
    Long countByClient(Client client);
    
    // Count goals by client and status
    Long countByClientAndStatus(Client client, ClientGoal.GoalStatus status);
    
    // Count goals by created by user
    Long countByCreatedBy(User user);
    
    // Count active goals by client
    @Query("SELECT COUNT(cg) FROM ClientGoal cg WHERE cg.client = :client AND cg.status = 'ACTIVE'")
    Long countActiveGoalsByClient(@Param("client") Client client);
    
    // Count completed goals by client
    @Query("SELECT COUNT(cg) FROM ClientGoal cg WHERE cg.client = :client AND cg.status = 'COMPLETED'")
    Long countCompletedGoalsByClient(@Param("client") Client client);
    
    // Find goals by date range
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND " +
           "cg.createdAt BETWEEN :startDate AND :endDate ORDER BY cg.createdAt DESC")
    List<ClientGoal> findByClientAndCreatedDateRange(@Param("client") Client client, 
                                                   @Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate);
    
    // Find goals by completion date range
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND " +
           "cg.completedAt BETWEEN :startDate AND :endDate ORDER BY cg.completedAt DESC")
    List<ClientGoal> findByClientAndCompletedDateRange(@Param("client") Client client, 
                                                     @Param("startDate") LocalDate startDate, 
                                                     @Param("endDate") LocalDate endDate);
    
    // Find goals with progress
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND " +
           "cg.currentValue IS NOT NULL AND cg.targetValue IS NOT NULL " +
           "ORDER BY cg.priority DESC, cg.targetDate ASC")
    List<ClientGoal> findGoalsWithProgress(@Param("client") Client client);
    
    // Find goals without progress
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND " +
           "(cg.currentValue IS NULL OR cg.targetValue IS NULL) " +
           "ORDER BY cg.priority DESC, cg.targetDate ASC")
    List<ClientGoal> findGoalsWithoutProgress(@Param("client") Client client);
    
    // Find high priority goals
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.client = :client AND " +
           "cg.priority IN ('HIGH', 'URGENT') AND cg.status = 'ACTIVE' " +
           "ORDER BY cg.priority DESC, cg.targetDate ASC")
    List<ClientGoal> findHighPriorityGoals(@Param("client") Client client);
    
    // Find goals by goal type and status
    List<ClientGoal> findByGoalTypeAndStatus(ClientGoal.GoalType goalType, ClientGoal.GoalStatus status);
    
    // Find goals by created by user and date range
    @Query("SELECT cg FROM ClientGoal cg WHERE cg.createdBy = :user AND " +
           "cg.createdAt BETWEEN :startDate AND :endDate ORDER BY cg.createdAt DESC")
    List<ClientGoal> findByCreatedByAndDateRange(@Param("user") User user, 
                                               @Param("startDate") LocalDate startDate, 
                                               @Param("endDate") LocalDate endDate);

    // Additional methods needed by ClientGoalService
    List<ClientGoal> findByClient_Id(Long clientId);
    Page<ClientGoal> findByClient_Id(Long clientId, Pageable pageable);
    List<ClientGoal> findByClient_IdAndStatus(Long clientId, ClientGoal.GoalStatus status);
    List<ClientGoal> findByCategory(ClientGoal.GoalCategory category);
    List<ClientGoal> findByAssignedBy_Id(Long assignedById);
    List<ClientGoal> findByIsPublicTrue();
    List<ClientGoal> findByIsPublicFalse();
    List<ClientGoal> findByTargetDateBeforeAndStatus(LocalDate date, ClientGoal.GoalStatus status);
    List<ClientGoal> findByTargetDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, ClientGoal.GoalStatus status);
    List<ClientGoal> findByStartDateAfter(LocalDate date);
    List<ClientGoal> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
