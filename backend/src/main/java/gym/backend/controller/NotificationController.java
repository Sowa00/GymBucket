package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.NotificationDTO;
import gym.backend.dto.NotificationRequestDTO;
import gym.backend.model.Notification;
import gym.backend.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    @Autowired
    private NotificationService notificationService;

    // Get all notifications with pagination
    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            logger.info("Fetching all notifications with pagination - page: {}, size: {}", page, size);
            Page<NotificationDTO> notifications = notificationService.getAllNotifications(page, size, sortBy, sortDir);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching notifications: " + e.getMessage(), null));
        }
    }

    // Get all notifications as list
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllNotificationsList() {
        try {
            logger.info("Fetching all notifications as list");
            List<NotificationDTO> notifications = notificationService.getAllNotificationsList();
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching notifications list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching notifications: " + e.getMessage(), null));
        }
    }

    // Get notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getNotificationById(@PathVariable Long id) {
        try {
            logger.info("Fetching notification by ID: {}", id);
            Optional<NotificationDTO> notification = notificationService.getNotificationById(id);
            
            if (notification.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Notification fetched successfully", notification.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Notification not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching notification by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching notification: " + e.getMessage(), null));
        }
    }

    // Create new notification
    @PostMapping
    public ResponseEntity<ApiResponseDTO> createNotification(@RequestBody NotificationRequestDTO request) {
        try {
            logger.info("Creating new notification: {} for user: {}", request.getTitle(), request.getUserId());
            NotificationDTO notification = notificationService.createNotification(request);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Notification created successfully", notification));
        } catch (Exception e) {
            logger.error("Error creating notification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating notification: " + e.getMessage(), null));
        }
    }

    // Update notification
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateNotification(
            @PathVariable Long id,
            @RequestBody NotificationRequestDTO request) {
        
        try {
            logger.info("Updating notification: {}", id);
            NotificationDTO notification = notificationService.updateNotification(id, request);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Notification updated successfully", notification));
        } catch (Exception e) {
            logger.error("Error updating notification: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating notification: " + e.getMessage(), null));
        }
    }

    // Delete notification
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteNotification(@PathVariable Long id) {
        try {
            logger.info("Deleting notification: {}", id);
            notificationService.deleteNotification(id);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Notification deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting notification: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting notification: " + e.getMessage(), null));
        }
    }

    // Mark notification as read
    @PutMapping("/{id}/mark-read")
    public ResponseEntity<ApiResponseDTO> markAsRead(@PathVariable Long id) {
        try {
            logger.info("Marking notification as read: {}", id);
            NotificationDTO notification = notificationService.markAsRead(id);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Notification marked as read successfully", notification));
        } catch (Exception e) {
            logger.error("Error marking notification as read: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error marking notification as read: " + e.getMessage(), null));
        }
    }

    // Mark notification as unread
    @PutMapping("/{id}/mark-unread")
    public ResponseEntity<ApiResponseDTO> markAsUnread(@PathVariable Long id) {
        try {
            logger.info("Marking notification as unread: {}", id);
            NotificationDTO notification = notificationService.markAsUnread(id);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Notification marked as unread successfully", notification));
        } catch (Exception e) {
            logger.error("Error marking notification as unread: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error marking notification as unread: " + e.getMessage(), null));
        }
    }

    // Get notifications by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO> getNotificationsByUser(@PathVariable Long userId) {
        try {
            logger.info("Fetching notifications for user: {}", userId);
            List<NotificationDTO> notifications = notificationService.getNotificationsByUser(userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching notifications for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching notifications: " + e.getMessage(), null));
        }
    }

    // Get unread notifications by user
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<ApiResponseDTO> getUnreadNotificationsByUser(@PathVariable Long userId) {
        try {
            logger.info("Fetching unread notifications for user: {}", userId);
            List<NotificationDTO> notifications = notificationService.getUnreadNotificationsByUser(userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Unread notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching unread notifications for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching unread notifications: " + e.getMessage(), null));
        }
    }

    // Get important notifications by user
    @GetMapping("/user/{userId}/important")
    public ResponseEntity<ApiResponseDTO> getImportantNotificationsByUser(@PathVariable Long userId) {
        try {
            logger.info("Fetching important notifications for user: {}", userId);
            List<NotificationDTO> notifications = notificationService.getImportantNotificationsByUser(userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Important notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching important notifications for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching important notifications: " + e.getMessage(), null));
        }
    }

    // Get notifications by type
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponseDTO> getNotificationsByType(@PathVariable Notification.NotificationType type) {
        try {
            logger.info("Fetching notifications by type: {}", type);
            List<NotificationDTO> notifications = notificationService.getNotificationsByType(type);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching notifications by type: {}", type, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching notifications: " + e.getMessage(), null));
        }
    }

    // Get notifications by category
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponseDTO> getNotificationsByCategory(@PathVariable Notification.NotificationCategory category) {
        try {
            logger.info("Fetching notifications by category: {}", category);
            List<NotificationDTO> notifications = notificationService.getNotificationsByCategory(category);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching notifications by category: {}", category, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching notifications: " + e.getMessage(), null));
        }
    }

    // Get recent notifications
    @GetMapping("/recent")
    public ResponseEntity<ApiResponseDTO> getRecentNotifications(
            @RequestParam(defaultValue = "24") int hours) {
        
        try {
            logger.info("Fetching recent notifications from last {} hours", hours);
            List<NotificationDTO> notifications = notificationService.getRecentNotifications(hours);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Recent notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching recent notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching recent notifications: " + e.getMessage(), null));
        }
    }

    // Get scheduled notifications
    @GetMapping("/scheduled")
    public ResponseEntity<ApiResponseDTO> getScheduledNotifications() {
        try {
            logger.info("Fetching scheduled notifications");
            List<NotificationDTO> notifications = notificationService.getScheduledNotifications();
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Scheduled notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching scheduled notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching scheduled notifications: " + e.getMessage(), null));
        }
    }

    // Get expired notifications
    @GetMapping("/expired")
    public ResponseEntity<ApiResponseDTO> getExpiredNotifications() {
        try {
            logger.info("Fetching expired notifications");
            List<NotificationDTO> notifications = notificationService.getExpiredNotifications();
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Expired notifications fetched successfully", notifications));
        } catch (Exception e) {
            logger.error("Error fetching expired notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching expired notifications: " + e.getMessage(), null));
        }
    }

    // Mark all notifications as read for user
    @PutMapping("/user/{userId}/mark-all-read")
    public ResponseEntity<ApiResponseDTO> markAllAsReadForUser(@PathVariable Long userId) {
        try {
            logger.info("Marking all notifications as read for user: {}", userId);
            notificationService.markAllAsReadForUser(userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "All notifications marked as read successfully", null));
        } catch (Exception e) {
            logger.error("Error marking all notifications as read for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error marking all notifications as read: " + e.getMessage(), null));
        }
    }

    // Delete expired notifications
    @DeleteMapping("/expired")
    public ResponseEntity<ApiResponseDTO> deleteExpiredNotifications() {
        try {
            logger.info("Deleting expired notifications");
            notificationService.deleteExpiredNotifications();
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Expired notifications deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting expired notifications", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting expired notifications: " + e.getMessage(), null));
        }
    }
}
