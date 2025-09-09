package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.ClientNutritionAssignmentDTO;
import gym.backend.dto.ClientNutritionAssignmentRequestDTO;
import gym.backend.service.ClientNutritionAssignmentService;
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
@RequestMapping("/api/client-nutrition-assignments")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientNutritionAssignmentController {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientNutritionAssignmentController.class);
    
    @Autowired
    private ClientNutritionAssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllNutritionAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Page<ClientNutritionAssignmentDTO> assignments = assignmentService.getAllNutritionAssignments(page, size, sortBy, sortDir);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Nutrition assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching nutrition assignments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching nutrition assignments: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllNutritionAssignmentsList() {
        try {
            List<ClientNutritionAssignmentDTO> assignments = assignmentService.getAllNutritionAssignmentsList();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Nutrition assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching nutrition assignments list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching nutrition assignments: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getNutritionAssignmentById(@PathVariable Long id) {
        try {
            Optional<ClientNutritionAssignmentDTO> assignment = assignmentService.getNutritionAssignmentById(id);
            if (assignment.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Nutrition assignment fetched successfully", assignment.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Nutrition assignment not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching nutrition assignment by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching nutrition assignment: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createNutritionAssignment(@RequestBody ClientNutritionAssignmentRequestDTO request) {
        try {
            ClientNutritionAssignmentDTO assignment = assignmentService.createNutritionAssignment(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Nutrition assignment created successfully", assignment));
        } catch (Exception e) {
            logger.error("Error creating nutrition assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating nutrition assignment: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateNutritionAssignment(
            @PathVariable Long id,
            @RequestBody ClientNutritionAssignmentRequestDTO request) {
        
        try {
            ClientNutritionAssignmentDTO assignment = assignmentService.updateNutritionAssignment(id, request);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Nutrition assignment updated successfully", assignment));
        } catch (Exception e) {
            logger.error("Error updating nutrition assignment: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating nutrition assignment: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteNutritionAssignment(@PathVariable Long id) {
        try {
            assignmentService.deleteNutritionAssignment(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Nutrition assignment deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting nutrition assignment: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting nutrition assignment: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponseDTO> getNutritionAssignmentsByClient(@PathVariable Long clientId) {
        try {
            List<ClientNutritionAssignmentDTO> assignments = assignmentService.getNutritionAssignmentsByClient(clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Nutrition assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching nutrition assignments for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching nutrition assignments: " + e.getMessage(), null));
        }
    }

    @GetMapping("/nutrition-plan/{nutritionPlanId}")
    public ResponseEntity<ApiResponseDTO> getNutritionAssignmentsByNutritionPlan(@PathVariable Long nutritionPlanId) {
        try {
            List<ClientNutritionAssignmentDTO> assignments = assignmentService.getNutritionAssignmentsByNutritionPlan(nutritionPlanId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Nutrition assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching nutrition assignments for nutrition plan: {}", nutritionPlanId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching nutrition assignments: " + e.getMessage(), null));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO> getActiveNutritionAssignments() {
        try {
            List<ClientNutritionAssignmentDTO> assignments = assignmentService.getActiveNutritionAssignments();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Active nutrition assignments fetched successfully", assignments));
        } catch (Exception e) {
            logger.error("Error fetching active nutrition assignments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching active nutrition assignments: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponseDTO> activateNutritionAssignment(@PathVariable Long id) {
        try {
            boolean success = assignmentService.activateNutritionAssignment(id);
            if (success) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Nutrition assignment activated successfully", null));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error activating nutrition assignment: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error activating nutrition assignment: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO> deactivateNutritionAssignment(@PathVariable Long id) {
        try {
            boolean success = assignmentService.deactivateNutritionAssignment(id);
            if (success) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Nutrition assignment deactivated successfully", null));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deactivating nutrition assignment: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deactivating nutrition assignment: " + e.getMessage(), null));
        }
    }
}
