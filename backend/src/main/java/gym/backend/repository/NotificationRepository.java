package gym.backend.repository;

import gym.backend.model.Notification;
import gym.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find notifications by user
    List<Notification> findByUser(User user);
    
    // Find notifications by user with pagination
    Page<Notification> findByUser(User user, Pageable pageable);
    
    // Additional methods needed by DashboardAnalyticsService
    long countByIsReadFalse();
    
    // Additional methods needed by NotificationService
    List<Notification> findByUser_Id(Long userId);
    List<Notification> findByUser_IdAndIsReadFalse(Long userId);
    List<Notification> findByUser_IdAndIsImportantTrue(Long userId);
    List<Notification> findByType(Notification.NotificationType type);
    List<Notification> findByCategory(Notification.NotificationCategory category);
    List<Notification> findByCreatedAtAfter(LocalDateTime dateTime);
    List<Notification> findByScheduledForBeforeAndIsReadFalse(LocalDateTime dateTime);
    List<Notification> findByExpiresAtBefore(LocalDateTime dateTime);
    
    // Find unread notifications by user
    List<Notification> findByUserAndIsRead(User user, Boolean isRead);
    
    // Find unread notifications by user with pagination
    Page<Notification> findByUserAndIsRead(User user, Boolean isRead, Pageable pageable);
    
    // Find notifications by type
    List<Notification> findByUserAndType(User user, Notification.NotificationType type);
    
    // Find notifications by category
    List<Notification> findByUserAndCategory(User user, Notification.NotificationCategory category);
    
    // Find important notifications
    List<Notification> findByUserAndIsImportant(User user, Boolean isImportant);
    
    // Find notifications by date range
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND " +
           "n.createdAt BETWEEN :startDate AND :endDate ORDER BY n.createdAt DESC")
    List<Notification> findByUserAndDateRange(@Param("user") User user, 
                                            @Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
    
    // Find scheduled notifications that should be sent
    @Query("SELECT n FROM Notification n WHERE n.scheduledFor <= :now AND " +
           "(n.expiresAt IS NULL OR n.expiresAt > :now) ORDER BY n.scheduledFor ASC")
    List<Notification> findScheduledNotificationsToSend(@Param("now") LocalDateTime now);
    
    // Find expired notifications
    @Query("SELECT n FROM Notification n WHERE n.expiresAt IS NOT NULL AND n.expiresAt <= :now")
    List<Notification> findExpiredNotifications(@Param("now") LocalDateTime now);
    
    // Count unread notifications by user
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user = :user AND n.isRead = false")
    Long countUnreadByUser(@Param("user") User user);
    
    // Count important unread notifications by user
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.user = :user AND n.isRead = false AND n.isImportant = true")
    Long countImportantUnreadByUser(@Param("user") User user);
    
    // Mark all notifications as read for user
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.user = :user AND n.isRead = false")
    int markAllAsReadByUser(@Param("user") User user, @Param("readAt") LocalDateTime readAt);
    
    // Mark notification as read
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.id = :id")
    int markAsRead(@Param("id") Long id, @Param("readAt") LocalDateTime readAt);
    
    // Delete expired notifications
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.expiresAt IS NOT NULL AND n.expiresAt <= :now")
    int deleteExpiredNotifications(@Param("now") LocalDateTime now);
    
    // Find notifications by entity type and ID
    @Query("SELECT n FROM Notification n WHERE n.actionData LIKE %:entityType% AND n.actionData LIKE %:entityId%")
    List<Notification> findByEntityTypeAndId(@Param("entityType") String entityType, 
                                           @Param("entityId") String entityId);
    
    // Find recent notifications (last 7 days)
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND " +
           "n.createdAt >= :date ORDER BY n.createdAt DESC")
    List<Notification> findRecentNotifications(@Param("user") User user, 
                                             @Param("date") LocalDateTime date);
    
    // Find notifications by action URL
    List<Notification> findByUserAndActionUrl(User user, String actionUrl);
    
    // Find notifications with action data
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND " +
           "n.actionData IS NOT NULL AND n.actionData != ''")
    List<Notification> findWithActionData(@Param("user") User user);
    
}
