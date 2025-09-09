package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.MealLogDTO;
import gym.backend.dto.MealLogRequestDTO;
import gym.backend.service.MealLogService;
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
@RequestMapping("/api/meal-logs")
@CrossOrigin(origins = "http://localhost:4200")
public class MealLogController {
    
    private static final Logger logger = LoggerFactory.getLogger(MealLogController.class);
    
    @Autowired
    private MealLogService mealLogService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllMealLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Page<MealLogDTO> mealLogs = mealLogService.getAllMealLogs(page, size, sortBy, sortDir);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Meal logs fetched successfully", mealLogs));
        } catch (Exception e) {
            logger.error("Error fetching meal logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meal logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllMealLogsList() {
        try {
            List<MealLogDTO> mealLogs = mealLogService.getAllMealLogsList();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Meal logs fetched successfully", mealLogs));
        } catch (Exception e) {
            logger.error("Error fetching meal logs list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meal logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getMealLogById(@PathVariable Long id) {
        try {
            Optional<MealLogDTO> mealLog = mealLogService.getMealLogById(id);
            if (mealLog.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Meal log fetched successfully", mealLog.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Meal log not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching meal log by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meal log: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createMealLog(@RequestBody MealLogRequestDTO request) {
        try {
            MealLogDTO mealLog = mealLogService.createMealLog(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Meal log created successfully", mealLog));
        } catch (Exception e) {
            logger.error("Error creating meal log", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating meal log: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateMealLog(
            @PathVariable Long id,
            @RequestBody MealLogRequestDTO request) {
        
        try {
            MealLogDTO mealLog = mealLogService.updateMealLog(id, request);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Meal log updated successfully", mealLog));
        } catch (Exception e) {
            logger.error("Error updating meal log: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating meal log: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteMealLog(@PathVariable Long id) {
        try {
            mealLogService.deleteMealLog(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Meal log deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting meal log: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting meal log: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponseDTO> getMealLogsByClient(@PathVariable Long clientId) {
        try {
            List<MealLogDTO> mealLogs = mealLogService.getMealLogsByClient(clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Meal logs fetched successfully", mealLogs));
        } catch (Exception e) {
            logger.error("Error fetching meal logs for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meal logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/meal/{mealId}")
    public ResponseEntity<ApiResponseDTO> getMealLogsByMeal(@PathVariable Long mealId) {
        try {
            List<MealLogDTO> mealLogs = mealLogService.getMealLogsByMeal(mealId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Meal logs fetched successfully", mealLogs));
        } catch (Exception e) {
            logger.error("Error fetching meal logs for meal: {}", mealId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meal logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}/recent")
    public ResponseEntity<ApiResponseDTO> getRecentMealLogsByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "7") int days) {
        
        try {
            List<MealLogDTO> mealLogs = mealLogService.getRecentMealLogsByClient(clientId, days);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Recent meal logs fetched successfully", mealLogs));
        } catch (Exception e) {
            logger.error("Error fetching recent meal logs for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching recent meal logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}/stats")
    public ResponseEntity<ApiResponseDTO> getMealLogStatsByClient(@PathVariable Long clientId) {
        try {
            Object stats = mealLogService.getMealLogStatsByClient(clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Meal log stats fetched successfully", stats));
        } catch (Exception e) {
            logger.error("Error fetching meal log stats for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meal log stats: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}/date/{date}")
    public ResponseEntity<ApiResponseDTO> getMealLogsByClientAndDate(
            @PathVariable Long clientId,
            @PathVariable String date) {
        
        try {
            List<MealLogDTO> mealLogs = mealLogService.getMealLogsByClientAndDate(clientId, date);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Meal logs fetched successfully", mealLogs));
        } catch (Exception e) {
            logger.error("Error fetching meal logs for client: {} and date: {}", clientId, date, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meal logs: " + e.getMessage(), null));
        }
    }
}
