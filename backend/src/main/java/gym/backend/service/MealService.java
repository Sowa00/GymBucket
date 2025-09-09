package gym.backend.service;

import gym.backend.dto.MealDTO;
import gym.backend.dto.MealRequestDTO;
import gym.backend.model.Meal;
import gym.backend.model.MealIngredient;
import gym.backend.model.User;
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
public class MealService {

    private static final Logger logger = LoggerFactory.getLogger(MealService.class);

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all meals with pagination
    public Page<MealDTO> getAllMeals(Pageable pageable) {
        logger.info("Fetching all meals with pagination: {}", pageable);
        Page<Meal> meals = mealRepository.findAll(pageable);
        return meals.map(this::convertToDTO);
    }

    // Get all meals as list (for dropdowns, etc.)
    public List<MealDTO> getAllMealsList() {
        logger.info("Fetching all meals as list");
        List<Meal> meals = mealRepository.findAll();
        return meals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get meal by ID
    public Optional<MealDTO> getMealById(Long id) {
        logger.info("Fetching meal by ID: {}", id);
        Optional<Meal> meal = mealRepository.findById(id);
        return meal.map(this::convertToDTO);
    }

    // Create new meal
    public MealDTO createMeal(MealRequestDTO request, Long userId) {
        logger.info("Creating new meal: {} for user: {}", request.getName(), userId);
        
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Meal meal = new Meal();
        meal.setName(request.getName());
        meal.setDescription(request.getDescription());
        meal.setCategory(request.getCategory());
        meal.setCalories(request.getCalories());
        meal.setProtein(request.getProtein());
        meal.setCarbs(request.getCarbs());
        meal.setFat(request.getFat());
        meal.setFiber(request.getFiber());
        meal.setSugar(request.getSugar());
        meal.setSodium(request.getSodium());
        meal.setInstructions(request.getInstructions());
        meal.setPrepTime(request.getPrepTime());
        meal.setCookTime(request.getCookTime());
        meal.setServings(request.getServings());
        meal.setIsCustom(request.getIsCustom() != null ? request.getIsCustom() : true);
        meal.setCreatedBy(currentUser);
        meal.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : false);
        meal.setCreatedAt(LocalDateTime.now());
        meal.setUpdatedAt(LocalDateTime.now());

        // Add ingredients if provided
        if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
            for (var ingredientRequest : request.getIngredients()) {
                MealIngredient ingredient = new MealIngredient();
                ingredient.setMeal(meal);
                ingredient.setName(ingredientRequest.getName());
                ingredient.setAmount(ingredientRequest.getAmount());
                ingredient.setUnit(ingredientRequest.getUnit());
                ingredient.setCalories(ingredientRequest.getCalories());
                ingredient.setProtein(ingredientRequest.getProtein());
                ingredient.setCarbs(ingredientRequest.getCarbs());
                ingredient.setFat(ingredientRequest.getFat());
                ingredient.setFiber(ingredientRequest.getFiber());
                ingredient.setSugar(ingredientRequest.getSugar());
                ingredient.setSodium(ingredientRequest.getSodium());
                
                meal.getIngredients().add(ingredient);
            }
        }

        Meal savedMeal = mealRepository.save(meal);
        logger.info("Meal created successfully with ID: {}", savedMeal.getId());
        return convertToDTO(savedMeal);
    }

    // Update meal
    public MealDTO updateMeal(Long id, MealRequestDTO request, Long userId) {
        logger.info("Updating meal: {} for user: {}", id, userId);
        
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found with ID: " + id));

        // Check if user owns this meal or is admin
        if (meal.getCreatedBy() != null && !meal.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to update this meal");
        }

        meal.setName(request.getName());
        meal.setDescription(request.getDescription());
        meal.setCategory(request.getCategory());
        meal.setCalories(request.getCalories());
        meal.setProtein(request.getProtein());
        meal.setCarbs(request.getCarbs());
        meal.setFat(request.getFat());
        meal.setFiber(request.getFiber());
        meal.setSugar(request.getSugar());
        meal.setSodium(request.getSodium());
        meal.setInstructions(request.getInstructions());
        meal.setPrepTime(request.getPrepTime());
        meal.setCookTime(request.getCookTime());
        meal.setServings(request.getServings());
        meal.setIsCustom(request.getIsCustom() != null ? request.getIsCustom() : meal.getIsCustom());
        meal.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : meal.getIsPublic());
        meal.setUpdatedAt(LocalDateTime.now());

        // Update ingredients if provided
        if (request.getIngredients() != null) {
            // Clear existing ingredients
            meal.getIngredients().clear();
            
            // Add new ingredients
            for (var ingredientRequest : request.getIngredients()) {
                MealIngredient ingredient = new MealIngredient();
                ingredient.setMeal(meal);
                ingredient.setName(ingredientRequest.getName());
                ingredient.setAmount(ingredientRequest.getAmount());
                ingredient.setUnit(ingredientRequest.getUnit());
                ingredient.setCalories(ingredientRequest.getCalories());
                ingredient.setProtein(ingredientRequest.getProtein());
                ingredient.setCarbs(ingredientRequest.getCarbs());
                ingredient.setFat(ingredientRequest.getFat());
                ingredient.setFiber(ingredientRequest.getFiber());
                ingredient.setSugar(ingredientRequest.getSugar());
                ingredient.setSodium(ingredientRequest.getSodium());
                
                meal.getIngredients().add(ingredient);
            }
        }

        Meal savedMeal = mealRepository.save(meal);
        logger.info("Meal updated successfully: {}", savedMeal.getName());
        return convertToDTO(savedMeal);
    }

    // Delete meal
    public void deleteMeal(Long id, Long userId) {
        logger.info("Deleting meal: {} for user: {}", id, userId);
        
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal not found with ID: " + id));

        // Check if user owns this meal or is admin
        if (meal.getCreatedBy() != null && !meal.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this meal");
        }

        mealRepository.delete(meal);
        logger.info("Meal deleted successfully: {}", meal.getName());
    }

    // Search meals
    public Page<MealDTO> searchMeals(String searchTerm, Pageable pageable) {
        logger.info("Searching meals with term: {}", searchTerm);
        Page<Meal> meals = mealRepository.findByNameContainingIgnoreCase(searchTerm, pageable);
        return meals.map(this::convertToDTO);
    }

    // Get meals by category
    public Page<MealDTO> getMealsByCategory(Meal.MealCategory category, Pageable pageable) {
        logger.info("Fetching meals by category: {}", category);
        Page<Meal> meals = mealRepository.findByCategory(category, pageable);
        return meals.map(this::convertToDTO);
    }

    // Get public meals
    public Page<MealDTO> getPublicMeals(Pageable pageable) {
        logger.info("Fetching public meals");
        Page<Meal> meals = mealRepository.findByIsPublicTrue(pageable);
        return meals.map(this::convertToDTO);
    }

    // Get user's meals
    public Page<MealDTO> getUserMeals(Long userId, Pageable pageable) {
        logger.info("Fetching meals for user: {}", userId);
        Page<Meal> meals = mealRepository.findByCreatedBy_Id(userId, pageable);
        return meals.map(this::convertToDTO);
    }

    // Convert entity to DTO
    private MealDTO convertToDTO(Meal meal) {
        MealDTO dto = new MealDTO();
        dto.setId(meal.getId());
        dto.setName(meal.getName());
        dto.setDescription(meal.getDescription());
        dto.setCategory(meal.getCategory());
        dto.setCalories(meal.getCalories());
        dto.setProtein(meal.getProtein());
        dto.setCarbs(meal.getCarbs());
        dto.setFat(meal.getFat());
        dto.setFiber(meal.getFiber());
        dto.setSugar(meal.getSugar());
        dto.setSodium(meal.getSodium());
        dto.setInstructions(meal.getInstructions());
        dto.setPrepTime(meal.getPrepTime());
        dto.setCookTime(meal.getCookTime());
        dto.setServings(meal.getServings());
        dto.setIsCustom(meal.getIsCustom());
        dto.setCreatedBy(meal.getCreatedBy() != null ? 
                meal.getCreatedBy().getFirstName() + " " + meal.getCreatedBy().getLastName() : "System");
        dto.setIsPublic(meal.getIsPublic());
        dto.setUsageCount(meal.getUsageCount());
        dto.setCreatedAt(meal.getCreatedAt());
        dto.setUpdatedAt(meal.getUpdatedAt());

        // Convert ingredients
        dto.setIngredients(meal.getIngredients().stream()
                .map(ingredient -> {
                    var ingredientDTO = new gym.backend.dto.MealIngredientDTO();
                    ingredientDTO.setId(ingredient.getId());
                    ingredientDTO.setName(ingredient.getName());
                    ingredientDTO.setAmount(ingredient.getAmount());
                    ingredientDTO.setUnit(ingredient.getUnit());
                    ingredientDTO.setCalories(ingredient.getCalories());
                    ingredientDTO.setProtein(ingredient.getProtein());
                    ingredientDTO.setCarbs(ingredient.getCarbs());
                    ingredientDTO.setFat(ingredient.getFat());
                    ingredientDTO.setFiber(ingredient.getFiber());
                    ingredientDTO.setSugar(ingredient.getSugar());
                    ingredientDTO.setSodium(ingredient.getSodium());
                    return ingredientDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }
}
