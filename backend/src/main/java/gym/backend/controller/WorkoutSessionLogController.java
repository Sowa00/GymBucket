package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.WorkoutSessionLogDTO;
import gym.backend.dto.WorkoutSessionLogRequestDTO;
import gym.backend.service.WorkoutSessionLogService;
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
@RequestMapping("/api/workout-session-logs")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkoutSessionLogController {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkoutSessionLogController.class);
    
    @Autowired
    private WorkoutSessionLogService sessionLogService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllWorkoutSessionLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Page<WorkoutSessionLogDTO> sessionLogs = sessionLogService.getAllWorkoutSessionLogs(page, size, sortBy, sortDir);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout session logs fetched successfully", sessionLogs));
        } catch (Exception e) {
            logger.error("Error fetching workout session logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout session logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllWorkoutSessionLogsList() {
        try {
            List<WorkoutSessionLogDTO> sessionLogs = sessionLogService.getAllWorkoutSessionLogsList();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout session logs fetched successfully", sessionLogs));
        } catch (Exception e) {
            logger.error("Error fetching workout session logs list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout session logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getWorkoutSessionLogById(@PathVariable Long id) {
        try {
            Optional<WorkoutSessionLogDTO> sessionLog = sessionLogService.getWorkoutSessionLogById(id);
            if (sessionLog.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Workout session log fetched successfully", sessionLog.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Workout session log not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching workout session log by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout session log: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createWorkoutSessionLog(@RequestBody WorkoutSessionLogRequestDTO request) {
        try {
            WorkoutSessionLogDTO sessionLog = sessionLogService.createWorkoutSessionLog(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Workout session log created successfully", sessionLog));
        } catch (Exception e) {
            logger.error("Error creating workout session log", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating workout session log: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateWorkoutSessionLog(
            @PathVariable Long id,
            @RequestBody WorkoutSessionLogRequestDTO request) {
        
        try {
            WorkoutSessionLogDTO sessionLog = sessionLogService.updateWorkoutSessionLog(id, request);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout session log updated successfully", sessionLog));
        } catch (Exception e) {
            logger.error("Error updating workout session log: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating workout session log: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteWorkoutSessionLog(@PathVariable Long id) {
        try {
            sessionLogService.deleteWorkoutSessionLog(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout session log deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting workout session log: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting workout session log: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponseDTO> getWorkoutSessionLogsByClient(@PathVariable Long clientId) {
        try {
            List<WorkoutSessionLogDTO> sessionLogs = sessionLogService.getWorkoutSessionLogsByClient(clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout session logs fetched successfully", sessionLogs));
        } catch (Exception e) {
            logger.error("Error fetching workout session logs for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout session logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/workout-plan/{workoutPlanId}")
    public ResponseEntity<ApiResponseDTO> getWorkoutSessionLogsByWorkoutPlan(@PathVariable Long workoutPlanId) {
        try {
            List<WorkoutSessionLogDTO> sessionLogs = sessionLogService.getWorkoutSessionLogsByWorkoutPlan(workoutPlanId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout session logs fetched successfully", sessionLogs));
        } catch (Exception e) {
            logger.error("Error fetching workout session logs for workout plan: {}", workoutPlanId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout session logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}/recent")
    public ResponseEntity<ApiResponseDTO> getRecentWorkoutSessionLogsByClient(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "7") int days) {
        
        try {
            List<WorkoutSessionLogDTO> sessionLogs = sessionLogService.getRecentWorkoutSessionLogsByClient(clientId, days);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Recent workout session logs fetched successfully", sessionLogs));
        } catch (Exception e) {
            logger.error("Error fetching recent workout session logs for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching recent workout session logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}/stats")
    public ResponseEntity<ApiResponseDTO> getWorkoutSessionStatsByClient(@PathVariable Long clientId) {
        try {
            Object stats = sessionLogService.getWorkoutSessionStatsByClient(clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout session stats fetched successfully", stats));
        } catch (Exception e) {
            logger.error("Error fetching workout session stats for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout session stats: " + e.getMessage(), null));
        }
    }
}
