package gym.backend.service;

import gym.backend.dto.WorkoutPlanDTO;
import gym.backend.dto.WorkoutPlanRequestDTO;
import gym.backend.model.WorkoutPlan;
import gym.backend.model.WorkoutPlanExercise;
import gym.backend.model.Exercise;
import gym.backend.model.User;
import gym.backend.repository.WorkoutPlanRepository;
import gym.backend.repository.ExerciseRepository;
import gym.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkoutPlanService {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutPlanService.class);

    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Autowired
    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository, 
                             ExerciseRepository exerciseRepository,
                             UserRepository userRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        logger.info("WorkoutPlanService initialized");
    }

    // Get all workout plans with pagination and filters
    public Page<WorkoutPlanDTO> getAllWorkoutPlans(Pageable pageable, String search, String category, 
                                                  String difficulty, String muscleGroup, String equipment, Boolean isPublic) {
        logger.info("Getting all workout plans with pagination and filters");
        
        if (search != null && !search.trim().isEmpty()) {
            return workoutPlanRepository.findByNameContainingIgnoreCase(search.trim(), pageable)
                    .map(this::convertToDTO);
        }
        
        if (category != null && !category.trim().isEmpty()) {
            try {
                WorkoutPlan.WorkoutCategory workoutCategory = WorkoutPlan.WorkoutCategory.valueOf(category.toUpperCase());
                return workoutPlanRepository.findByCategory(workoutCategory, pageable)
                        .map(this::convertToDTO);
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid category: {}", category);
                return Page.empty(pageable);
            }
        }
        
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            try {
                WorkoutPlan.DifficultyLevel difficultyLevel = WorkoutPlan.DifficultyLevel.valueOf(difficulty.toUpperCase());
                return workoutPlanRepository.findByDifficulty(difficultyLevel, pageable)
                        .map(this::convertToDTO);
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid difficulty level: {}", difficulty);
                return Page.empty(pageable);
            }
        }
        
        if (isPublic != null) {
            return workoutPlanRepository.findByIsPublic(isPublic, pageable)
                    .map(this::convertToDTO);
        }
        
        return workoutPlanRepository.findAll(pageable).map(this::convertToDTO);
    }

    // Get all workout plans without pagination (for dropdowns, etc.)
    public List<WorkoutPlanDTO> getAllWorkoutPlansList(String search, String category, String difficulty, 
                                                      String muscleGroup, String equipment, Boolean isPublic) {
        logger.info("Getting all workout plans list with filters");
        
        if (search != null && !search.trim().isEmpty()) {
            return workoutPlanRepository.findByNameContainingIgnoreCase(search.trim())
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        
        if (category != null && !category.trim().isEmpty()) {
            try {
                WorkoutPlan.WorkoutCategory workoutCategory = WorkoutPlan.WorkoutCategory.valueOf(category.toUpperCase());
                return workoutPlanRepository.findByCategory(workoutCategory)
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid category: {}", category);
                return List.of();
            }
        }
        
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            try {
                WorkoutPlan.DifficultyLevel difficultyLevel = WorkoutPlan.DifficultyLevel.valueOf(difficulty.toUpperCase());
                return workoutPlanRepository.findByDifficulty(difficultyLevel)
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid difficulty level: {}", difficulty);
                return List.of();
            }
        }
        
        if (isPublic != null) {
            return workoutPlanRepository.findByIsPublic(isPublic)
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        
        return workoutPlanRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get workout plan by ID
    public WorkoutPlanDTO getWorkoutPlanById(Long id) {
        logger.info("Getting workout plan by ID: {}", id);
        return workoutPlanRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    // Create new workout plan
    public WorkoutPlanDTO createWorkoutPlan(WorkoutPlanRequestDTO workoutPlanRequest) {
        logger.info("Creating new workout plan: {}", workoutPlanRequest.getName());
        
        validateWorkoutPlanRequest(workoutPlanRequest);
        
        User currentUser = getCurrentUser();
        
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setName(workoutPlanRequest.getName());
        workoutPlan.setDescription(workoutPlanRequest.getDescription());
        workoutPlan.setCategory(WorkoutPlan.WorkoutCategory.valueOf(workoutPlanRequest.getCategory().toUpperCase()));
        workoutPlan.setDifficulty(WorkoutPlan.DifficultyLevel.valueOf(workoutPlanRequest.getDifficulty().toUpperCase()));
        workoutPlan.setEstimatedDuration(workoutPlanRequest.getEstimatedDuration());
        workoutPlan.setTargetMuscleGroups(workoutPlanRequest.getTargetMuscleGroups());
        workoutPlan.setRequiredEquipment(workoutPlanRequest.getRequiredEquipment());
        workoutPlan.setTags(workoutPlanRequest.getTags());
        workoutPlan.setCreatedBy(currentUser);
        workoutPlan.setIsPublic(workoutPlanRequest.getIsPublic() != null ? workoutPlanRequest.getIsPublic() : false);
        workoutPlan.setCreatedAt(LocalDateTime.now());
        workoutPlan.setUpdatedAt(LocalDateTime.now());
        
        // Add exercises to the workout plan
        if (workoutPlanRequest.getExercises() != null && !workoutPlanRequest.getExercises().isEmpty()) {
            for (WorkoutPlanRequestDTO.ExerciseInPlanRequest exerciseRequest : workoutPlanRequest.getExercises()) {
                Exercise exercise = exerciseRepository.findById(exerciseRequest.getExerciseId())
                        .orElseThrow(() -> new IllegalArgumentException("Exercise not found with ID: " + exerciseRequest.getExerciseId()));
                
                WorkoutPlanExercise exerciseInPlan = new WorkoutPlanExercise();
                exerciseInPlan.setExercise(exercise);
                exerciseInPlan.setSets(exerciseRequest.getSets());
                exerciseInPlan.setReps(exerciseRequest.getReps());
                exerciseInPlan.setWeight(exerciseRequest.getWeight());
                exerciseInPlan.setDuration(exerciseRequest.getDuration());
                exerciseInPlan.setRestTime(exerciseRequest.getRestTime());
                exerciseInPlan.setNotes(exerciseRequest.getNotes());
                exerciseInPlan.setOrderIndex(exerciseRequest.getOrder());
                
                workoutPlan.getExercises().add(exerciseInPlan);
            }
        }
        
        WorkoutPlan savedWorkoutPlan = workoutPlanRepository.save(workoutPlan);
        logger.info("Workout plan created successfully with ID: {}", savedWorkoutPlan.getId());
        
        return convertToDTO(savedWorkoutPlan);
    }

    // Update workout plan
    public WorkoutPlanDTO updateWorkoutPlan(Long id, WorkoutPlanRequestDTO workoutPlanRequest) {
        logger.info("Updating workout plan with ID: {}", id);
        
        validateWorkoutPlanRequest(workoutPlanRequest);
        
        return workoutPlanRepository.findById(id)
                .map(workoutPlan -> {
                    workoutPlan.setName(workoutPlanRequest.getName());
                    workoutPlan.setDescription(workoutPlanRequest.getDescription());
                    workoutPlan.setCategory(WorkoutPlan.WorkoutCategory.valueOf(workoutPlanRequest.getCategory().toUpperCase()));
                    workoutPlan.setDifficulty(WorkoutPlan.DifficultyLevel.valueOf(workoutPlanRequest.getDifficulty().toUpperCase()));
                    workoutPlan.setEstimatedDuration(workoutPlanRequest.getEstimatedDuration());
                    workoutPlan.setTargetMuscleGroups(workoutPlanRequest.getTargetMuscleGroups());
                    workoutPlan.setRequiredEquipment(workoutPlanRequest.getRequiredEquipment());
                    workoutPlan.setTags(workoutPlanRequest.getTags());
                    workoutPlan.setIsPublic(workoutPlanRequest.getIsPublic() != null ? workoutPlanRequest.getIsPublic() : false);
                    workoutPlan.setUpdatedAt(LocalDateTime.now());
                    
                    // Clear existing exercises and add new ones
                    workoutPlan.getExercises().clear();
                    if (workoutPlanRequest.getExercises() != null && !workoutPlanRequest.getExercises().isEmpty()) {
                        for (WorkoutPlanRequestDTO.ExerciseInPlanRequest exerciseRequest : workoutPlanRequest.getExercises()) {
                            Exercise exercise = exerciseRepository.findById(exerciseRequest.getExerciseId())
                                    .orElseThrow(() -> new IllegalArgumentException("Exercise not found with ID: " + exerciseRequest.getExerciseId()));
                            
                            WorkoutPlanExercise exerciseInPlan = new WorkoutPlanExercise();
                            exerciseInPlan.setExercise(exercise);
                            exerciseInPlan.setSets(exerciseRequest.getSets());
                            exerciseInPlan.setReps(exerciseRequest.getReps());
                            exerciseInPlan.setWeight(exerciseRequest.getWeight());
                            exerciseInPlan.setDuration(exerciseRequest.getDuration());
                            exerciseInPlan.setRestTime(exerciseRequest.getRestTime());
                            exerciseInPlan.setNotes(exerciseRequest.getNotes());
                            exerciseInPlan.setOrderIndex(exerciseRequest.getOrder());
                            
                            workoutPlan.getExercises().add(exerciseInPlan);
                        }
                    }
                    
                    WorkoutPlan savedWorkoutPlan = workoutPlanRepository.save(workoutPlan);
                    logger.info("Workout plan updated successfully: {}", savedWorkoutPlan.getName());
                    
                    return convertToDTO(savedWorkoutPlan);
                })
                .orElse(null);
    }

    // Delete workout plan
    public boolean deleteWorkoutPlan(Long id) {
        logger.info("Deleting workout plan with ID: {}", id);
        
        if (workoutPlanRepository.existsById(id)) {
            workoutPlanRepository.deleteById(id);
            logger.info("Workout plan deleted successfully with ID: {}", id);
            return true;
        } else {
            logger.warn("Workout plan not found for deletion with ID: {}", id);
            return false;
        }
    }

    // Duplicate workout plan
    public WorkoutPlanDTO duplicateWorkoutPlan(Long id, String newName) {
        logger.info("Duplicating workout plan with ID: {}", id);
        
        return workoutPlanRepository.findById(id)
                .map(originalPlan -> {
                    User currentUser = getCurrentUser();
                    
                    WorkoutPlan duplicatedPlan = new WorkoutPlan();
                    duplicatedPlan.setName(newName != null ? newName : originalPlan.getName() + " (Kopia)");
                    duplicatedPlan.setDescription(originalPlan.getDescription());
                    duplicatedPlan.setCategory(originalPlan.getCategory());
                    duplicatedPlan.setDifficulty(originalPlan.getDifficulty());
                    duplicatedPlan.setEstimatedDuration(originalPlan.getEstimatedDuration());
                    duplicatedPlan.setTargetMuscleGroups(originalPlan.getTargetMuscleGroups());
                    duplicatedPlan.setRequiredEquipment(originalPlan.getRequiredEquipment());
                    duplicatedPlan.setTags(originalPlan.getTags());
                    duplicatedPlan.setCreatedBy(currentUser);
                    duplicatedPlan.setIsPublic(false); // Duplicated plans are private by default
                    duplicatedPlan.setCreatedAt(LocalDateTime.now());
                    duplicatedPlan.setUpdatedAt(LocalDateTime.now());
                    
                    // Copy exercises
                    for (WorkoutPlanExercise originalExercise : originalPlan.getExercises()) {
                        WorkoutPlanExercise duplicatedExercise = new WorkoutPlanExercise();
                        duplicatedExercise.setExercise(originalExercise.getExercise());
                        duplicatedExercise.setSets(originalExercise.getSets());
                        duplicatedExercise.setReps(originalExercise.getReps());
                        duplicatedExercise.setWeight(originalExercise.getWeight());
                        duplicatedExercise.setDuration(originalExercise.getDuration());
                        duplicatedExercise.setRestTime(originalExercise.getRestTime());
                        duplicatedExercise.setNotes(originalExercise.getNotes());
                        duplicatedExercise.setOrderIndex(originalExercise.getOrderIndex());
                        
                        duplicatedPlan.getExercises().add(duplicatedExercise);
                    }
                    
                    WorkoutPlan savedPlan = workoutPlanRepository.save(duplicatedPlan);
                    logger.info("Workout plan duplicated successfully with new ID: {}", savedPlan.getId());
                    
                    return convertToDTO(savedPlan);
                })
                .orElse(null);
    }

    // Search workout plans
    public Page<WorkoutPlanDTO> searchWorkoutPlans(String query, Pageable pageable) {
        logger.info("Searching workout plans with query: {}", query);
        return workoutPlanRepository.findByNameContainingIgnoreCase(query, pageable)
                .map(this::convertToDTO);
    }

    // Get workout plans by category
    public Page<WorkoutPlanDTO> getWorkoutPlansByCategory(String category, Pageable pageable) {
        logger.info("Getting workout plans by category: {}", category);
        try {
            WorkoutPlan.WorkoutCategory workoutCategory = WorkoutPlan.WorkoutCategory.valueOf(category.toUpperCase());
            return workoutPlanRepository.findByCategory(workoutCategory, pageable)
                    .map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid category: {}", category);
            return Page.empty(pageable);
        }
    }

    // Get workout plans by difficulty
    public Page<WorkoutPlanDTO> getWorkoutPlansByDifficulty(String difficulty, Pageable pageable) {
        logger.info("Getting workout plans by difficulty: {}", difficulty);
        try {
            WorkoutPlan.DifficultyLevel difficultyLevel = WorkoutPlan.DifficultyLevel.valueOf(difficulty.toUpperCase());
            return workoutPlanRepository.findByDifficulty(difficultyLevel, pageable)
                    .map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid difficulty level: {}", difficulty);
            return Page.empty(pageable);
        }
    }

    // Get public workout plans
    public Page<WorkoutPlanDTO> getPublicWorkoutPlans(Pageable pageable) {
        logger.info("Getting public workout plans");
        return workoutPlanRepository.findByIsPublic(true, pageable)
                .map(this::convertToDTO);
    }

    // Get user's workout plans
    public Page<WorkoutPlanDTO> getMyWorkoutPlans(Pageable pageable) {
        logger.info("Getting user's workout plans");
        User currentUser = getCurrentUser();
        return workoutPlanRepository.findByCreatedBy(currentUser, pageable)
                .map(this::convertToDTO);
    }

    // Validate workout plan request
    private void validateWorkoutPlanRequest(WorkoutPlanRequestDTO workoutPlanRequest) {
        if (workoutPlanRequest.getName() == null || workoutPlanRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa planu treningowego jest wymagana");
        }
        
        if (workoutPlanRequest.getCategory() == null || workoutPlanRequest.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategoria planu treningowego jest wymagana");
        }
        
        if (workoutPlanRequest.getDifficulty() == null || workoutPlanRequest.getDifficulty().trim().isEmpty()) {
            throw new IllegalArgumentException("Poziom trudności jest wymagany");
        }
        
        try {
            WorkoutPlan.WorkoutCategory.valueOf(workoutPlanRequest.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nieprawidłowa kategoria planu treningowego");
        }
        
        try {
            WorkoutPlan.DifficultyLevel.valueOf(workoutPlanRequest.getDifficulty().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nieprawidłowy poziom trudności");
        }
    }

    // Get current user
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String email = authentication.getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }
        throw new IllegalArgumentException("User not authenticated");
    }

    // Convert WorkoutPlan entity to DTO
    private WorkoutPlanDTO convertToDTO(WorkoutPlan workoutPlan) {
        WorkoutPlanDTO dto = new WorkoutPlanDTO();
        dto.setId(workoutPlan.getId());
        dto.setName(workoutPlan.getName());
        dto.setDescription(workoutPlan.getDescription());
        dto.setCategory(workoutPlan.getCategory().name());
        dto.setDifficulty(workoutPlan.getDifficulty().name());
        dto.setEstimatedDuration(workoutPlan.getEstimatedDuration());
        dto.setTargetMuscleGroups(workoutPlan.getTargetMuscleGroups());
        dto.setRequiredEquipment(workoutPlan.getRequiredEquipment());
        dto.setTags(workoutPlan.getTags());
        dto.setCreatedBy(workoutPlan.getCreatedBy().getEmail());
        dto.setIsPublic(workoutPlan.getIsPublic());
        dto.setCreatedAt(workoutPlan.getCreatedAt());
        dto.setUpdatedAt(workoutPlan.getUpdatedAt());
        
        // Convert exercises
        dto.setExercises(workoutPlan.getExercises().stream()
                .map(exerciseInPlan -> {
                    WorkoutPlanDTO.ExerciseInPlanDTO exerciseDTO = new WorkoutPlanDTO.ExerciseInPlanDTO();
                    exerciseDTO.setExerciseId(exerciseInPlan.getExercise().getId());
                    exerciseDTO.setExerciseName(exerciseInPlan.getExercise().getName());
                    exerciseDTO.setSets(exerciseInPlan.getSets());
                    exerciseDTO.setReps(exerciseInPlan.getReps());
                    exerciseDTO.setWeight(exerciseInPlan.getWeight());
                    exerciseDTO.setDuration(exerciseInPlan.getDuration());
                    exerciseDTO.setRestTime(exerciseInPlan.getRestTime());
                    exerciseDTO.setNotes(exerciseInPlan.getNotes());
                    exerciseDTO.setOrder(exerciseInPlan.getOrderIndex());
                    return exerciseDTO;
                })
                .collect(Collectors.toList()));
        
        return dto;
    }
}
