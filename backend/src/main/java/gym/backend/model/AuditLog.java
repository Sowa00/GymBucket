package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "old_values", columnDefinition = "JSON")
    private String oldValues;

    @Column(name = "new_values", columnDefinition = "JSON")
    private String newValues;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", length = 20)
    private ActionType actionType;

    @Column(name = "session_id", length = 255)
    private String sessionId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public AuditLog() {}

    public AuditLog(String action, String entityType, Long entityId, User user) {
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean isCreateAction() {
        return "CREATE".equalsIgnoreCase(action);
    }

    public boolean isUpdateAction() {
        return "UPDATE".equalsIgnoreCase(action);
    }

    public boolean isDeleteAction() {
        return "DELETE".equalsIgnoreCase(action);
    }

    public boolean isLoginAction() {
        return "LOGIN".equalsIgnoreCase(action);
    }

    public boolean isLogoutAction() {
        return "LOGOUT".equalsIgnoreCase(action);
    }

    public String getFormattedAction() {
        switch (action.toUpperCase()) {
            case "CREATE":
                return "Utworzono";
            case "UPDATE":
                return "Zaktualizowano";
            case "DELETE":
                return "Usunięto";
            case "LOGIN":
                return "Logowanie";
            case "LOGOUT":
                return "Wylogowanie";
            default:
                return action;
        }
    }

    public String getEntityDescription() {
        if (entityType == null) {
            return "Nieznany";
        }
        
        switch (entityType.toUpperCase()) {
            case "USER":
                return "Użytkownik";
            case "CLIENT":
                return "Klient";
            case "WORKOUT_PLAN":
                return "Plan treningowy";
            case "NUTRITION_PLAN":
                return "Plan żywieniowy";
            case "EXERCISE":
                return "Ćwiczenie";
            case "MEAL":
                return "Posiłek";
            case "TRAINING_SESSION":
                return "Sesja treningowa";
            default:
                return entityType;
        }
    }

    public boolean hasChanges() {
        return oldValues != null || newValues != null;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", action='" + action + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", user=" + (user != null ? user.getFullName() : "null") +
                ", createdAt=" + createdAt +
                '}';
    }

    // Enums
    public enum ActionType {
        CREATE,
        UPDATE,
        DELETE,
        LOGIN,
        LOGOUT,
        VIEW,
        EXPORT,
        IMPORT
    }

    // Additional methods needed by services
    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUserId(Long userId) {
        // This method is used by services but doesn't actually set the user
        // The user should be set through the setUser method
    }

    public String getEntityName() {
        return entityType;
    }

    public void setEntityName(String entityName) {
        this.entityType = entityName;
    }

    public String getDescription() {
        return action;
    }

    public void setDescription(String description) {
        this.action = description;
    }

    public LocalDateTime getTimestamp() {
        return createdAt;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.createdAt = timestamp;
    }
}
