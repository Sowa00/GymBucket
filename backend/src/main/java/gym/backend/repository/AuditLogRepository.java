package gym.backend.repository;

import gym.backend.model.AuditLog;
import gym.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Find logs by user
    List<AuditLog> findByUser(User user);
    
    // Find logs by user with pagination
    Page<AuditLog> findByUser(User user, Pageable pageable);
    
    // Additional methods needed by AuditLogService
    List<AuditLog> findByUser_Id(Long userId);
    Page<AuditLog> findByUser_Id(Long userId, Pageable pageable);
    
    // Find logs by action
    List<AuditLog> findByAction(String action);
    
    // Additional methods needed by AuditLogService
    List<AuditLog> findByActionType(AuditLog.ActionType actionType);
    List<AuditLog> findByCreatedAtAfter(LocalDateTime timestamp);
    List<AuditLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> findByCreatedAtBefore(LocalDateTime timestamp);
    List<AuditLog> findByActionContainingIgnoreCase(String action);
    
    // Find logs by entity type
    List<AuditLog> findByEntityType(String entityType);
    
    // Find logs by entity ID
    List<AuditLog> findByEntityId(Long entityId);
    
    // Find logs by entity type and ID
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
    
    // Find logs by IP address
    List<AuditLog> findByIpAddress(String ipAddress);
    
    // Find logs by session ID
    List<AuditLog> findBySessionId(String sessionId);
    
    // Find logs by date range
    @Query("SELECT al FROM AuditLog al WHERE al.createdAt BETWEEN :startDate AND :endDate " +
           "ORDER BY al.createdAt DESC")
    List<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate);
    
    // Find logs by user and date range
    @Query("SELECT al FROM AuditLog al WHERE al.user = :user AND " +
           "al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<AuditLog> findByUserAndDateRange(@Param("user") User user, 
                                        @Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);
    
    // Find logs by entity type and date range
    @Query("SELECT al FROM AuditLog al WHERE al.entityType = :entityType AND " +
           "al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<AuditLog> findByEntityTypeAndDateRange(@Param("entityType") String entityType, 
                                              @Param("startDate") LocalDateTime startDate, 
                                              @Param("endDate") LocalDateTime endDate);
    
    // Find recent logs (last 24 hours)
    @Query("SELECT al FROM AuditLog al WHERE al.createdAt >= :date ORDER BY al.createdAt DESC")
    List<AuditLog> findRecentLogs(@Param("date") LocalDateTime date);
    
    // Find recent logs by user
    @Query("SELECT al FROM AuditLog al WHERE al.user = :user AND al.createdAt >= :date " +
           "ORDER BY al.createdAt DESC")
    List<AuditLog> findRecentLogsByUser(@Param("user") User user, 
                                      @Param("date") LocalDateTime date);
    
    // Count logs by user
    Long countByUser(User user);
    
    // Count logs by action
    Long countByAction(String action);
    
    // Count logs by entity type
    Long countByEntityType(String entityType);
    
    // Count logs by IP address
    Long countByIpAddress(String ipAddress);
    
    // Find login logs
    @Query("SELECT al FROM AuditLog al WHERE al.action = 'LOGIN' ORDER BY al.createdAt DESC")
    List<AuditLog> findLoginLogs();
    
    // Find logout logs
    @Query("SELECT al FROM AuditLog al WHERE al.action = 'LOGOUT' ORDER BY al.createdAt DESC")
    List<AuditLog> findLogoutLogs();
    
    // Find create logs
    @Query("SELECT al FROM AuditLog al WHERE al.action = 'CREATE' ORDER BY al.createdAt DESC")
    List<AuditLog> findCreateLogs();
    
    // Find update logs
    @Query("SELECT al FROM AuditLog al WHERE al.action = 'UPDATE' ORDER BY al.createdAt DESC")
    List<AuditLog> findUpdateLogs();
    
    // Find delete logs
    @Query("SELECT al FROM AuditLog al WHERE al.action = 'DELETE' ORDER BY al.createdAt DESC")
    List<AuditLog> findDeleteLogs();
    
    // Find logs by user and action
    List<AuditLog> findByUserAndAction(User user, String action);
    
    // Find logs by user and entity type
    List<AuditLog> findByUserAndEntityType(User user, String entityType);
    
    // Find logs with changes (old or new values)
    @Query("SELECT al FROM AuditLog al WHERE al.oldValues IS NOT NULL OR al.newValues IS NOT NULL " +
           "ORDER BY al.createdAt DESC")
    List<AuditLog> findLogsWithChanges();
    
    // Find logs by user agent
    List<AuditLog> findByUserAgentContaining(String userAgent);
    
    // Find logs for specific entity
    @Query("SELECT al FROM AuditLog al WHERE al.entityType = :entityType AND al.entityId = :entityId " +
           "ORDER BY al.createdAt DESC")
    List<AuditLog> findLogsForEntity(@Param("entityType") String entityType, 
                                   @Param("entityId") Long entityId);
    
    // Find logs by action and entity type
    List<AuditLog> findByActionAndEntityType(String action, String entityType);
    
    // Find logs by action and user
    List<AuditLog> findByActionAndUser(String action, User user);
    
    // Find logs by IP address and date range
    @Query("SELECT al FROM AuditLog al WHERE al.ipAddress = :ipAddress AND " +
           "al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<AuditLog> findByIpAddressAndDateRange(@Param("ipAddress") String ipAddress, 
                                             @Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate);
}
