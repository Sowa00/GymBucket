package gym.backend.controller;

import gym.backend.dto.ClientNutritionAssignmentDTO;
import gym.backend.dto.ClientNutritionAssignmentRequestDTO;
import gym.backend.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client-nutrition-assignments")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientNutritionAssignmentController {

    private static final Logger logger = LoggerFactory.getLogger(ClientNutritionAssignmentController.class);

    private final ClientService clientService;

    @Autowired
    public ClientNutritionAssignmentController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Get all nutrition assignments for a specific client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClientNutritionAssignmentDTO>> getNutritionAssignmentsByClient(@PathVariable Long clientId) {
        try {
            logger.info("Getting nutrition assignments for client: {}", clientId);
            List<ClientNutritionAssignmentDTO> assignments = clientService.getAssignedNutritionPlans(clientId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            logger.error("Error getting nutrition assignments for client {}: {}", clientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all nutrition assignments for a specific nutrition plan
    @GetMapping("/nutrition-plan/{nutritionPlanId}")
    public ResponseEntity<List<ClientNutritionAssignmentDTO>> getNutritionAssignmentsByPlan(@PathVariable Long nutritionPlanId) {
        try {
            logger.info("Getting nutrition assignments for nutrition plan: {}", nutritionPlanId);
            List<ClientNutritionAssignmentDTO> assignments = clientService.getNutritionPlanAssignments(nutritionPlanId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            logger.error("Error getting nutrition assignments for nutrition plan {}: {}", nutritionPlanId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create a new nutrition assignment
    @PostMapping
    public ResponseEntity<ClientNutritionAssignmentDTO> createNutritionAssignment(@RequestBody ClientNutritionAssignmentRequestDTO requestDTO) {
        try {
            logger.info("Creating nutrition assignment: {}", requestDTO);
            ClientNutritionAssignmentDTO assignment = clientService.assignNutritionPlan(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request for nutrition assignment: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error creating nutrition assignment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete a nutrition assignment
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteNutritionAssignment(@PathVariable Long assignmentId) {
        try {
            logger.info("Deleting nutrition assignment: {}", assignmentId);
            clientService.unassignNutritionPlan(assignmentId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Assignment not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deleting nutrition assignment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}