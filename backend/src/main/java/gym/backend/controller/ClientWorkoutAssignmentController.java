package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.ClientWorkoutAssignmentDTO;
import gym.backend.dto.ClientWorkoutAssignmentRequestDTO;
import gym.backend.service.ClientWorkoutAssignmentService;
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
@RequestMapping("/api/client-workout-assignments")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientWorkoutAssignmentController {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientWorkoutAssignmentController.class);
    
    @Autowired
    private ClientWorkoutAssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllWorkoutAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Page<ClientWorkoutAssignmentDTO> assignments = assignmentService.getAllWorkoutAssignments(page, size, sortBy, sortDir);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching workout assignments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout assignments: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllWorkoutAssignmentsList() {
        try {
            List<ClientWorkoutAssignmentDTO> assignments = assignmentService.getAllWorkoutAssignmentsList();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching workout assignments list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout assignments: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getWorkoutAssignmentById(@PathVariable Long id) {
        try {
            Optional<ClientWorkoutAssignmentDTO> assignment = assignmentService.getWorkoutAssignmentById(id);
            if (assignment.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Workout assignment fetched successfully", assignment.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Workout assignment not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching workout assignment by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout assignment: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createWorkoutAssignment(@RequestBody ClientWorkoutAssignmentRequestDTO request) {
        try {
            ClientWorkoutAssignmentDTO assignment = assignmentService.createWorkoutAssignment(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Workout assignment created successfully", assignment));
        } catch (Exception e) {
            logger.error("Error creating workout assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating workout assignment: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateWorkoutAssignment(
            @PathVariable Long id,
            @RequestBody ClientWorkoutAssignmentRequestDTO request) {
        
        try {
            ClientWorkoutAssignmentDTO assignment = assignmentService.updateWorkoutAssignment(id, request);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout assignment updated successfully", assignment));
        } catch (Exception e) {
            logger.error("Error updating workout assignment: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating workout assignment: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteWorkoutAssignment(@PathVariable Long id) {
        try {
            assignmentService.deleteWorkoutAssignment(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout assignment deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting workout assignment: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting workout assignment: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponseDTO> getWorkoutAssignmentsByClient(@PathVariable Long clientId) {
        try {
            List<ClientWorkoutAssignmentDTO> assignments = assignmentService.getWorkoutAssignmentsByClient(clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching workout assignments for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout assignments: " + e.getMessage(), null));
        }
    }

    @GetMapping("/workout-plan/{workoutPlanId}")
    public ResponseEntity<ApiResponseDTO> getWorkoutAssignmentsByWorkoutPlan(@PathVariable Long workoutPlanId) {
        try {
            List<ClientWorkoutAssignmentDTO> assignments = assignmentService.getWorkoutAssignmentsByWorkoutPlan(workoutPlanId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Workout assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching workout assignments for workout plan: {}", workoutPlanId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching workout assignments: " + e.getMessage(), null));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO> getActiveWorkoutAssignments() {
        try {
            List<ClientWorkoutAssignmentDTO> assignments = assignmentService.getActiveWorkoutAssignments();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Active workout assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching active workout assignments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching active workout assignments: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponseDTO> activateWorkoutAssignment(@PathVariable Long id) {
        try {
            boolean success = assignmentService.activateWorkoutAssignment(id);
            if (success) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Workout assignment activated successfully", null));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error activating workout assignment: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error activating workout assignment: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO> deactivateWorkoutAssignment(@PathVariable Long id) {
        try {
            boolean success = assignmentService.deactivateWorkoutAssignment(id);
            if (success) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Workout assignment deactivated successfully", null));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deactivating workout assignment: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deactivating workout assignment: " + e.getMessage(), null));
        }
    }
}
