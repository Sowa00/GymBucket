package gym.backend.dto;

import gym.backend.model.Notification;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class NotificationRequestDTO {
    
    // @NotNull(message = "User ID is required")
    private Long userId;
    
    // @NotBlank(message = "Title is required")
    // @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    // @NotBlank(message = "Message is required")
    // @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;
    
    // @NotNull(message = "Type is required")
    private Notification.NotificationType type;
    
    // @NotNull(message = "Category is required")
    private Notification.NotificationCategory category;
    
    private Boolean isImportant = false;
    
    // @Size(max = 500, message = "Action URL must not exceed 500 characters")
    private String actionUrl;
    
    private String actionData;
    
    private LocalDateTime scheduledFor;
    
    private LocalDateTime expiresAt;

    // Constructors
    public NotificationRequestDTO() {}

    public NotificationRequestDTO(Long userId, String title, String message, 
                                 Notification.NotificationType type, 
                                 Notification.NotificationCategory category) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.category = category;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    // Helper methods
    public boolean isImportant() {
        return isImportant != null && isImportant;
    }

    public boolean isScheduled() {
        return scheduledFor != null;
    }

    public boolean hasExpiration() {
        return expiresAt != null;
    }

    public boolean hasAction() {
        return actionUrl != null && !actionUrl.trim().isEmpty();
    }

    public boolean isImmediate() {
        return !isScheduled();
    }

    public boolean isInfoType() {
        return type == Notification.NotificationType.INFO;
    }

    public boolean isWarningType() {
        return type == Notification.NotificationType.WARNING;
    }

    public boolean isSuccessType() {
        return type == Notification.NotificationType.SUCCESS;
    }

    public boolean isErrorType() {
        return type == Notification.NotificationType.ERROR;
    }

    public boolean isReminderType() {
        return type == Notification.NotificationType.REMINDER;
    }

    public boolean isTrainingCategory() {
        return category == Notification.NotificationCategory.TRAINING;
    }

    public boolean isNutritionCategory() {
        return category == Notification.NotificationCategory.NUTRITION;
    }

    public boolean isPaymentCategory() {
        return category == Notification.NotificationCategory.PAYMENT;
    }

    public boolean isSystemCategory() {
        return category == Notification.NotificationCategory.SYSTEM;
    }

    public boolean isReminderCategory() {
        return category == Notification.NotificationCategory.REMINDER;
    }

    @Override
    public String toString() {
        return "NotificationRequestDTO{" +
                "userId=" + userId +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", category=" + category +
                ", isImportant=" + isImportant +
                ", isScheduled=" + isScheduled() +
                '}';
    }
}
