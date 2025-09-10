package gym.backend.controller;

import gym.backend.dto.ClientWorkoutAssignmentDTO;
import gym.backend.dto.ClientWorkoutAssignmentRequestDTO;
import gym.backend.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client-workout-assignments")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientWorkoutAssignmentController {

    private static final Logger logger = LoggerFactory.getLogger(ClientWorkoutAssignmentController.class);

    private final ClientService clientService;

    @Autowired
    public ClientWorkoutAssignmentController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Get all workout assignments for a specific client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ClientWorkoutAssignmentDTO>> getWorkoutAssignmentsByClient(@PathVariable Long clientId) {
        try {
            logger.info("Getting workout assignments for client: {}", clientId);
            List<ClientWorkoutAssignmentDTO> assignments = clientService.getAssignedWorkoutPlans(clientId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            logger.error("Error getting workout assignments for client {}: {}", clientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all workout assignments for a specific workout plan
    @GetMapping("/workout-plan/{workoutPlanId}")
    public ResponseEntity<List<ClientWorkoutAssignmentDTO>> getWorkoutAssignmentsByPlan(@PathVariable Long workoutPlanId) {
        try {
            logger.info("Getting workout assignments for workout plan: {}", workoutPlanId);
            List<ClientWorkoutAssignmentDTO> assignments = clientService.getWorkoutPlanAssignments(workoutPlanId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            logger.error("Error getting workout assignments for workout plan {}: {}", workoutPlanId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Create a new workout assignment
    @PostMapping
    public ResponseEntity<ClientWorkoutAssignmentDTO> createWorkoutAssignment(@RequestBody ClientWorkoutAssignmentRequestDTO requestDTO) {
        try {
            logger.info("Creating workout assignment: {}", requestDTO);
            ClientWorkoutAssignmentDTO assignment = clientService.assignWorkoutPlan(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request for workout assignment: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error creating workout assignment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete a workout assignment
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<Void> deleteWorkoutAssignment(@PathVariable Long assignmentId) {
        try {
            logger.info("Deleting workout assignment: {}", assignmentId);
            clientService.unassignWorkoutPlan(assignmentId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            logger.error("Assignment not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deleting workout assignment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}