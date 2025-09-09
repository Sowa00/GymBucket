package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.ExerciseDTO;
import gym.backend.dto.ExerciseRequestDTO;
import gym.backend.service.ExerciseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = {"http://localhost:4200", "https://gymbucket.com"})
public class ExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseController.class);

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
        logger.info("ExerciseController initialized");
    }

    // Get all exercises with pagination
    @GetMapping
    public ResponseEntity<?> getAllExercises(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) String difficulty) {
        try {
            logger.info("Getting all exercises with filters - page: {}, size: {}, search: {}, muscleGroup: {}, equipment: {}, difficulty: {}", 
                       page, size, search, muscleGroup, equipment, difficulty);
            
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<ExerciseDTO> exercises = exerciseService.getAllExercises(pageable, search, muscleGroup, equipment, difficulty);
            
            logger.info("Found {} exercises", exercises.getTotalElements());
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            logger.error("Error getting exercises: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania ćwiczeń"));
        }
    }

    // Get all exercises without pagination (for dropdowns, etc.)
    @GetMapping("/all")
    public ResponseEntity<?> getAllExercisesList(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) String equipment,
            @RequestParam(required = false) String difficulty) {
        try {
            logger.info("Getting all exercises list with filters - search: {}, muscleGroup: {}, equipment: {}, difficulty: {}", 
                       search, muscleGroup, equipment, difficulty);
            
            List<ExerciseDTO> exercises = exerciseService.getAllExercisesList(search, muscleGroup, equipment, difficulty);
            
            logger.info("Found {} exercises", exercises.size());
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            logger.error("Error getting exercises list: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania listy ćwiczeń"));
        }
    }

    // Get exercise by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getExerciseById(@PathVariable Long id) {
        try {
            logger.info("Getting exercise by ID: {}", id);
            ExerciseDTO exercise = exerciseService.getExerciseById(id);
            
            if (exercise != null) {
                logger.info("Exercise found: {}", exercise.getName());
                return ResponseEntity.ok(exercise);
            } else {
                logger.warn("Exercise not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting exercise by ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania ćwiczenia"));
        }
    }

    // Create new exercise
    @PostMapping
    public ResponseEntity<?> createExercise(@RequestBody ExerciseRequestDTO exerciseRequest) {
        try {
            logger.info("Creating new exercise: {}", exerciseRequest.getName());
            ExerciseDTO exercise = exerciseService.createExercise(exerciseRequest);
            
            logger.info("Exercise created successfully with ID: {}", exercise.getId());
            return ResponseEntity.ok(exercise);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating exercise: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error creating exercise: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas tworzenia ćwiczenia"));
        }
    }

    // Update exercise
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(@PathVariable Long id, @RequestBody ExerciseRequestDTO exerciseRequest) {
        try {
            logger.info("Updating exercise with ID: {}", id);
            ExerciseDTO exercise = exerciseService.updateExercise(id, exerciseRequest);
            
            if (exercise != null) {
                logger.info("Exercise updated successfully: {}", exercise.getName());
                return ResponseEntity.ok(exercise);
            } else {
                logger.warn("Exercise not found for update with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error updating exercise: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error updating exercise: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas aktualizacji ćwiczenia"));
        }
    }

    // Delete exercise
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id) {
        try {
            logger.info("Deleting exercise with ID: {}", id);
            boolean deleted = exerciseService.deleteExercise(id);
            
            if (deleted) {
                logger.info("Exercise deleted successfully with ID: {}", id);
                return ResponseEntity.ok(new ApiResponseDTO(true, "Ćwiczenie zostało usunięte pomyślnie"));
            } else {
                logger.warn("Exercise not found for deletion with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error deleting exercise: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas usuwania ćwiczenia"));
        }
    }

    // Search exercises
    @GetMapping("/search")
    public ResponseEntity<?> searchExercises(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Searching exercises with query: {}", query);
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<ExerciseDTO> exercises = exerciseService.searchExercises(query, pageable);
            
            logger.info("Found {} exercises matching query: {}", exercises.getTotalElements(), query);
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            logger.error("Error searching exercises: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas wyszukiwania ćwiczeń"));
        }
    }

    // Get exercises by muscle group
    @GetMapping("/by-muscle-group")
    public ResponseEntity<?> getExercisesByMuscleGroup(
            @RequestParam String muscleGroup,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Getting exercises by muscle group: {}", muscleGroup);
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<ExerciseDTO> exercises = exerciseService.getExercisesByMuscleGroup(muscleGroup, pageable);
            
            logger.info("Found {} exercises for muscle group: {}", exercises.getTotalElements(), muscleGroup);
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            logger.error("Error getting exercises by muscle group: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania ćwiczeń dla grupy mięśniowej"));
        }
    }

    // Get exercises by equipment
    @GetMapping("/by-equipment")
    public ResponseEntity<?> getExercisesByEquipment(
            @RequestParam String equipment,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Getting exercises by equipment: {}", equipment);
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<ExerciseDTO> exercises = exerciseService.getExercisesByEquipment(equipment, pageable);
            
            logger.info("Found {} exercises for equipment: {}", exercises.getTotalElements(), equipment);
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            logger.error("Error getting exercises by equipment: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania ćwiczeń dla sprzętu"));
        }
    }

    // Get exercises by difficulty
    @GetMapping("/by-difficulty")
    public ResponseEntity<?> getExercisesByDifficulty(
            @RequestParam String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Getting exercises by difficulty: {}", difficulty);
            Pageable pageable = Pageable.ofSize(size).withPage(page);
            Page<ExerciseDTO> exercises = exerciseService.getExercisesByDifficulty(difficulty, pageable);
            
            logger.info("Found {} exercises for difficulty: {}", exercises.getTotalElements(), difficulty);
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            logger.error("Error getting exercises by difficulty: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił błąd podczas pobierania ćwiczeń dla poziomu trudności"));
        }
    }
}
