package gym.backend.dto;

import gym.backend.model.Notification;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String message;
    private Notification.NotificationType type;
    private Notification.NotificationCategory category;
    private Boolean isRead;
    private Boolean isImportant;
    private String actionUrl;
    private String actionData;
    private LocalDateTime scheduledFor;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    // Constructors
    public NotificationDTO() {}

    public NotificationDTO(Long id, String title, String message, 
                          Notification.NotificationType type, 
                          Notification.NotificationCategory category) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.type = type;
        this.category = category;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Notification.NotificationType getType() {
        return type;
    }

    public void setType(Notification.NotificationType type) {
        this.type = type;
    }

    public Notification.NotificationCategory getCategory() {
        return category;
    }

    public void setCategory(Notification.NotificationCategory category) {
        this.category = category;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(Boolean isImportant) {
        this.isImportant = isImportant;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public LocalDateTime getScheduledFor() {
        return scheduledFor;
    }

    public void setScheduledFor(LocalDateTime scheduledFor) {
        this.scheduledFor = scheduledFor;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    // Helper methods
    public boolean isRead() {
        return isRead != null && isRead;
    }

    public boolean isUnread() {
        return !isRead();
    }

    public boolean isImportant() {
        return isImportant != null && isImportant;
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isScheduled() {
        return scheduledFor != null && LocalDateTime.now().isBefore(scheduledFor);
    }

    public boolean shouldBeSent() {
        return !isScheduled() && !isExpired();
    }

    public boolean hasAction() {
        return actionUrl != null && !actionUrl.trim().isEmpty();
    }

    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        
        LocalDateTime now = LocalDateTime.now();
        long minutesAgo = java.time.temporal.ChronoUnit.MINUTES.between(createdAt, now);
        
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

    public String getTypeIcon() {
        if (type == null) {
            return "📢";
        }
        
        switch (type) {
            case INFO:
                return "ℹ️";
            case WARNING:
                return "⚠️";
            case SUCCESS:
                return "✅";
            case ERROR:
                return "❌";
            case REMINDER:
                return "🔔";
            default:
                return "📢";
        }
    }

    public String getCategoryIcon() {
        if (category == null) {
            return "📢";
        }
        
        switch (category) {
            case TRAINING:
                return "💪";
            case NUTRITION:
                return "🥗";
            case PAYMENT:
                return "💳";
            case SYSTEM:
                return "⚙️";
            case REMINDER:
                return "🔔";
            default:
                return "📢";
        }
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", category=" + category +
                ", isRead=" + isRead +
                ", isImportant=" + isImportant +
                '}';
    }
}
