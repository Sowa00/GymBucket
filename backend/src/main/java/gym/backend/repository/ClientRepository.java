package gym.backend.repository;

import gym.backend.model.Client;
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
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Find clients by trainer
    List<Client> findByTrainer(User trainer);
    
    Page<Client> findByTrainer(User trainer, Pageable pageable);
    
    List<Client> findByTrainerAndIsActive(User trainer, Boolean isActive);
    
    Page<Client> findByTrainerAndIsActive(User trainer, Boolean isActive, Pageable pageable);

    // Find by email
    Optional<Client> findByEmail(String email);
    
    // Find client with trainer eagerly loaded
    @Query("SELECT c FROM Client c JOIN FETCH c.trainer WHERE c.id = :clientId")
    Optional<Client> findByIdWithTrainer(@Param("clientId") Long clientId);
    
    boolean existsByEmail(String email);
    
    // Check if email exists among active clients only
    boolean existsByEmailAndIsActive(String email, Boolean isActive);
    
    // Find client by email and active status
    Optional<Client> findByEmailAndIsActive(String email, Boolean isActive);

    // Search clients by name
    @Query("SELECT c FROM Client c WHERE c.trainer = :trainer AND c.isActive = true AND " +
           "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Client> findByTrainerAndNameContaining(@Param("trainer") User trainer, @Param("searchTerm") String searchTerm);

    // Find clients with upcoming sessions
    @Query("SELECT DISTINCT c FROM Client c JOIN c.trainer t WHERE t = :trainer AND c.isActive = true")
    List<Client> findActiveClientsByTrainer(@Param("trainer") User trainer);

    // Find clients by start date range
    List<Client> findByTrainerAndStartDateBetween(User trainer, LocalDate startDate, LocalDate endDate);

    // Find clients by payment status
    List<Client> findByTrainerAndPaymentStatus(User trainer, String paymentStatus);

    // Count clients by trainer
    long countByTrainer(User trainer);
    
    long countByTrainerAndIsActive(User trainer, Boolean isActive);

    // Find clients with recent sessions
    @Query("SELECT c FROM Client c WHERE c.trainer = :trainer AND c.isActive = true AND c.lastSessionDate >= :since")
    List<Client> findClientsWithRecentSessions(@Param("trainer") User trainer, @Param("since") LocalDate since);

    // Find clients without recent sessions
    @Query("SELECT c FROM Client c WHERE c.trainer = :trainer AND c.isActive = true AND (c.lastSessionDate < :since OR c.lastSessionDate IS NULL)")
    List<Client> findClientsWithoutRecentSessions(@Param("trainer") User trainer, @Param("since") LocalDate since);

    // Find clients by age range
    @Query("SELECT c FROM Client c WHERE c.trainer = :trainer AND c.isActive = true AND " +
           "YEAR(CURRENT_DATE) - YEAR(c.dateOfBirth) BETWEEN :minAge AND :maxAge")
    List<Client> findByTrainerAndAgeBetween(@Param("trainer") User trainer, 
                                           @Param("minAge") Integer minAge, 
                                           @Param("maxAge") Integer maxAge);

    // Find clients by gender
    List<Client> findByTrainerAndGenderAndIsActive(User trainer, String gender, Boolean isActive);

    // Find clients with specific fitness goals
    @Query("SELECT c FROM Client c WHERE c.trainer = :trainer AND c.isActive = true AND " +
           "LOWER(c.fitnessGoals) LIKE LOWER(CONCAT('%', :goal, '%'))")
    List<Client> findByTrainerAndFitnessGoalsContaining(@Param("trainer") User trainer, @Param("goal") String goal);

    // Find clients with medical conditions
    @Query("SELECT c FROM Client c WHERE c.trainer = :trainer AND c.isActive = true AND c.medicalConditions IS NOT NULL AND c.medicalConditions != ''")
    List<Client> findByTrainerWithMedicalConditions(@Param("trainer") User trainer);

    // Statistics queries
    @Query("SELECT AVG(c.totalSessions) FROM Client c WHERE c.trainer = :trainer AND c.isActive = true")
    Double getAverageSessionsByTrainer(@Param("trainer") User trainer);

    @Query("SELECT SUM(c.monthlyFee) FROM Client c WHERE c.trainer = :trainer AND c.isActive = true")
    Double getTotalMonthlyRevenueByTrainer(@Param("trainer") User trainer);

    @Query("SELECT COUNT(c) FROM Client c WHERE c.trainer = :trainer AND c.isActive = true AND c.paymentStatus = 'PAID'")
    Long countPaidClientsByTrainer(@Param("trainer") User trainer);

    @Query("SELECT COUNT(c) FROM Client c WHERE c.trainer = :trainer AND c.isActive = true AND c.paymentStatus = 'OVERDUE'")
    Long countOverdueClientsByTrainer(@Param("trainer") User trainer);
}
