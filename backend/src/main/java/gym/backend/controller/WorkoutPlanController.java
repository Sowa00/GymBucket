package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.WorkoutPlanDTO;
import gym.backend.dto.WorkoutPlanRequestDTO;
import gym.backend.service.WorkoutPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-plans")
@CrossOrigin(origins = {"http://localhost:4200", "https://gymbucket.com"})
public class WorkoutPlanController {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutPlanController.class);

    private final WorkoutPlanService workoutPlanService;

    @Autowired
    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
        logger.info("WorkoutPlanController initialized");
    }

    // Get all workout plans with pagination
    @GetMapping
    public ResponseEntity<?> getAllWorkoutPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) Boolean isPublic) {
        try {
            logger.info("Getting all workout plans with filters - page: {}, size: {}, search: {}, category: {}, difficulty: {}, muscleGroup: {}, equipment: {}, isPublic: {}", 
                       page, size, search, category, difficulty, muscleGroup, equipment, isPublic);
            
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<WorkoutPlanDTO> workoutPlans = workoutPlanService.getAllWorkoutPlans(pageable, search, category, difficulty, muscleGroup, equipment, isPublic);
            
            logger.info("Found {} workout plans", workoutPlans.getTotalElements());
            return ResponseEntity.ok(workoutPlans);
        } catch (Exception e) {
            logger.error("Error getting workout plans: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania planów treningowych"));
        }
    }

    // Get all workout plans without pagination (for dropdowns, etc.)
    @GetMapping("/all")
    public ResponseEntity<?> getAllWorkoutPlansList(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) Boolean isPublic) {
        try {
            logger.info("Getting all workout plans list with filters - search: {}, category: {}, difficulty: {}, muscleGroup: {}, equipment: {}, isPublic: {}", 
                       search, category, difficulty, muscleGroup, equipment, isPublic);
            
            List<WorkoutPlanDTO> workoutPlans = workoutPlanService.getAllWorkoutPlansList(search, category, difficulty, muscleGroup, equipment, isPublic);
            
            logger.info("Found {} workout plans", workoutPlans.size());
            return ResponseEntity.ok(workoutPlans);
        } catch (Exception e) {
            logger.error("Error getting workout plans list: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania listy planów treningowych"));
        }
    }

    // Get workout plan by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkoutPlanById(@PathVariable Long id) {
        try {
            logger.info("Getting workout plan by ID: {}", id);
            WorkoutPlanDTO workoutPlan = workoutPlanService.getWorkoutPlanById(id);
            
            if (workoutPlan != null) {
                logger.info("Workout plan found: {}", workoutPlan.getName());
                return ResponseEntity.ok(workoutPlan);
            } else {
                logger.warn("Workout plan not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting workout plan by ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania planu treningowego"));
        }
    }

    // Create new workout plan
    @PostMapping
    public ResponseEntity<?> createWorkoutPlan(@RequestBody WorkoutPlanRequestDTO workoutPlanRequest) {
        try {
            logger.info("Creating new workout plan: {}", workoutPlanRequest.getName());
            WorkoutPlanDTO workoutPlan = workoutPlanService.createWorkoutPlan(workoutPlanRequest);
            
            logger.info("Workout plan created successfully with ID: {}", workoutPlan.getId());
            return ResponseEntity.ok(workoutPlan);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating workout plan: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error creating workout plan: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas tworzenia planu treningowego"));
        }
    }

    // Update workout plan
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkoutPlan(@PathVariable Long id, @RequestBody WorkoutPlanRequestDTO workoutPlanRequest) {
        try {
            logger.info("Updating workout plan with ID: {}", id);
            WorkoutPlanDTO workoutPlan = workoutPlanService.updateWorkoutPlan(id, workoutPlanRequest);
            
            if (workoutPlan != null) {
                logger.info("Workout plan updated successfully: {}", workoutPlan.getName());
                return ResponseEntity.ok(workoutPlan);
            } else {
                logger.warn("Workout plan not found for update with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error updating workout plan: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error updating workout plan: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas aktualizacji planu treningowego"));
        }
    }

    // Delete workout plan
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutPlan(@PathVariable Long id) {
        try {
            logger.info("Deleting workout plan with ID: {}", id);
            boolean deleted = workoutPlanService.deleteWorkoutPlan(id);
            
            if (deleted) {
                logger.info("Workout plan deleted successfully with ID: {}", id);
                return ResponseEntity.ok(new ApiResponseDTO(true, "Plan treningowy został usunięty pomyślnie"));
            } else {
                logger.warn("Workout plan not found for deletion with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting workout plan: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas usuwania planu treningowego"));
        }
    }

    // Duplicate workout plan
    @PostMapping("/{id}/duplicate")
    public ResponseEntity<?> duplicateWorkoutPlan(@PathVariable Long id, @RequestParam(required = false) String newName) {
        try {
            logger.info("Duplicating workout plan with ID: {}", id);
            WorkoutPlanDTO duplicatedPlan = workoutPlanService.duplicateWorkoutPlan(id, newName);
            
            if (duplicatedPlan != null) {
                logger.info("Workout plan duplicated successfully with new ID: {}", duplicatedPlan.getId());
                return ResponseEntity.ok(duplicatedPlan);
            } else {
                logger.warn("Workout plan not found for duplication with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error duplicating workout plan: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error duplicating workout plan: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas duplikowania planu treningowego"));
        }
    }

    // Search workout plans
    @GetMapping("/search")
    public ResponseEntity<?> searchWorkoutPlans(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Searching workout plans with query: {}", query);
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<WorkoutPlanDTO> workoutPlans = workoutPlanService.searchWorkoutPlans(query, pageable);
            
            logger.info("Found {} workout plans matching query: {}", workoutPlans.getTotalElements(), query);
            return ResponseEntity.ok(workoutPlans);
        } catch (Exception e) {
            logger.error("Error searching workout plans: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas wyszukiwania planów treningowych"));
        }
    }

    // Get workout plans by category
    @GetMapping("/by-category")
    public ResponseEntity<?> getWorkoutPlansByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Getting workout plans by category: {}", category);
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<WorkoutPlanDTO> workoutPlans = workoutPlanService.getWorkoutPlansByCategory(category, pageable);
            
            logger.info("Found {} workout plans for category: {}", workoutPlans.getTotalElements(), category);
            return ResponseEntity.ok(workoutPlans);
        } catch (Exception e) {
            logger.error("Error getting workout plans by category: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania planów treningowych dla kategorii"));
        }
    }

    // Get workout plans by difficulty
    @GetMapping("/by-difficulty")
    public ResponseEntity<?> getWorkoutPlansByDifficulty(
            @RequestParam String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Getting workout plans by difficulty: {}", difficulty);
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<WorkoutPlanDTO> workoutPlans = workoutPlanService.getWorkoutPlansByDifficulty(difficulty, pageable);
            
            logger.info("Found {} workout plans for difficulty: {}", workoutPlans.getTotalElements(), difficulty);
            return ResponseEntity.ok(workoutPlans);
        } catch (Exception e) {
            logger.error("Error getting workout plans by difficulty: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania planów treningowych dla poziomu trudności"));
        }
    }

    // Get public workout plans
    @GetMapping("/public")
    public ResponseEntity<?> getPublicWorkoutPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Getting public workout plans");
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<WorkoutPlanDTO> workoutPlans = workoutPlanService.getPublicWorkoutPlans(pageable);
            
            logger.info("Found {} public workout plans", workoutPlans.getTotalElements());
            return ResponseEntity.ok(workoutPlans);
        } catch (Exception e) {
            logger.error("Error getting public workout plans: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania publicznych planów treningowych"));
        }
    }

    // Get user's workout plans
    @GetMapping("/my")
    public ResponseEntity<?> getMyWorkoutPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Getting user's workout plans");
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<WorkoutPlanDTO> workoutPlans = workoutPlanService.getMyWorkoutPlans(pageable);
            
            logger.info("Found {} user workout plans", workoutPlans.getTotalElements());
            return ResponseEntity.ok(workoutPlans);
        } catch (Exception e) {
            logger.error("Error getting user's workout plans: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania planów treningowych użytkownika"));
        }
    }
}
