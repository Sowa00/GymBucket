package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.MealDTO;
import gym.backend.dto.MealRequestDTO;
import gym.backend.service.MealService;
import gym.backend.repository.UserRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/meals")
@CrossOrigin(origins = "http://localhost:4200")
public class MealController {

    private static final Logger logger = LoggerFactory.getLogger(MealController.class);

    @Autowired
    private MealService mealService;

    @Autowired
    private UserRepository userRepository;

    // Get all meals with pagination
    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllMeals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        try {
            logger.info("Fetching all meals - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                       page, size, sortBy, sortDir);
            
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                       Sort.by(sortBy).descending() : 
                       Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<MealDTO> meals = mealService.getAllMeals(pageable);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Meals fetched successfully", 
                meals
            ));
        } catch (Exception e) {
            logger.error("Error fetching meals", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meals: " + e.getMessage(), null));
        }
    }

    // Get all meals as list
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllMealsList() {
        try {
            logger.info("Fetching all meals as list");
            List<MealDTO> meals = mealService.getAllMealsList();
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Meals list fetched successfully", 
                meals
            ));
        } catch (Exception e) {
            logger.error("Error fetching meals list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meals list: " + e.getMessage(), null));
        }
    }

    // Get meal by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getMealById(@PathVariable Long id) {
        try {
            logger.info("Fetching meal by ID: {}", id);
            Optional<MealDTO> meal = mealService.getMealById(id);
            
            if (meal.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(
                    true, 
                    "Meal fetched successfully", 
                    meal.get()
                ));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Meal not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching meal by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching meal: " + e.getMessage(), null));
        }
    }

    // Create new meal
    @PostMapping
    public ResponseEntity<ApiResponseDTO> createMeal(@RequestBody MealRequestDTO request) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Creating new meal: {} for user: {}", request.getName(), userId);
            MealDTO createdMeal = mealService.createMeal(request, userId);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(
                        true, 
                        "Meal created successfully", 
                        createdMeal
                    ));
        } catch (Exception e) {
            logger.error("Error creating meal", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating meal: " + e.getMessage(), null));
        }
    }

    // Update meal
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateMeal(
            @PathVariable Long id,
            @RequestBody MealRequestDTO request) {
        
        try {
            Long userId = getCurrentUserId();
            logger.info("Updating meal: {} for user: {}", id, userId);
            MealDTO updatedMeal = mealService.updateMeal(id, request, userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Meal updated successfully", 
                updatedMeal
            ));
        } catch (RuntimeException e) {
            logger.error("Error updating meal: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error updating meal: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating meal: " + e.getMessage(), null));
        }
    }

    // Delete meal
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteMeal(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            logger.info("Deleting meal: {} for user: {}", id, userId);
            mealService.deleteMeal(id, userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Meal deleted successfully", 
                null
            ));
        } catch (RuntimeException e) {
            logger.error("Error deleting meal: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Error deleting meal: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting meal: " + e.getMessage(), null));
        }
    }

    // Search meals
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO> searchMeals(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            logger.info("Searching meals with term: {}", searchTerm);
            Pageable pageable = PageRequest.of(page, size);
            Page<MealDTO> meals = mealService.searchMeals(searchTerm, pageable);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Meals search completed", 
                meals
            ));
        } catch (Exception e) {
            logger.error("Error searching meals", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error searching meals: " + e.getMessage(), null));
        }
    }

    // Get public meals
    @GetMapping("/public")
    public ResponseEntity<ApiResponseDTO> getPublicMeals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            logger.info("Fetching public meals");
            Pageable pageable = PageRequest.of(page, size);
            Page<MealDTO> meals = mealService.getPublicMeals(pageable);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "Public meals fetched successfully", 
                meals
            ));
        } catch (Exception e) {
            logger.error("Error fetching public meals", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching public meals: " + e.getMessage(), null));
        }
    }

    // Get user's meals
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO> getUserMeals(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            logger.info("Fetching meals for user: {}", userId);
            Pageable pageable = PageRequest.of(page, size);
            Page<MealDTO> meals = mealService.getUserMeals(userId, pageable);
            
            return ResponseEntity.ok(new ApiResponseDTO(
                true, 
                "User meals fetched successfully", 
                meals
            ));
        } catch (Exception e) {
            logger.error("Error fetching user meals", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching user meals: " + e.getMessage(), null));
        }
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
