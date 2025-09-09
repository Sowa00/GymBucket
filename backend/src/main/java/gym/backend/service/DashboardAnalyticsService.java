package gym.backend.service;

import gym.backend.dto.DashboardAnalyticsDTO;
import gym.backend.dto.DashboardAnalyticsRequestDTO;
import gym.backend.model.DashboardAnalytics;
import gym.backend.repository.DashboardAnalyticsRepository;
import gym.backend.repository.UserRepository;
import gym.backend.repository.WorkoutPlanRepository;
import gym.backend.repository.NutritionPlanRepository;
import gym.backend.repository.WorkoutSessionLogRepository;
import gym.backend.repository.MealLogRepository;
import gym.backend.repository.NotificationRepository;
import gym.backend.repository.FileUploadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DashboardAnalyticsService {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardAnalyticsService.class);
    
    @Autowired
    private DashboardAnalyticsRepository analyticsRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;
    
    @Autowired
    private NutritionPlanRepository nutritionPlanRepository;
    
    @Autowired
    private WorkoutSessionLogRepository sessionLogRepository;
    
    @Autowired
    private MealLogRepository mealLogRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private FileUploadRepository fileUploadRepository;

    // Get all dashboard analytics with pagination
    public Page<DashboardAnalyticsDTO> getAllDashboardAnalytics(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all dashboard analytics with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DashboardAnalytics> analytics = analyticsRepository.findAll(pageable);
        
        return analytics.map(this::convertToDTO);
    }

    // Get all dashboard analytics as list
    public List<DashboardAnalyticsDTO> getAllDashboardAnalyticsList() {
        logger.info("Fetching all dashboard analytics as list");
        List<DashboardAnalytics> analytics = analyticsRepository.findAll();
        return analytics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get dashboard analytics by ID
    public Optional<DashboardAnalyticsDTO> getDashboardAnalyticsById(Long id) {
        logger.info("Fetching dashboard analytics by ID: {}", id);
        return analyticsRepository.findById(id).map(this::convertToDTO);
    }

    // Get dashboard analytics by date
    public Optional<DashboardAnalyticsDTO> getDashboardAnalyticsByDate(LocalDate date) {
        logger.info("Fetching dashboard analytics for date: {}", date);
        return analyticsRepository.findByAnalyticsDate(date).map(this::convertToDTO);
    }

    // Get latest dashboard analytics
    public Optional<DashboardAnalyticsDTO> getLatestDashboardAnalytics() {
        logger.info("Fetching latest dashboard analytics");
        return analyticsRepository.findTopByOrderByAnalyticsDateDesc().map(this::convertToDTO);
    }

    // Create new dashboard analytics
    public DashboardAnalyticsDTO createDashboardAnalytics(DashboardAnalyticsRequestDTO request) {
        logger.info("Creating new dashboard analytics for date: {}", request.getDate());
        
        DashboardAnalytics analytics = new DashboardAnalytics();
        analytics.setDate(request.getDate());
        analytics.setTotalClients(request.getTotalClients());
        analytics.setActiveClients(request.getActiveClients());
        analytics.setNewClients(request.getNewClients());
        analytics.setTotalWorkoutPlans(request.getTotalWorkoutPlans());
        analytics.setTotalNutritionPlans(request.getTotalNutritionPlans());
        analytics.setTotalWorkoutSessions(request.getTotalWorkoutSessions());
        analytics.setTotalMealLogs(request.getTotalMealLogs());
        analytics.setTotalRevenue(request.getTotalRevenue());
        analytics.setMonthlyRevenue(request.getMonthlyRevenue() != null ? BigDecimal.valueOf(request.getMonthlyRevenue()) : BigDecimal.ZERO);
        analytics.setTotalNotifications(request.getTotalNotifications());
        analytics.setUnreadNotifications(request.getUnreadNotifications());
        analytics.setTotalFileUploads(request.getTotalFileUploads());
        analytics.setAverageWorkoutRating(request.getAverageWorkoutRating());
        analytics.setAverageMealRatingAsDouble(request.getAverageMealRating());
        analytics.setTopWorkoutPlan(request.getTopWorkoutPlan());
        analytics.setTopNutritionPlan(request.getTopNutritionPlan());
        analytics.setMostActiveClient(request.getMostActiveClient());
        analytics.setStatistics(request.getStatistics());
        analytics.setCreatedAt(LocalDateTime.now());
        analytics.setUpdatedAt(LocalDateTime.now());
        
        DashboardAnalytics savedAnalytics = analyticsRepository.save(analytics);
        logger.info("Dashboard analytics created successfully with ID: {}", savedAnalytics.getId());
        
        return convertToDTO(savedAnalytics);
    }

    // Update dashboard analytics
    public DashboardAnalyticsDTO updateDashboardAnalytics(Long id, DashboardAnalyticsRequestDTO request) {
        logger.info("Updating dashboard analytics: {}", id);
        
        Optional<DashboardAnalytics> analyticsOpt = analyticsRepository.findById(id);
        if (analyticsOpt.isEmpty()) {
            throw new RuntimeException("Dashboard analytics not found with ID: " + id);
        }
        
        DashboardAnalytics analytics = analyticsOpt.get();
        analytics.setDate(request.getDate());
        analytics.setTotalClients(request.getTotalClients());
        analytics.setActiveClients(request.getActiveClients());
        analytics.setNewClients(request.getNewClients());
        analytics.setTotalWorkoutPlans(request.getTotalWorkoutPlans());
        analytics.setTotalNutritionPlans(request.getTotalNutritionPlans());
        analytics.setTotalWorkoutSessions(request.getTotalWorkoutSessions());
        analytics.setTotalMealLogs(request.getTotalMealLogs());
        analytics.setTotalRevenue(request.getTotalRevenue());
        analytics.setMonthlyRevenue(request.getMonthlyRevenue() != null ? BigDecimal.valueOf(request.getMonthlyRevenue()) : BigDecimal.ZERO);
        analytics.setTotalNotifications(request.getTotalNotifications());
        analytics.setUnreadNotifications(request.getUnreadNotifications());
        analytics.setTotalFileUploads(request.getTotalFileUploads());
        analytics.setAverageWorkoutRating(request.getAverageWorkoutRating());
        analytics.setAverageMealRatingAsDouble(request.getAverageMealRating());
        analytics.setTopWorkoutPlan(request.getTopWorkoutPlan());
        analytics.setTopNutritionPlan(request.getTopNutritionPlan());
        analytics.setMostActiveClient(request.getMostActiveClient());
        analytics.setStatistics(request.getStatistics());
        analytics.setUpdatedAt(LocalDateTime.now());
        
        DashboardAnalytics savedAnalytics = analyticsRepository.save(analytics);
        logger.info("Dashboard analytics updated successfully with ID: {}", savedAnalytics.getId());
        
        return convertToDTO(savedAnalytics);
    }

    // Delete dashboard analytics
    public void deleteDashboardAnalytics(Long id) {
        logger.info("Deleting dashboard analytics: {}", id);
        
        Optional<DashboardAnalytics> analyticsOpt = analyticsRepository.findById(id);
        if (analyticsOpt.isEmpty()) {
            throw new RuntimeException("Dashboard analytics not found with ID: " + id);
        }
        
        analyticsRepository.deleteById(id);
        logger.info("Dashboard analytics deleted successfully with ID: {}", id);
    }

    // Get dashboard analytics by date range
    public List<DashboardAnalyticsDTO> getDashboardAnalyticsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching dashboard analytics from {} to {}", startDate, endDate);
        
        List<DashboardAnalytics> analytics = analyticsRepository.findByAnalyticsDateBetween(startDate, endDate);
        return analytics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent dashboard analytics
    public List<DashboardAnalyticsDTO> getRecentDashboardAnalytics(int days) {
        logger.info("Fetching recent dashboard analytics from last {} days", days);
        
        LocalDate since = LocalDate.now().minusDays(days);
        List<DashboardAnalytics> analytics = analyticsRepository.findByAnalyticsDateAfter(since);
        return analytics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this week's dashboard analytics
    public List<DashboardAnalyticsDTO> getThisWeeksDashboardAnalytics() {
        logger.info("Fetching this week's dashboard analytics");
        
        LocalDate startOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        List<DashboardAnalytics> analytics = analyticsRepository.findByAnalyticsDateBetween(startOfWeek, endOfWeek);
        return analytics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this month's dashboard analytics
    public List<DashboardAnalyticsDTO> getThisMonthsDashboardAnalytics() {
        logger.info("Fetching this month's dashboard analytics");
        
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<DashboardAnalytics> analytics = analyticsRepository.findByAnalyticsDateBetween(startOfMonth, endOfMonth);
        return analytics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Generate today's dashboard analytics
    public DashboardAnalyticsDTO generateTodaysDashboardAnalytics() {
        logger.info("Generating today's dashboard analytics");
        
        LocalDate today = LocalDate.now();
        
        // Check if analytics already exist for today
        Optional<DashboardAnalytics> existingAnalytics = analyticsRepository.findByAnalyticsDate(today);
        if (existingAnalytics.isPresent()) {
            logger.info("Dashboard analytics already exist for today, updating...");
            return updateTodaysDashboardAnalytics(existingAnalytics.get());
        }
        
        // Generate new analytics
        DashboardAnalytics analytics = new DashboardAnalytics();
        analytics.setDate(today);
        
        // Calculate statistics
        analytics.setTotalClients((int) userRepository.count());
        analytics.setActiveClients((int) userRepository.countByIsActiveTrue());
        analytics.setNewClients((int) userRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(30)));
        analytics.setTotalWorkoutPlans((int) workoutPlanRepository.count());
        analytics.setTotalNutritionPlans((int) nutritionPlanRepository.count());
        analytics.setTotalWorkoutSessions((int) sessionLogRepository.count());
        analytics.setTotalMealLogs((int) mealLogRepository.count());
        analytics.setTotalNotifications((int) notificationRepository.count());
        analytics.setUnreadNotifications((int) notificationRepository.countByIsReadFalse());
        analytics.setTotalFileUploads((int) fileUploadRepository.count());
        
        // Calculate average ratings
        Double avgWorkoutRating = sessionLogRepository.getAverageRating();
        analytics.setAverageWorkoutRating(avgWorkoutRating != null ? avgWorkoutRating : 0.0);
        
        // Set other fields
        analytics.setTotalRevenue(0.0);
        analytics.setMonthlyRevenue(BigDecimal.ZERO);
        analytics.setAverageMealRatingAsDouble(0.0);
        analytics.setTopWorkoutPlan("N/A");
        analytics.setTopNutritionPlan("N/A");
        analytics.setMostActiveClient("N/A");
        analytics.setStatistics("{}");
        analytics.setCreatedAt(LocalDateTime.now());
        analytics.setUpdatedAt(LocalDateTime.now());
        
        DashboardAnalytics savedAnalytics = analyticsRepository.save(analytics);
        logger.info("Today's dashboard analytics generated successfully with ID: {}", savedAnalytics.getId());
        
        return convertToDTO(savedAnalytics);
    }

    // Update today's dashboard analytics
    private DashboardAnalyticsDTO updateTodaysDashboardAnalytics(DashboardAnalytics analytics) {
        logger.info("Updating today's dashboard analytics");
        
        // Recalculate statistics
        analytics.setTotalClients((int) userRepository.count());
        analytics.setActiveClients((int) userRepository.countByIsActiveTrue());
        analytics.setNewClients((int) userRepository.countByCreatedAtAfter(LocalDateTime.now().minusDays(30)));
        analytics.setTotalWorkoutPlans((int) workoutPlanRepository.count());
        analytics.setTotalNutritionPlans((int) nutritionPlanRepository.count());
        analytics.setTotalWorkoutSessions((int) sessionLogRepository.count());
        analytics.setTotalMealLogs((int) mealLogRepository.count());
        analytics.setTotalNotifications((int) notificationRepository.count());
        analytics.setUnreadNotifications((int) notificationRepository.countByIsReadFalse());
        analytics.setTotalFileUploads((int) fileUploadRepository.count());
        
        // Calculate average ratings
        Double avgWorkoutRating = sessionLogRepository.getAverageRating();
        analytics.setAverageWorkoutRating(avgWorkoutRating != null ? avgWorkoutRating : 0.0);
        
        analytics.setUpdatedAt(LocalDateTime.now());
        
        DashboardAnalytics savedAnalytics = analyticsRepository.save(analytics);
        logger.info("Today's dashboard analytics updated successfully with ID: {}", savedAnalytics.getId());
        
        return convertToDTO(savedAnalytics);
    }

    // Get dashboard analytics summary
    public DashboardAnalyticsDTO getDashboardAnalyticsSummary() {
        logger.info("Getting dashboard analytics summary");
        
        // Get latest analytics or generate new ones
        Optional<DashboardAnalytics> latestAnalytics = analyticsRepository.findTopByOrderByAnalyticsDateDesc();
        if (latestAnalytics.isPresent()) {
            return convertToDTO(latestAnalytics.get());
        } else {
            return generateTodaysDashboardAnalytics();
        }
    }

    // Get dashboard analytics trends
    public List<DashboardAnalyticsDTO> getDashboardAnalyticsTrends(int days) {
        logger.info("Getting dashboard analytics trends for last {} days", days);
        
        LocalDate since = LocalDate.now().minusDays(days);
        List<DashboardAnalytics> analytics = analyticsRepository.findByAnalyticsDateAfterOrderByAnalyticsDateAsc(since);
        return analytics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Convert entity to DTO
    private DashboardAnalyticsDTO convertToDTO(DashboardAnalytics analytics) {
        DashboardAnalyticsDTO dto = new DashboardAnalyticsDTO();
        dto.setId(analytics.getId());
        dto.setDate(analytics.getDate());
        dto.setTotalClients(analytics.getTotalClients());
        dto.setActiveClients(analytics.getActiveClients());
        dto.setNewClients(analytics.getNewClients());
        dto.setTotalWorkoutPlans(analytics.getTotalWorkoutPlans());
        dto.setTotalNutritionPlans(analytics.getTotalNutritionPlans());
        dto.setTotalWorkoutSessions(analytics.getTotalWorkoutSessions());
        dto.setTotalMealLogs(analytics.getTotalMealLogs());
        dto.setTotalRevenue(analytics.getTotalRevenue());
        dto.setMonthlyRevenue(analytics.getMonthlyRevenue() != null ? analytics.getMonthlyRevenue().doubleValue() : 0.0);
        dto.setTotalNotifications(analytics.getTotalNotifications());
        dto.setUnreadNotifications(analytics.getUnreadNotifications());
        dto.setTotalFileUploads(analytics.getTotalFileUploads());
        dto.setAverageWorkoutRating(analytics.getAverageWorkoutRating());
        dto.setAverageMealRating(analytics.getAverageMealRatingAsDouble());
        dto.setTopWorkoutPlan(analytics.getTopWorkoutPlan());
        dto.setTopNutritionPlan(analytics.getTopNutritionPlan());
        dto.setMostActiveClient(analytics.getMostActiveClient());
        dto.setStatistics(analytics.getStatistics());
        dto.setCreatedAt(analytics.getCreatedAt());
        dto.setUpdatedAt(analytics.getUpdatedAt());
        
        return dto;
    }

    // Additional methods needed by DashboardAnalyticsController
    public List<DashboardAnalyticsDTO> getDashboardAnalyticsByUser(Long userId) {
        logger.info("Fetching dashboard analytics for user: {}", userId);
        
        List<DashboardAnalytics> analytics = analyticsRepository.findByUser(userRepository.findById(userId).orElse(null));
        return analytics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<DashboardAnalyticsDTO> getDashboardAnalyticsByMetric(String metric) {
        logger.info("Fetching dashboard analytics by metric: {}", metric);
        
        // This is a simplified implementation - you might want to add more specific logic
        List<DashboardAnalytics> analytics = analyticsRepository.findAll();
        return analytics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<DashboardAnalyticsDTO> getDashboardAnalyticsByUserAndMetric(Long userId, String metric) {
        logger.info("Fetching dashboard analytics for user: {} and metric: {}", userId, metric);
        
        List<DashboardAnalytics> analytics = analyticsRepository.findByUser(userRepository.findById(userId).orElse(null));
        return analytics.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public DashboardAnalyticsDTO getDashboardAnalyticsStats() {
        logger.info("Fetching dashboard analytics stats");
        
        Optional<DashboardAnalytics> latestAnalytics = analyticsRepository.findTopByOrderByAnalyticsDateDesc();
        if (latestAnalytics.isPresent()) {
            return convertToDTO(latestAnalytics.get());
        }
        
        // Return empty stats if no analytics found
        return new DashboardAnalyticsDTO();
    }
}
