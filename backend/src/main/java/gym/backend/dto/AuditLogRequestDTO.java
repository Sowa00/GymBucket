package gym.backend.dto;

import gym.backend.model.AuditLog;

public class AuditLogRequestDTO {
    
    private Long userId;
    
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

    // Constructors
    public AuditLogRequestDTO() {}

    public AuditLogRequestDTO(Long userId, String action, String entityType, 
                             Long entityId, AuditLog.ActionType actionType) {
        this.userId = userId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.actionType = actionType;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public boolean isViewAction() {
        return actionType == AuditLog.ActionType.VIEW;
    }

    public boolean isExportAction() {
        return actionType == AuditLog.ActionType.EXPORT;
    }

    public boolean isImportAction() {
        return actionType == AuditLog.ActionType.IMPORT;
    }

    public boolean hasEntity() {
        return entityId != null && entityType != null;
    }

    public boolean hasEntityName() {
        return entityName != null && !entityName.trim().isEmpty();
    }

    public boolean hasAction() {
        return action != null && !action.trim().isEmpty();
    }

    public boolean hasUserId() {
        return userId != null;
    }

    public boolean hasActionType() {
        return actionType != null;
    }

    public String getFormattedAction() {
        if (action == null) {
            return "N/A";
        }
        return action;
    }

    public String getFormattedEntityType() {
        if (entityType == null) {
            return "N/A";
        }
        return entityType;
    }

    public String getFormattedEntityId() {
        if (entityId == null) {
            return "N/A";
        }
        return entityId.toString();
    }

    public String getFormattedEntityName() {
        if (entityName == null) {
            return "N/A";
        }
        return entityName;
    }

    @Override
    public String toString() {
        return "AuditLogRequestDTO{" +
                "userId=" + userId +
                ", action='" + action + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", actionType=" + actionType +
                '}';
    }
}
