package gym.backend.service;

import gym.backend.dto.AuditLogDTO;
import gym.backend.dto.AuditLogRequestDTO;
import gym.backend.model.AuditLog;
import gym.backend.model.User;
import gym.backend.repository.AuditLogRepository;
import gym.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuditLogService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);
    
    @Autowired
    private AuditLogRepository auditLogRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Get all audit logs with pagination
    public Page<AuditLogDTO> getAllAuditLogs(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all audit logs with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AuditLog> auditLogs = auditLogRepository.findAll(pageable);
        
        return auditLogs.map(this::convertToDTO);
    }

    // Get all audit logs as list
    public List<AuditLogDTO> getAllAuditLogsList() {
        logger.info("Fetching all audit logs as list");
        List<AuditLog> auditLogs = auditLogRepository.findAll();
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit log by ID
    public Optional<AuditLogDTO> getAuditLogById(Long id) {
        logger.info("Fetching audit log by ID: {}", id);
        return auditLogRepository.findById(id).map(this::convertToDTO);
    }

    // Create new audit log
    public AuditLogDTO createAuditLog(AuditLogRequestDTO request) {
        logger.info("Creating new audit log for user: {} - action: {}", request.getUserId(), request.getAction());
        
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + request.getUserId());
        }
        
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(request.getUserId());
        auditLog.setAction(request.getAction());
        auditLog.setEntityType(request.getEntityType());
        auditLog.setEntityId(request.getEntityId());
        auditLog.setEntityName(request.getEntityName());
        auditLog.setOldValues(request.getOldValues());
        auditLog.setNewValues(request.getNewValues());
        auditLog.setIpAddress(request.getIpAddress());
        auditLog.setUserAgent(request.getUserAgent());
        auditLog.setActionType(request.getActionType());
        auditLog.setDescription(request.getDescription());
        auditLog.setTimestamp(LocalDateTime.now());
        
        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        logger.info("Audit log created successfully with ID: {}", savedAuditLog.getId());
        
        return convertToDTO(savedAuditLog);
    }

    // Get audit logs by user
    public List<AuditLogDTO> getAuditLogsByUser(Long userId) {
        logger.info("Fetching audit logs for user: {}", userId);
        
        List<AuditLog> auditLogs = auditLogRepository.findByUser_Id(userId);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit logs by user with pagination
    public Page<AuditLogDTO> getAuditLogsByUser(Long userId, int page, int size) {
        logger.info("Fetching audit logs for user: {} with pagination", userId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<AuditLog> auditLogs = auditLogRepository.findByUser_Id(userId, pageable);
        
        return auditLogs.map(this::convertToDTO);
    }

    // Get audit logs by action type
    public List<AuditLogDTO> getAuditLogsByActionType(AuditLog.ActionType actionType) {
        logger.info("Fetching audit logs by action type: {}", actionType);
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionType(actionType);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit logs by entity type
    public List<AuditLogDTO> getAuditLogsByEntityType(String entityType) {
        logger.info("Fetching audit logs by entity type: {}", entityType);
        
        List<AuditLog> auditLogs = auditLogRepository.findByEntityType(entityType);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit logs by entity ID
    public List<AuditLogDTO> getAuditLogsByEntityId(Long entityId) {
        logger.info("Fetching audit logs by entity ID: {}", entityId);
        
        List<AuditLog> auditLogs = auditLogRepository.findByEntityId(entityId);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit logs by entity type and ID
    public List<AuditLogDTO> getAuditLogsByEntityTypeAndId(String entityType, Long entityId) {
        logger.info("Fetching audit logs by entity type: {} and ID: {}", entityType, entityId);
        
        List<AuditLog> auditLogs = auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent audit logs
    public List<AuditLogDTO> getRecentAuditLogs(int hours) {
        logger.info("Fetching recent audit logs from last {} hours", hours);
        
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<AuditLog> auditLogs = auditLogRepository.findByCreatedAtAfter(since);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get today's audit logs
    public List<AuditLogDTO> getTodaysAuditLogs() {
        logger.info("Fetching today's audit logs");
        
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        List<AuditLog> auditLogs = auditLogRepository.findByCreatedAtBetween(startOfDay, endOfDay);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this week's audit logs
    public List<AuditLogDTO> getThisWeeksAuditLogs() {
        logger.info("Fetching this week's audit logs");
        
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfWeek().getValue() - 1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);
        List<AuditLog> auditLogs = auditLogRepository.findByCreatedAtBetween(startOfWeek, endOfWeek);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this month's audit logs
    public List<AuditLogDTO> getThisMonthsAuditLogs() {
        logger.info("Fetching this month's audit logs");
        
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        List<AuditLog> auditLogs = auditLogRepository.findByCreatedAtBetween(startOfMonth, endOfMonth);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit logs by date range
    public List<AuditLogDTO> getAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Fetching audit logs from {} to {}", startDate, endDate);
        
        List<AuditLog> auditLogs = auditLogRepository.findByCreatedAtBetween(startDate, endDate);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit logs by IP address
    public List<AuditLogDTO> getAuditLogsByIpAddress(String ipAddress) {
        logger.info("Fetching audit logs by IP address: {}", ipAddress);
        
        List<AuditLog> auditLogs = auditLogRepository.findByIpAddress(ipAddress);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit logs by user agent
    public List<AuditLogDTO> getAuditLogsByUserAgent(String userAgent) {
        logger.info("Fetching audit logs by user agent: {}", userAgent);
        
        List<AuditLog> auditLogs = auditLogRepository.findByUserAgentContaining(userAgent);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit logs by action
    public List<AuditLogDTO> getAuditLogsByAction(String action) {
        logger.info("Fetching audit logs by action: {}", action);
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionContainingIgnoreCase(action);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get audit logs by description
    public List<AuditLogDTO> getAuditLogsByDescription(String description) {
        logger.info("Fetching audit logs by description: {}", description);
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionContainingIgnoreCase(description);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get login audit logs
    public List<AuditLogDTO> getLoginAuditLogs() {
        logger.info("Fetching login audit logs");
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionType(AuditLog.ActionType.LOGIN);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get logout audit logs
    public List<AuditLogDTO> getLogoutAuditLogs() {
        logger.info("Fetching logout audit logs");
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionType(AuditLog.ActionType.LOGOUT);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get create audit logs
    public List<AuditLogDTO> getCreateAuditLogs() {
        logger.info("Fetching create audit logs");
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionType(AuditLog.ActionType.CREATE);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get update audit logs
    public List<AuditLogDTO> getUpdateAuditLogs() {
        logger.info("Fetching update audit logs");
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionType(AuditLog.ActionType.UPDATE);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get delete audit logs
    public List<AuditLogDTO> getDeleteAuditLogs() {
        logger.info("Fetching delete audit logs");
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionType(AuditLog.ActionType.DELETE);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get view audit logs
    public List<AuditLogDTO> getViewAuditLogs() {
        logger.info("Fetching view audit logs");
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionType(AuditLog.ActionType.VIEW);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get export audit logs
    public List<AuditLogDTO> getExportAuditLogs() {
        logger.info("Fetching export audit logs");
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionType(AuditLog.ActionType.EXPORT);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get import audit logs
    public List<AuditLogDTO> getImportAuditLogs() {
        logger.info("Fetching import audit logs");
        
        List<AuditLog> auditLogs = auditLogRepository.findByActionType(AuditLog.ActionType.IMPORT);
        return auditLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Delete old audit logs
    public void deleteOldAuditLogs(int days) {
        logger.info("Deleting audit logs older than {} days", days);
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        List<AuditLog> oldAuditLogs = auditLogRepository.findByCreatedAtBefore(cutoffDate);
        
        auditLogRepository.deleteAll(oldAuditLogs);
        logger.info("Deleted {} old audit logs", oldAuditLogs.size());
    }

    // Convert entity to DTO
    private AuditLogDTO convertToDTO(AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setUserId(auditLog.getUserId());
        dto.setAction(auditLog.getAction());
        dto.setEntityType(auditLog.getEntityType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setEntityName(auditLog.getEntityName());
        dto.setOldValues(auditLog.getOldValues());
        dto.setNewValues(auditLog.getNewValues());
        dto.setIpAddress(auditLog.getIpAddress());
        dto.setUserAgent(auditLog.getUserAgent());
        dto.setActionType(auditLog.getActionType());
        dto.setDescription(auditLog.getDescription());
        dto.setTimestamp(auditLog.getTimestamp());
        
        // Set user name if available
        if (auditLog.getUserId() != null) {
            Optional<User> userOpt = userRepository.findById(auditLog.getUserId());
            if (userOpt.isPresent()) {
                dto.setUserName(userOpt.get().getFirstName() + " " + userOpt.get().getLastName());
            }
        }
        
        return dto;
    }

    // Additional methods needed by AuditLogController
    public AuditLogDTO updateAuditLog(Long id, AuditLogRequestDTO request) {
        logger.info("Updating audit log with ID: {}", id);
        
        return auditLogRepository.findById(id)
                .map(auditLog -> {
                    auditLog.setAction(request.getAction());
                    auditLog.setEntityType(request.getEntityType());
                    auditLog.setEntityId(request.getEntityId());
                    auditLog.setOldValues(request.getOldValues());
                    auditLog.setNewValues(request.getNewValues());
                    auditLog.setIpAddress(request.getIpAddress());
                    auditLog.setUserAgent(request.getUserAgent());
                    auditLog.setActionType(request.getActionType());
                    
                    AuditLog savedLog = auditLogRepository.save(auditLog);
                    logger.info("Audit log updated successfully with ID: {}", savedLog.getId());
                    
                    return convertToDTO(savedLog);
                })
                .orElse(null);
    }

    public boolean deleteAuditLog(Long id) {
        logger.info("Deleting audit log with ID: {}", id);
        
        if (auditLogRepository.existsById(id)) {
            auditLogRepository.deleteById(id);
            logger.info("Audit log deleted successfully with ID: {}", id);
            return true;
        }
        
        logger.warn("Audit log not found with ID: {}", id);
        return false;
    }

    public List<AuditLogDTO> getAuditLogsByEntity(String entityType, Long entityId) {
        logger.info("Getting audit logs for entity: {} with ID: {}", entityType, entityId);
        
        List<AuditLog> logs = auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return logs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
