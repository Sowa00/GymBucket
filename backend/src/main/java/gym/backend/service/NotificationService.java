package gym.backend.service;

import gym.backend.dto.NotificationDTO;
import gym.backend.dto.NotificationRequestDTO;
import gym.backend.model.Notification;
import gym.backend.model.User;
import gym.backend.repository.NotificationRepository;
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
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Get all notifications with pagination
    public Page<NotificationDTO> getAllNotifications(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all notifications with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Notification> notifications = notificationRepository.findAll(pageable);
        
        return notifications.map(this::convertToDTO);
    }

    // Get all notifications as list
    public List<NotificationDTO> getAllNotificationsList() {
        logger.info("Fetching all notifications as list");
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get notification by ID
    public Optional<NotificationDTO> getNotificationById(Long id) {
        logger.info("Fetching notification by ID: {}", id);
        return notificationRepository.findById(id).map(this::convertToDTO);
    }

    // Create new notification
    public NotificationDTO createNotification(NotificationRequestDTO request) {
        logger.info("Creating new notification: {} for user: {}", request.getTitle(), request.getUserId());
        
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + request.getUserId());
        }
        
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setCategory(request.getCategory());
        notification.setIsRead(false);
        notification.setIsImportant(request.getIsImportant());
        notification.setActionUrl(request.getActionUrl());
        notification.setActionData(request.getActionData());
        notification.setScheduledFor(request.getScheduledFor());
        notification.setExpiresAt(request.getExpiresAt());
        notification.setCreatedAt(LocalDateTime.now());
        
        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Notification created successfully with ID: {}", savedNotification.getId());
        
        return convertToDTO(savedNotification);
    }

    // Update notification
    public NotificationDTO updateNotification(Long id, NotificationRequestDTO request) {
        logger.info("Updating notification: {}", id);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isEmpty()) {
            throw new RuntimeException("Notification not found with ID: " + id);
        }
        
        Notification notification = notificationOpt.get();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setCategory(request.getCategory());
        notification.setIsImportant(request.getIsImportant());
        notification.setActionUrl(request.getActionUrl());
        notification.setActionData(request.getActionData());
        notification.setScheduledFor(request.getScheduledFor());
        notification.setExpiresAt(request.getExpiresAt());
        
        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Notification updated successfully with ID: {}", savedNotification.getId());
        
        return convertToDTO(savedNotification);
    }

    // Delete notification
    public void deleteNotification(Long id) {
        logger.info("Deleting notification: {}", id);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isEmpty()) {
            throw new RuntimeException("Notification not found with ID: " + id);
        }
        
        notificationRepository.deleteById(id);
        logger.info("Notification deleted successfully with ID: {}", id);
    }

    // Mark notification as read
    public NotificationDTO markAsRead(Long id) {
        logger.info("Marking notification as read: {}", id);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isEmpty()) {
            throw new RuntimeException("Notification not found with ID: " + id);
        }
        
        Notification notification = notificationOpt.get();
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        
        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Notification marked as read with ID: {}", savedNotification.getId());
        
        return convertToDTO(savedNotification);
    }

    // Mark notification as unread
    public NotificationDTO markAsUnread(Long id) {
        logger.info("Marking notification as unread: {}", id);
        
        Optional<Notification> notificationOpt = notificationRepository.findById(id);
        if (notificationOpt.isEmpty()) {
            throw new RuntimeException("Notification not found with ID: " + id);
        }
        
        Notification notification = notificationOpt.get();
        notification.setIsRead(false);
        notification.setReadAt(null);
        
        Notification savedNotification = notificationRepository.save(notification);
        logger.info("Notification marked as unread with ID: {}", savedNotification.getId());
        
        return convertToDTO(savedNotification);
    }

    // Get notifications by user
    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        logger.info("Fetching notifications for user: {}", userId);
        
        List<Notification> notifications = notificationRepository.findByUser_Id(userId);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get unread notifications by user
    public List<NotificationDTO> getUnreadNotificationsByUser(Long userId) {
        logger.info("Fetching unread notifications for user: {}", userId);
        
        List<Notification> notifications = notificationRepository.findByUser_IdAndIsReadFalse(userId);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get important notifications by user
    public List<NotificationDTO> getImportantNotificationsByUser(Long userId) {
        logger.info("Fetching important notifications for user: {}", userId);
        
        List<Notification> notifications = notificationRepository.findByUser_IdAndIsImportantTrue(userId);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get notifications by type
    public List<NotificationDTO> getNotificationsByType(Notification.NotificationType type) {
        logger.info("Fetching notifications by type: {}", type);
        
        List<Notification> notifications = notificationRepository.findByType(type);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get notifications by category
    public List<NotificationDTO> getNotificationsByCategory(Notification.NotificationCategory category) {
        logger.info("Fetching notifications by category: {}", category);
        
        List<Notification> notifications = notificationRepository.findByCategory(category);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent notifications
    public List<NotificationDTO> getRecentNotifications(int hours) {
        logger.info("Fetching recent notifications from last {} hours", hours);
        
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        List<Notification> notifications = notificationRepository.findByCreatedAtAfter(since);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get scheduled notifications
    public List<NotificationDTO> getScheduledNotifications() {
        logger.info("Fetching scheduled notifications");
        
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = notificationRepository.findByScheduledForBeforeAndIsReadFalse(now);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get expired notifications
    public List<NotificationDTO> getExpiredNotifications() {
        logger.info("Fetching expired notifications");
        
        LocalDateTime now = LocalDateTime.now();
        List<Notification> notifications = notificationRepository.findByExpiresAtBefore(now);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Mark all notifications as read for user
    public void markAllAsReadForUser(Long userId) {
        logger.info("Marking all notifications as read for user: {}", userId);
        
        List<Notification> notifications = notificationRepository.findByUser_IdAndIsReadFalse(userId);
        LocalDateTime now = LocalDateTime.now();
        
        for (Notification notification : notifications) {
            notification.setIsRead(true);
            notification.setReadAt(now);
        }
        
        notificationRepository.saveAll(notifications);
        logger.info("Marked {} notifications as read for user: {}", notifications.size(), userId);
    }

    // Delete expired notifications
    public void deleteExpiredNotifications() {
        logger.info("Deleting expired notifications");
        
        LocalDateTime now = LocalDateTime.now();
        List<Notification> expiredNotifications = notificationRepository.findByExpiresAtBefore(now);
        
        notificationRepository.deleteAll(expiredNotifications);
        logger.info("Deleted {} expired notifications", expiredNotifications.size());
    }

    // Convert entity to DTO
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setCategory(notification.getCategory());
        dto.setIsRead(notification.getIsRead());
        dto.setIsImportant(notification.getIsImportant());
        dto.setActionUrl(notification.getActionUrl());
        dto.setActionData(notification.getActionData());
        dto.setScheduledFor(notification.getScheduledFor());
        dto.setExpiresAt(notification.getExpiresAt());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setReadAt(notification.getReadAt());
        
        // Set user name if available
        if (notification.getUserId() != null) {
            Optional<User> userOpt = userRepository.findById(notification.getUserId());
            if (userOpt.isPresent()) {
                dto.setUserName(userOpt.get().getFirstName() + " " + userOpt.get().getLastName());
            }
        }
        
        return dto;
    }
}
