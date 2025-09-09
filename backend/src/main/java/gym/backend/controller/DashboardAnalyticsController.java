package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.DashboardAnalyticsDTO;
import gym.backend.dto.DashboardAnalyticsRequestDTO;
import gym.backend.service.DashboardAnalyticsService;
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
@RequestMapping("/api/dashboard-analytics")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardAnalyticsController {
    
    private static final Logger logger = LoggerFactory.getLogger(DashboardAnalyticsController.class);
    
    @Autowired
    private DashboardAnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllDashboardAnalytics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Page<DashboardAnalyticsDTO> analytics = analyticsService.getAllDashboardAnalytics(page, size, sortBy, sortDir);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Dashboard analytics fetched successfully", analytics));
        } catch (Exception e) {
            logger.error("Error fetching dashboard analytics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching dashboard analytics: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllDashboardAnalyticsList() {
        try {
            List<DashboardAnalyticsDTO> analytics = analyticsService.getAllDashboardAnalyticsList();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Dashboard analytics fetched successfully", analytics));
        } catch (Exception e) {
            logger.error("Error fetching dashboard analytics list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching dashboard analytics: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getDashboardAnalyticsById(@PathVariable Long id) {
        try {
            Optional<DashboardAnalyticsDTO> analytics = analyticsService.getDashboardAnalyticsById(id);
            if (analytics.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Dashboard analytics fetched successfully", analytics.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Dashboard analytics not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching dashboard analytics by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching dashboard analytics: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createDashboardAnalytics(@RequestBody DashboardAnalyticsRequestDTO request) {
        try {
            DashboardAnalyticsDTO analytics = analyticsService.createDashboardAnalytics(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Dashboard analytics created successfully", analytics));
        } catch (Exception e) {
            logger.error("Error creating dashboard analytics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating dashboard analytics: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateDashboardAnalytics(
            @PathVariable Long id,
            @RequestBody DashboardAnalyticsRequestDTO request) {
        
        try {
            DashboardAnalyticsDTO analytics = analyticsService.updateDashboardAnalytics(id, request);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Dashboard analytics updated successfully", analytics));
        } catch (Exception e) {
            logger.error("Error updating dashboard analytics: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating dashboard analytics: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteDashboardAnalytics(@PathVariable Long id) {
        try {
            analyticsService.deleteDashboardAnalytics(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Dashboard analytics deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting dashboard analytics: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting dashboard analytics: " + e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO> getDashboardAnalyticsByUser(@PathVariable Long userId) {
        try {
            List<DashboardAnalyticsDTO> analytics = analyticsService.getDashboardAnalyticsByUser(userId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Dashboard analytics fetched successfully", analytics));
        } catch (Exception e) {
            logger.error("Error fetching dashboard analytics for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching dashboard analytics: " + e.getMessage(), null));
        }
    }

    @GetMapping("/metric/{metricName}")
    public ResponseEntity<ApiResponseDTO> getDashboardAnalyticsByMetric(@PathVariable String metricName) {
        try {
            List<DashboardAnalyticsDTO> analytics = analyticsService.getDashboardAnalyticsByMetric(metricName);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Dashboard analytics fetched successfully", analytics));
        } catch (Exception e) {
            logger.error("Error fetching dashboard analytics for metric: {}", metricName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching dashboard analytics: " + e.getMessage(), null));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponseDTO> getRecentDashboardAnalytics(
            @RequestParam(defaultValue = "24") int hours) {
        
        try {
            List<DashboardAnalyticsDTO> analytics = analyticsService.getRecentDashboardAnalytics(hours);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Recent dashboard analytics fetched successfully", analytics));
        } catch (Exception e) {
            logger.error("Error fetching recent dashboard analytics", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching recent dashboard analytics: " + e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}/metric/{metricName}")
    public ResponseEntity<ApiResponseDTO> getDashboardAnalyticsByUserAndMetric(
            @PathVariable Long userId,
            @PathVariable String metricName) {
        
        try {
            List<DashboardAnalyticsDTO> analytics = analyticsService.getDashboardAnalyticsByUserAndMetric(userId, metricName);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Dashboard analytics fetched successfully", analytics));
        } catch (Exception e) {
            logger.error("Error fetching dashboard analytics for user: {} and metric: {}", userId, metricName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching dashboard analytics: " + e.getMessage(), null));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponseDTO> getDashboardAnalyticsStats() {
        try {
            Object stats = analyticsService.getDashboardAnalyticsStats();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Dashboard analytics stats fetched successfully", stats));
        } catch (Exception e) {
            logger.error("Error fetching dashboard analytics stats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching dashboard analytics stats: " + e.getMessage(), null));
        }
    }
}
