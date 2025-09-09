package gym.backend.service;

import gym.backend.dto.NutritionPlanDTO;
import gym.backend.dto.NutritionPlanRequestDTO;
import gym.backend.model.NutritionPlan;
import gym.backend.model.NutritionPlanMeal;
import gym.backend.model.Meal;
import gym.backend.model.User;
import gym.backend.repository.NutritionPlanRepository;
import gym.backend.repository.MealRepository;
import gym.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NutritionPlanService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionPlanService.class);

    @Autowired
    private NutritionPlanRepository nutritionPlanRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all nutrition plans with pagination
    public Page<NutritionPlanDTO> getAllNutritionPlans(Pageable pageable) {
        logger.info("Fetching all nutrition plans with pagination: {}", pageable);
        Page<NutritionPlan> plans = nutritionPlanRepository.findAll(pageable);
        return plans.map(this::convertToDTO);
    }

    // Get all nutrition plans as list (for dropdowns, etc.)
    public List<NutritionPlanDTO> getAllNutritionPlansList() {
        logger.info("Fetching all nutrition plans as list");
        List<NutritionPlan> plans = nutritionPlanRepository.findAll();
        return plans.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get nutrition plan by ID
    public Optional<NutritionPlanDTO> getNutritionPlanById(Long id) {
        logger.info("Fetching nutrition plan by ID: {}", id);
        Optional<NutritionPlan> plan = nutritionPlanRepository.findById(id);
        return plan.map(this::convertToDTO);
    }

    // Create new nutrition plan
    public NutritionPlanDTO createNutritionPlan(NutritionPlanRequestDTO request, Long userId) {
        logger.info("Creating new nutrition plan: {} for user: {}", request.getName(), userId);
        
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        NutritionPlan nutritionPlan = new NutritionPlan();
        nutritionPlan.setName(request.getName());
        nutritionPlan.setDescription(request.getDescription());
        nutritionPlan.setCategory(request.getCategory());
        nutritionPlan.setDifficulty(request.getDifficulty());
        nutritionPlan.setTargetCalories(request.getTargetCalories());
        nutritionPlan.setTargetProtein(request.getTargetProtein());
        nutritionPlan.setTargetCarbs(request.getTargetCarbs());
        nutritionPlan.setTargetFat(request.getTargetFat());
        nutritionPlan.setDuration(request.getDuration());
        nutritionPlan.setCreatedBy(currentUser);
        nutritionPlan.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : false);
        nutritionPlan.setIsTemplate(request.getIsTemplate() != null ? request.getIsTemplate() : false);
        nutritionPlan.setCreatedAt(LocalDateTime.now());
        nutritionPlan.setUpdatedAt(LocalDateTime.now());

        // Add meals if provided
        if (request.getMeals() != null && !request.getMeals().isEmpty()) {
            for (var mealRequest : request.getMeals()) {
                Meal meal = mealRepository.findById(mealRequest.getMealId())
                        .orElseThrow(() -> new RuntimeException("Meal not found with ID: " + mealRequest.getMealId()));
                
                NutritionPlanMeal nutritionPlanMeal = new NutritionPlanMeal();
                nutritionPlanMeal.setNutritionPlan(nutritionPlan);
                nutritionPlanMeal.setMeal(meal);
                nutritionPlanMeal.setDayNumber(mealRequest.getDayNumber());
                nutritionPlanMeal.setMealOrder(mealRequest.getMealOrder());
                nutritionPlanMeal.setPortionSize(mealRequest.getPortionSize() != null ? mealRequest.getPortionSize() : 1.0);
                nutritionPlanMeal.setNotes(mealRequest.getNotes());
                nutritionPlanMeal.setIsOptional(mealRequest.getIsOptional() != null ? mealRequest.getIsOptional() : false);
                
                nutritionPlan.getMeals().add(nutritionPlanMeal);
            }
        }

        NutritionPlan savedPlan = nutritionPlanRepository.save(nutritionPlan);
        logger.info("Nutrition plan created successfully with ID: {}", savedPlan.getId());
        return convertToDTO(savedPlan);
    }

    // Update nutrition plan
    public NutritionPlanDTO updateNutritionPlan(Long id, NutritionPlanRequestDTO request, Long userId) {
        logger.info("Updating nutrition plan: {} for user: {}", id, userId);
        
        NutritionPlan nutritionPlan = nutritionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nutrition plan not found with ID: " + id));

        // Check if user owns this plan or is admin
        if (!nutritionPlan.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this nutrition plan");
        }

        nutritionPlan.setName(request.getName());
        nutritionPlan.setDescription(request.getDescription());
        nutritionPlan.setCategory(request.getCategory());
        nutritionPlan.setDifficulty(request.getDifficulty());
        nutritionPlan.setTargetCalories(request.getTargetCalories());
        nutritionPlan.setTargetProtein(request.getTargetProtein());
        nutritionPlan.setTargetCarbs(request.getTargetCarbs());
        nutritionPlan.setTargetFat(request.getTargetFat());
        nutritionPlan.setDuration(request.getDuration());
        nutritionPlan.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : nutritionPlan.getIsPublic());
        nutritionPlan.setIsTemplate(request.getIsTemplate() != null ? request.getIsTemplate() : nutritionPlan.getIsTemplate());
        nutritionPlan.setUpdatedAt(LocalDateTime.now());

        // Update meals if provided
        if (request.getMeals() != null) {
            // Clear existing meals
            nutritionPlan.getMeals().clear();
            
            // Add new meals
            for (var mealRequest : request.getMeals()) {
                Meal meal = mealRepository.findById(mealRequest.getMealId())
                        .orElseThrow(() -> new RuntimeException("Meal not found with ID: " + mealRequest.getMealId()));
                
                NutritionPlanMeal nutritionPlanMeal = new NutritionPlanMeal();
                nutritionPlanMeal.setNutritionPlan(nutritionPlan);
                nutritionPlanMeal.setMeal(meal);
                nutritionPlanMeal.setDayNumber(mealRequest.getDayNumber());
                nutritionPlanMeal.setMealOrder(mealRequest.getMealOrder());
                nutritionPlanMeal.setPortionSize(mealRequest.getPortionSize() != null ? mealRequest.getPortionSize() : 1.0);
                nutritionPlanMeal.setNotes(mealRequest.getNotes());
                nutritionPlanMeal.setIsOptional(mealRequest.getIsOptional() != null ? mealRequest.getIsOptional() : false);
                
                nutritionPlan.getMeals().add(nutritionPlanMeal);
            }
        }

        NutritionPlan savedPlan = nutritionPlanRepository.save(nutritionPlan);
        logger.info("Nutrition plan updated successfully: {}", savedPlan.getName());
        return convertToDTO(savedPlan);
    }

    // Delete nutrition plan
    public void deleteNutritionPlan(Long id, Long userId) {
        logger.info("Deleting nutrition plan: {} for user: {}", id, userId);
        
        NutritionPlan nutritionPlan = nutritionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nutrition plan not found with ID: " + id));

        // Check if user owns this plan or is admin
        if (!nutritionPlan.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this nutrition plan");
        }

        nutritionPlanRepository.delete(nutritionPlan);
        logger.info("Nutrition plan deleted successfully: {}", nutritionPlan.getName());
    }

    // Duplicate nutrition plan
    public NutritionPlanDTO duplicateNutritionPlan(Long id, Long userId) {
        logger.info("Duplicating nutrition plan: {} for user: {}", id, userId);
        
        NutritionPlan originalPlan = nutritionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nutrition plan not found with ID: " + id));

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Create new plan
        NutritionPlan duplicatedPlan = new NutritionPlan();
        duplicatedPlan.setName(originalPlan.getName() + " (Kopia)");
        duplicatedPlan.setDescription(originalPlan.getDescription());
        duplicatedPlan.setCategory(originalPlan.getCategory());
        duplicatedPlan.setDifficulty(originalPlan.getDifficulty());
        duplicatedPlan.setTargetCalories(originalPlan.getTargetCalories());
        duplicatedPlan.setTargetProtein(originalPlan.getTargetProtein());
        duplicatedPlan.setTargetCarbs(originalPlan.getTargetCarbs());
        duplicatedPlan.setTargetFat(originalPlan.getTargetFat());
        duplicatedPlan.setDuration(originalPlan.getDuration());
        duplicatedPlan.setCreatedBy(currentUser);
        duplicatedPlan.setIsPublic(false); // Duplicated plans are private by default
        duplicatedPlan.setIsTemplate(false);
        duplicatedPlan.setCreatedAt(LocalDateTime.now());
        duplicatedPlan.setUpdatedAt(LocalDateTime.now());

        // Copy meals
        for (NutritionPlanMeal originalMeal : originalPlan.getMeals()) {
            NutritionPlanMeal duplicatedMeal = new NutritionPlanMeal();
            duplicatedMeal.setNutritionPlan(duplicatedPlan);
            duplicatedMeal.setMeal(originalMeal.getMeal());
            duplicatedMeal.setDayNumber(originalMeal.getDayNumber());
            duplicatedMeal.setMealOrder(originalMeal.getMealOrder());
            duplicatedMeal.setPortionSize(originalMeal.getPortionSize());
            duplicatedMeal.setNotes(originalMeal.getNotes());
            duplicatedMeal.setIsOptional(originalMeal.getIsOptional());
            
            duplicatedPlan.getMeals().add(duplicatedMeal);
        }

        NutritionPlan savedPlan = nutritionPlanRepository.save(duplicatedPlan);
        logger.info("Nutrition plan duplicated successfully with new ID: {}", savedPlan.getId());
        return convertToDTO(savedPlan);
    }

    // Search nutrition plans
    public Page<NutritionPlanDTO> searchNutritionPlans(String searchTerm, Pageable pageable) {
        logger.info("Searching nutrition plans with term: {}", searchTerm);
        Page<NutritionPlan> plans = nutritionPlanRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
        return plans.map(this::convertToDTO);
    }

    // Get nutrition plans by category
    public Page<NutritionPlanDTO> getNutritionPlansByCategory(NutritionPlan.NutritionCategory category, Pageable pageable) {
        logger.info("Fetching nutrition plans by category: {}", category);
        Page<NutritionPlan> plans = nutritionPlanRepository.findByCategory(category, pageable);
        return plans.map(this::convertToDTO);
    }

    // Get nutrition plans by difficulty
    public Page<NutritionPlanDTO> getNutritionPlansByDifficulty(NutritionPlan.DifficultyLevel difficulty, Pageable pageable) {
        logger.info("Fetching nutrition plans by difficulty: {}", difficulty);
        Page<NutritionPlan> plans = nutritionPlanRepository.findByDifficulty(difficulty, pageable);
        return plans.map(this::convertToDTO);
    }

    // Get public nutrition plans
    public Page<NutritionPlanDTO> getPublicNutritionPlans(Pageable pageable) {
        logger.info("Fetching public nutrition plans");
        Page<NutritionPlan> plans = nutritionPlanRepository.findByIsPublicTrue(pageable);
        return plans.map(this::convertToDTO);
    }

    // Get user's nutrition plans
    public Page<NutritionPlanDTO> getUserNutritionPlans(Long userId, Pageable pageable) {
        logger.info("Fetching nutrition plans for user: {}", userId);
        Page<NutritionPlan> plans = nutritionPlanRepository.findByCreatedBy_Id(userId, pageable);
        return plans.map(this::convertToDTO);
    }

    // Convert entity to DTO
    private NutritionPlanDTO convertToDTO(NutritionPlan nutritionPlan) {
        NutritionPlanDTO dto = new NutritionPlanDTO();
        dto.setId(nutritionPlan.getId());
        dto.setName(nutritionPlan.getName());
        dto.setDescription(nutritionPlan.getDescription());
        dto.setCategory(nutritionPlan.getCategory());
        dto.setDifficulty(nutritionPlan.getDifficulty());
        dto.setTargetCalories(nutritionPlan.getTargetCalories());
        dto.setTargetProtein(nutritionPlan.getTargetProtein());
        dto.setTargetCarbs(nutritionPlan.getTargetCarbs());
        dto.setTargetFat(nutritionPlan.getTargetFat());
        dto.setDuration(nutritionPlan.getDuration());
        dto.setCreatedBy(nutritionPlan.getCreatedBy().getFirstName() + " " + nutritionPlan.getCreatedBy().getLastName());
        dto.setIsPublic(nutritionPlan.getIsPublic());
        dto.setIsTemplate(nutritionPlan.getIsTemplate());
        dto.setUsageCount(nutritionPlan.getUsageCount());
        dto.setRating(nutritionPlan.getRating());
        dto.setRatingCount(nutritionPlan.getRatingCount());
        dto.setAverageRating(nutritionPlan.getAverageRating());
        dto.setCreatedAt(nutritionPlan.getCreatedAt());
        dto.setUpdatedAt(nutritionPlan.getUpdatedAt());

        // Convert meals
        dto.setMeals(nutritionPlan.getMeals().stream()
                .map(meal -> {
                    var mealDTO = new gym.backend.dto.NutritionPlanMealDTO();
                    mealDTO.setId(meal.getId());
                    mealDTO.setMealId(meal.getMeal().getId());
                    mealDTO.setMealName(meal.getMeal().getName());
                    mealDTO.setDayNumber(meal.getDayNumber());
                    mealDTO.setMealOrder(meal.getMealOrder());
                    mealDTO.setMealOrderName(meal.getMealOrderName());
                    mealDTO.setPortionSize(meal.getPortionSize());
                    mealDTO.setNotes(meal.getNotes());
                    mealDTO.setIsOptional(meal.getIsOptional());
                    return mealDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }
}
