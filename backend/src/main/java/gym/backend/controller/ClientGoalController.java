package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.ClientGoalDTO;
import gym.backend.dto.ClientGoalRequestDTO;
import gym.backend.model.ClientGoal;
import gym.backend.service.ClientGoalService;
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
@RequestMapping("/api/client-goals")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientGoalController {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientGoalController.class);
    
    @Autowired
    private ClientGoalService goalService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllClientGoals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Page<ClientGoalDTO> goals = goalService.getAllClientGoals(page, size, sortBy, sortDir);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Client goals fetched successfully", goals));
        } catch (Exception e) {
            logger.error("Error fetching client goals", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching client goals: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllClientGoalsList() {
        try {
            List<ClientGoalDTO> goals = goalService.getAllClientGoalsList();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Client goals fetched successfully", goals));
        } catch (Exception e) {
            logger.error("Error fetching client goals list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching client goals: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getClientGoalById(@PathVariable Long id) {
        try {
            Optional<ClientGoalDTO> goal = goalService.getClientGoalById(id);
            if (goal.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Client goal fetched successfully", goal.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Client goal not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching client goal by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching client goal: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createClientGoal(@RequestBody ClientGoalRequestDTO request) {
        try {
            ClientGoalDTO goal = goalService.createClientGoal(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Client goal created successfully", goal));
        } catch (Exception e) {
            logger.error("Error creating client goal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating client goal: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateClientGoal(
            @PathVariable Long id,
            @RequestBody ClientGoalRequestDTO request) {
        
        try {
            ClientGoalDTO goal = goalService.updateClientGoal(id, request);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Client goal updated successfully", goal));
        } catch (Exception e) {
            logger.error("Error updating client goal: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating client goal: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteClientGoal(@PathVariable Long id) {
        try {
            goalService.deleteClientGoal(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Client goal deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting client goal: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting client goal: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponseDTO> getClientGoalsByClient(@PathVariable Long clientId) {
        try {
            List<ClientGoalDTO> goals = goalService.getClientGoalsByClient(clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Client goals fetched successfully", goals));
        } catch (Exception e) {
            logger.error("Error fetching client goals for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching client goals: " + e.getMessage(), null));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponseDTO> getClientGoalsByCategory(@PathVariable String category) {
        try {
            ClientGoal.GoalCategory categoryEnum = ClientGoal.GoalCategory.valueOf(category.toUpperCase());
            List<ClientGoalDTO> goals = goalService.getClientGoalsByCategory(categoryEnum);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Client goals fetched successfully", goals));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid goal category: {}", category, e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, "Invalid goal category: " + category, null));
        } catch (Exception e) {
            logger.error("Error fetching client goals for category: {}", category, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching client goals: " + e.getMessage(), null));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponseDTO> getActiveClientGoals() {
        try {
            List<ClientGoalDTO> goals = goalService.getActiveClientGoals();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Active client goals fetched successfully", goals));
        } catch (Exception e) {
            logger.error("Error fetching active client goals", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching active client goals: " + e.getMessage(), null));
        }
    }

    @GetMapping("/completed")
    public ResponseEntity<ApiResponseDTO> getCompletedClientGoals() {
        try {
            List<ClientGoalDTO> goals = goalService.getCompletedClientGoals();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Completed client goals fetched successfully", goals));
        } catch (Exception e) {
            logger.error("Error fetching completed client goals", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching completed client goals: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponseDTO> completeClientGoal(@PathVariable Long id) {
        try {
            boolean success = goalService.completeClientGoal(id);
            if (success) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Client goal completed successfully", null));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error completing client goal: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error completing client goal: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponseDTO> activateClientGoal(@PathVariable Long id) {
        try {
            boolean success = goalService.activateClientGoal(id);
            if (success) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Client goal activated successfully", null));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error activating client goal: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error activating client goal: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO> deactivateClientGoal(@PathVariable Long id) {
        try {
            boolean success = goalService.deactivateClientGoal(id);
            if (success) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Client goal deactivated successfully", null));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deactivating client goal: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deactivating client goal: " + e.getMessage(), null));
        }
    }
}
