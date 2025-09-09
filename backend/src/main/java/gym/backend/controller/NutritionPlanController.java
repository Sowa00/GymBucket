package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.NutritionPlanDTO;
import gym.backend.dto.NutritionPlanRequestDTO;
import gym.backend.service.NutritionPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nutrition-plans")
@CrossOrigin(origins = "http://localhost:4200")
public class NutritionPlanController {

    private static final Logger logger = LoggerFactory.getLogger(NutritionPlanController.class);

    @Autowired
    private NutritionPlanService nutritionPlanService;

    // Get all nutrition plans with pagination
    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllNutritionPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        try {
            logger.info("Fetching all nutrition plans - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                       page, size, sortBy, sortDir);
            
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                       Sort.by(sortBy).descending() : 
                       Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<NutritionPlanDTO> plans = nutritionPlanService.getAllNutritionPlans(pageable);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Nutrition plans fetched successfully", 
                plans
            ));
        } catch (Exception e) {
            logger.error("Error fetching nutrition plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching nutrition plans: " + e.getMessage(), null));
        }
    }

    // Get all nutrition plans as list
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllNutritionPlansList() {
        try {
            logger.info("Fetching all nutrition plans as list");
            List<NutritionPlanDTO> plans = nutritionPlanService.getAllNutritionPlansList();
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Nutrition plans list fetched successfully", 
                plans
            ));
        } catch (Exception e) {
            logger.error("Error fetching nutrition plans list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching nutrition plans list: " + e.getMessage(), null));
        }
    }

    // Get nutrition plan by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getNutritionPlanById(@PathVariable Long id) {
        try {
            logger.info("Fetching nutrition plan by ID: {}", id);
            Optional<NutritionPlanDTO> plan = nutritionPlanService.getNutritionPlanById(id);
            
            if (plan.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(
                    true, 
                    "Nutrition plan fetched successfully", 
                    plan.get()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Nutrition plan not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching nutrition plan by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching nutrition plan: " + e.getMessage(), null));
        }
    }

    // Create new nutrition plan
    @PostMapping
    public ResponseEntity<ApiResponseDTO> createNutritionPlan(
            @RequestBody NutritionPlanRequestDTO request,
            @RequestHeader("X-User-Id") Long userId) {
        
        try {
            logger.info("Creating new nutrition plan: {} for user: {}", request.getName(), userId);
            NutritionPlanDTO createdPlan = nutritionPlanService.createNutritionPlan(request, userId);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(
                        true, 
                        "Nutrition plan created successfully", 
                        createdPlan
                    ));
        } catch (Exception e) {
            logger.error("Error creating nutrition plan", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating nutrition plan: " + e.getMessage(), null));
        }
    }

    // Update nutrition plan
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateNutritionPlan(
            @PathVariable Long id,
            @RequestBody NutritionPlanRequestDTO request,
            @RequestHeader("X-User-Id") Long userId) {
        
        try {
            logger.info("Updating nutrition plan: {} for user: {}", id, userId);
            NutritionPlanDTO updatedPlan = nutritionPlanService.updateNutritionPlan(id, request, userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Nutrition plan updated successfully", 
                updatedPlan
            ));
        } catch (RuntimeException e) {
            logger.error("Error updating nutrition plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error updating nutrition plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating nutrition plan: " + e.getMessage(), null));
        }
    }

    // Delete nutrition plan
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteNutritionPlan(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        
        try {
            logger.info("Deleting nutrition plan: {} for user: {}", id, userId);
            nutritionPlanService.deleteNutritionPlan(id, userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Nutrition plan deleted successfully", 
                null
            ));
        } catch (RuntimeException e) {
            logger.error("Error deleting nutrition plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error deleting nutrition plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting nutrition plan: " + e.getMessage(), null));
        }
    }

    // Duplicate nutrition plan
    @PostMapping("/{id}/duplicate")
    public ResponseEntity<ApiResponseDTO> duplicateNutritionPlan(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        
        try {
            logger.info("Duplicating nutrition plan: {} for user: {}", id, userId);
            NutritionPlanDTO duplicatedPlan = nutritionPlanService.duplicateNutritionPlan(id, userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Nutrition plan duplicated successfully", 
                duplicatedPlan
            ));
        } catch (RuntimeException e) {
            logger.error("Error duplicating nutrition plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error duplicating nutrition plan: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error duplicating nutrition plan: " + e.getMessage(), null));
        }
    }

    // Search nutrition plans
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO> searchNutritionPlans(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            logger.info("Searching nutrition plans with term: {}", searchTerm);
            Pageable pageable = PageRequest.of(page, size);
            Page<NutritionPlanDTO> plans = nutritionPlanService.searchNutritionPlans(searchTerm, pageable);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Nutrition plans search completed", 
                plans
            ));
        } catch (Exception e) {
            logger.error("Error searching nutrition plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error searching nutrition plans: " + e.getMessage(), null));
        }
    }

    // Get public nutrition plans
    @GetMapping("/public")
    public ResponseEntity<ApiResponseDTO> getPublicNutritionPlans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            logger.info("Fetching public nutrition plans");
            Pageable pageable = PageRequest.of(page, size);
            Page<NutritionPlanDTO> plans = nutritionPlanService.getPublicNutritionPlans(pageable);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Public nutrition plans fetched successfully", 
                plans
            ));
        } catch (Exception e) {
            logger.error("Error fetching public nutrition plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching public nutrition plans: " + e.getMessage(), null));
        }
    }

    // Get user's nutrition plans
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO> getUserNutritionPlans(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            logger.info("Fetching nutrition plans for user: {}", userId);
            Pageable pageable = PageRequest.of(page, size);
            Page<NutritionPlanDTO> plans = nutritionPlanService.getUserNutritionPlans(userId, pageable);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "User nutrition plans fetched successfully", 
                plans
            ));
        } catch (Exception e) {
            logger.error("Error fetching user nutrition plans", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching user nutrition plans: " + e.getMessage(), null));
        }
    }
}
