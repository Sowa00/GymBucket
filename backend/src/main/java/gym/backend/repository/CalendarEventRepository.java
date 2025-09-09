package gym.backend.repository;

import gym.backend.model.CalendarEvent;
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
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    // Find events by trainer
    List<CalendarEvent> findByTrainer(User trainer);
    
    // Find events by trainer with pagination
    Page<CalendarEvent> findByTrainer(User trainer, Pageable pageable);
    
    // Find events by client
    List<CalendarEvent> findByClient_Id(Long clientId);
    
    // Additional methods needed by CalendarEventService
    Page<CalendarEvent> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    List<CalendarEvent> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<CalendarEvent> findByTrainer_Id(Long trainerId);
    List<CalendarEvent> findByStartDate(LocalDate startDate);
    
    // Find events by date range
    @Query("SELECT ce FROM CalendarEvent ce WHERE ce.trainer = :trainer AND " +
           "ce.startDate BETWEEN :startDate AND :endDate ORDER BY ce.startDate, ce.startTime")
    List<CalendarEvent> findByTrainerAndDateRange(@Param("trainer") User trainer, 
                                                  @Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);
    
    // Find events for today
    @Query("SELECT ce FROM CalendarEvent ce WHERE ce.trainer = :trainer AND " +
           "ce.startDate = :date ORDER BY ce.startTime")
    List<CalendarEvent> findByTrainerAndDate(@Param("trainer") User trainer, 
                                            @Param("date") LocalDate date);
    
    // Find upcoming events
    @Query("SELECT ce FROM CalendarEvent ce WHERE ce.trainer = :trainer AND " +
           "ce.startDate >= :date AND ce.status = 'SCHEDULED' ORDER BY ce.startDate, ce.startTime")
    List<CalendarEvent> findUpcomingEvents(@Param("trainer") User trainer, 
                                          @Param("date") LocalDate date);
    
    // Find events by status
    List<CalendarEvent> findByTrainerAndStatus(User trainer, CalendarEvent.EventStatus status);
    
    // Find events by type
    List<CalendarEvent> findByTrainerAndEventType(User trainer, CalendarEvent.EventType eventType);
    
    // Find recurring events
    List<CalendarEvent> findByTrainerAndIsRecurring(User trainer, Boolean isRecurring);
    
    // Count events by status
    @Query("SELECT COUNT(ce) FROM CalendarEvent ce WHERE ce.trainer = :trainer AND ce.status = :status")
    Long countByTrainerAndStatus(@Param("trainer") User trainer, 
                                @Param("status") CalendarEvent.EventStatus status);
    
    // Count events for today
    @Query("SELECT COUNT(ce) FROM CalendarEvent ce WHERE ce.trainer = :trainer AND " +
           "ce.startDate = :date AND ce.status IN ('SCHEDULED', 'CONFIRMED')")
    Long countTodayEvents(@Param("trainer") User trainer, @Param("date") LocalDate date);
    
    // Find events with reminders
    @Query("SELECT ce FROM CalendarEvent ce WHERE ce.trainer = :trainer AND " +
           "ce.startDate = :date AND ce.reminderMinutes > 0")
    List<CalendarEvent> findEventsWithReminders(@Param("trainer") User trainer, 
                                               @Param("date") LocalDate date);
    
    // Find events by workout plan
    List<CalendarEvent> findByWorkoutPlan_Id(Long workoutPlanId);
    
    // Find events by nutrition plan
    List<CalendarEvent> findByNutritionPlan_Id(Long nutritionPlanId);
    
    // Find events by priority
    List<CalendarEvent> findByTrainerAndPriority(User trainer, CalendarEvent.Priority priority);
    
    // Find overdue events
    @Query("SELECT ce FROM CalendarEvent ce WHERE ce.trainer = :trainer AND " +
           "ce.startDate < :date AND ce.status = 'SCHEDULED'")
    List<CalendarEvent> findOverdueEvents(@Param("trainer") User trainer, 
                                         @Param("date") LocalDate date);
}
