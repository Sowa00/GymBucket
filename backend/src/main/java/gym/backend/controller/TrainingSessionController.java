package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.TrainingSessionDTO;
import gym.backend.service.TrainingSessionService;
import gym.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/training-sessions")
@CrossOrigin(origins = {"http://localhost:4200", "https://gymbucket.com"})
public class TrainingSessionController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingSessionController.class);

    private final TrainingSessionService trainingSessionService;
    private final UserRepository userRepository;

    @Autowired
    public TrainingSessionController(TrainingSessionService trainingSessionService, UserRepository userRepository) {
        this.trainingSessionService = trainingSessionService;
        this.userRepository = userRepository;
        logger.info("TrainingSessionController initialized");
    }

    // Create a new training session
    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody TrainingSessionDTO sessionDTO) {
        try {
            Long trainerId = getCurrentUserId();
            logger.info("Creating new training session for trainer: {}", trainerId);
            TrainingSessionDTO session = trainingSessionService.createSession(sessionDTO, trainerId);
            logger.info("Training session created successfully with id: {}", session.getId());
            return ResponseEntity.ok(session);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating training session: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error creating training session: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas tworzenia sesji treningowej"));
        }
    }

    // Get all sessions for a trainer
    @GetMapping
    public ResponseEntity<?> getAllSessions(@RequestParam(required = false) String type) {
        try {
            Long trainerId = getCurrentUserId();
            logger.debug("Getting sessions for trainer: {}", trainerId);

            List<TrainingSessionDTO> sessions;
            if ("today".equals(type)) {
                sessions = trainingSessionService.getTodaysSessions(trainerId);
            } else if ("upcoming".equals(type)) {
                sessions = trainingSessionService.getUpcomingSessions(trainerId);
            } else {
                sessions = trainingSessionService.getSessionsByTrainer(trainerId);
            }

            return ResponseEntity.ok(sessions);
        } catch (IllegalArgumentException e) {
            logger.error("Error getting sessions: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error getting sessions: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas pobierania sesji"));
        }
    }

    // Update session
    @PutMapping("/{sessionId}")
    public ResponseEntity<?> updateSession(@PathVariable Long sessionId,
                                         @RequestBody TrainingSessionDTO sessionDTO) {
        try {
            Long trainerId = getCurrentUserId();
            logger.info("Updating session: {} for trainer: {}", sessionId, trainerId);
            TrainingSessionDTO session = trainingSessionService.updateSession(sessionId, sessionDTO, trainerId);
            logger.info("Session updated successfully with id: {}", session.getId());
            return ResponseEntity.ok(session);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating session: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error updating session: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas aktualizacji sesji"));
        }
    }

    // Delete session
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<?> deleteSession(@PathVariable Long sessionId) {
        try {
            Long trainerId = getCurrentUserId();
            logger.info("Deleting session: {} for trainer: {}", sessionId, trainerId);
            trainingSessionService.deleteSession(sessionId, trainerId);
            logger.info("Session deleted successfully with id: {}", sessionId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Sesja treningowa została usunięta pomyślnie"));
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting session: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error deleting session: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas usuwania sesji"));
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new ApiResponseDTO(true, "Training session service is running"));
    }

    // Helper method to get current user ID from security context
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            String email = (String) authentication.getPrincipal();
            return userRepository.findByEmail(email)
                    .map(user -> user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        }
        throw new RuntimeException("User not authenticated");
    }
}
