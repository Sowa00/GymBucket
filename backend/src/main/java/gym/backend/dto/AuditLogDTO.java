package gym.backend.dto;

import gym.backend.model.AuditLog;

import java.time.LocalDateTime;

public class AuditLogDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String action;
    private String entityType;
    private Long entityId;
    private String entityName;
    private String oldValues; // JSON string
    private String newValues; // JSON string
    private String ipAddress;
    private String userAgent;
    private AuditLog.ActionType actionType;
    private String description;
    private LocalDateTime timestamp;

    // Constructors
    public AuditLogDTO() {}

    public AuditLogDTO(Long id, Long userId, String userName, String action, 
                      String entityType, Long entityId, AuditLog.ActionType actionType) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.actionType = actionType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getOldValues() {
        return oldValues;
    }

    public void setOldValues(String oldValues) {
        this.oldValues = oldValues;
    }

    public String getNewValues() {
        return newValues;
    }

    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public AuditLog.ActionType getActionType() {
        return actionType;
    }

    public void setActionType(AuditLog.ActionType actionType) {
        this.actionType = actionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // Helper methods
    public boolean hasOldValues() {
        return oldValues != null && !oldValues.trim().isEmpty();
    }

    public boolean hasNewValues() {
        return newValues != null && !newValues.trim().isEmpty();
    }

    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public boolean hasIpAddress() {
        return ipAddress != null && !ipAddress.trim().isEmpty();
    }

    public boolean hasUserAgent() {
        return userAgent != null && !userAgent.trim().isEmpty();
    }

    public String getFormattedTimestamp() {
        if (timestamp == null) {
            return "N/A";
        }
        
        LocalDateTime now = LocalDateTime.now();
        long minutesAgo = java.time.temporal.ChronoUnit.MINUTES.between(timestamp, now);
        
        if (minutesAgo < 1) {
            return "Teraz";
        } else if (minutesAgo < 60) {
            return minutesAgo + " min temu";
        } else if (minutesAgo < 1440) { // 24 hours
            long hoursAgo = minutesAgo / 60;
            return hoursAgo + " godz. temu";
        } else {
            long daysAgo = minutesAgo / 1440;
            return daysAgo + " dni temu";
        }
    }

    public String getActionIcon() {
        if (actionType == null) {
            return "📝";
        }
        
        switch (actionType) {
            case CREATE:
                return "➕";
            case UPDATE:
                return "✏️";
            case DELETE:
                return "🗑️";
            case LOGIN:
                return "🔐";
            case LOGOUT:
                return "🚪";
            case VIEW:
                return "👁️";
            case EXPORT:
                return "📤";
            case IMPORT:
                return "📥";
            default:
                return "📝";
        }
    }

    public String getActionColor() {
        if (actionType == null) {
            return "blue";
        }
        
        switch (actionType) {
            case CREATE:
                return "green";
            case UPDATE:
                return "orange";
            case DELETE:
                return "red";
            case LOGIN:
                return "blue";
            case LOGOUT:
                return "gray";
            case VIEW:
                return "purple";
            case EXPORT:
                return "teal";
            case IMPORT:
                return "cyan";
            default:
                return "blue";
        }
    }

    public boolean isRecent() {
        if (timestamp == null) {
            return false;
        }
        return timestamp.isAfter(LocalDateTime.now().minusHours(24));
    }

    public boolean isToday() {
        if (timestamp == null) {
            return false;
        }
        return timestamp.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }

    public boolean isThisWeek() {
        if (timestamp == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);
        return !timestamp.isBefore(startOfWeek) && !timestamp.isAfter(endOfWeek);
    }

    public boolean isThisMonth() {
        if (timestamp == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return timestamp.getYear() == now.getYear() && 
               timestamp.getMonth() == now.getMonth();
    }

    public boolean isCreateAction() {
        return actionType == AuditLog.ActionType.CREATE;
    }

    public boolean isUpdateAction() {
        return actionType == AuditLog.ActionType.UPDATE;
    }

    public boolean isDeleteAction() {
        return actionType == AuditLog.ActionType.DELETE;
    }

    public boolean isLoginAction() {
        return actionType == AuditLog.ActionType.LOGIN;
    }

    public boolean isLogoutAction() {
        return actionType == AuditLog.ActionType.LOGOUT;
    }

    @Override
    public String toString() {
        return "AuditLogDTO{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", action='" + action + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", actionType=" + actionType +
                ", timestamp=" + timestamp +
                '}';
    }
}
