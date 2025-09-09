package gym.backend.repository;

import gym.backend.model.Client;
import gym.backend.model.TrainingSession;
import gym.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long> {

    // Find sessions by trainer
    List<TrainingSession> findByTrainer(User trainer);
    
    Page<TrainingSession> findByTrainer(User trainer, Pageable pageable);

    // Find sessions by client
    List<TrainingSession> findByClient(Client client);
    
    Page<TrainingSession> findByClient(Client client, Pageable pageable);

    // Find sessions by date
    List<TrainingSession> findBySessionDate(LocalDate sessionDate);
    
    List<TrainingSession> findByTrainerAndSessionDate(User trainer, LocalDate sessionDate);
    
    List<TrainingSession> findByClientAndSessionDate(Client client, LocalDate sessionDate);

    // Find sessions by date range
    List<TrainingSession> findByTrainerAndSessionDateBetween(User trainer, LocalDate startDate, LocalDate endDate);
    
    List<TrainingSession> findByClientAndSessionDateBetween(Client client, LocalDate startDate, LocalDate endDate);

    // Find sessions by status
    List<TrainingSession> findByTrainerAndStatus(User trainer, TrainingSession.SessionStatus status);
    
    List<TrainingSession> findByClientAndStatus(Client client, TrainingSession.SessionStatus status);

    // Find upcoming sessions
    @Query("SELECT ts FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.sessionDate >= CURRENT_DATE AND ts.status IN ('SCHEDULED', 'CONFIRMED') " +
           "ORDER BY ts.sessionDate ASC, ts.startTime ASC")
    List<TrainingSession> findUpcomingSessionsByTrainer(@Param("trainer") User trainer);

    @Query("SELECT ts FROM TrainingSession ts WHERE ts.client = :client AND " +
           "ts.sessionDate >= CURRENT_DATE AND ts.status IN ('SCHEDULED', 'CONFIRMED') " +
           "ORDER BY ts.sessionDate ASC, ts.startTime ASC")
    List<TrainingSession> findUpcomingSessionsByClient(@Param("client") Client client);

    // Find today's sessions
    @Query("SELECT ts FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.sessionDate = CURRENT_DATE ORDER BY ts.startTime ASC")
    List<TrainingSession> findTodaysSessionsByTrainer(@Param("trainer") User trainer);

    @Query("SELECT ts FROM TrainingSession ts WHERE ts.client = :client AND " +
           "ts.sessionDate = CURRENT_DATE ORDER BY ts.startTime ASC")
    List<TrainingSession> findTodaysSessionsByClient(@Param("client") Client client);

    // Find sessions by time range
    @Query("SELECT ts FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.sessionDate = :date AND ts.startTime >= :startTime AND ts.endTime <= :endTime")
    List<TrainingSession> findByTrainerAndDateAndTimeRange(@Param("trainer") User trainer, 
                                                          @Param("date") LocalDate date,
                                                          @Param("startTime") LocalTime startTime,
                                                          @Param("endTime") LocalTime endTime);

    // Check for time conflicts
    @Query("SELECT ts FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.sessionDate = :date AND ts.status NOT IN ('CANCELLED', 'NO_SHOW') AND " +
           "((ts.startTime < :endTime AND ts.endTime > :startTime))")
    List<TrainingSession> findConflictingSessions(@Param("trainer") User trainer,
                                                 @Param("date") LocalDate date,
                                                 @Param("startTime") LocalTime startTime,
                                                 @Param("endTime") LocalTime endTime);

    // Find sessions by session type
    List<TrainingSession> findByTrainerAndSessionType(User trainer, String sessionType);
    
    List<TrainingSession> findByClientAndSessionType(Client client, String sessionType);

    // Find sessions by location
    List<TrainingSession> findByTrainerAndLocation(User trainer, String location);

    // Find paid/unpaid sessions
    List<TrainingSession> findByTrainerAndIsPaid(User trainer, Boolean isPaid);
    
    List<TrainingSession> findByClientAndIsPaid(Client client, Boolean isPaid);

    // Find sessions with ratings
    @Query("SELECT ts FROM TrainingSession ts WHERE ts.trainer = :trainer AND ts.rating IS NOT NULL")
    List<TrainingSession> findRatedSessionsByTrainer(@Param("trainer") User trainer);

    @Query("SELECT ts FROM TrainingSession ts WHERE ts.client = :client AND ts.rating IS NOT NULL")
    List<TrainingSession> findRatedSessionsByClient(@Param("client") Client client);

    // Statistics queries
    @Query("SELECT COUNT(ts) FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.sessionDate BETWEEN :startDate AND :endDate")
    Long countSessionsByTrainerAndDateRange(@Param("trainer") User trainer,
                                           @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(ts) FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.status = 'COMPLETED' AND ts.sessionDate BETWEEN :startDate AND :endDate")
    Long countCompletedSessionsByTrainerAndDateRange(@Param("trainer") User trainer,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(ts.rating) FROM TrainingSession ts WHERE ts.trainer = :trainer AND ts.rating IS NOT NULL")
    Double getAverageRatingByTrainer(@Param("trainer") User trainer);

    @Query("SELECT SUM(ts.price) FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.isPaid = true AND ts.sessionDate BETWEEN :startDate AND :endDate")
    Double getTotalRevenueByTrainerAndDateRange(@Param("trainer") User trainer,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(ts) FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.status = 'NO_SHOW' AND ts.sessionDate BETWEEN :startDate AND :endDate")
    Long countNoShowSessionsByTrainerAndDateRange(@Param("trainer") User trainer,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    // Find sessions by client and trainer
    List<TrainingSession> findByClientAndTrainer(Client client, User trainer);

    // Find recent sessions
    @Query("SELECT ts FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.sessionDate >= :since ORDER BY ts.sessionDate DESC, ts.startTime DESC")
    List<TrainingSession> findRecentSessionsByTrainer(@Param("trainer") User trainer, @Param("since") LocalDate since);

    // Find sessions needing feedback
    @Query("SELECT ts FROM TrainingSession ts WHERE ts.trainer = :trainer AND " +
           "ts.status = 'COMPLETED' AND ts.feedback IS NULL AND ts.sessionDate < CURRENT_DATE")
    List<TrainingSession> findSessionsNeedingFeedbackByTrainer(@Param("trainer") User trainer);
}
