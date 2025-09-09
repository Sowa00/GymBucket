package gym.backend.service;

import gym.backend.dto.ExerciseDTO;
import gym.backend.dto.ExerciseRequestDTO;
import gym.backend.model.Exercise;
import gym.backend.model.Exercise.ExerciseCategory;
import gym.backend.model.Exercise.DifficultyLevel;
import gym.backend.repository.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExerciseService {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        logger.info("ExerciseService initialized");
    }

    // Get all exercises with pagination and filters
    public Page<ExerciseDTO> getAllExercises(Pageable pageable, String search, String muscleGroup, String equipment, String difficulty) {
        logger.info("Getting all exercises with pagination and filters");
        
        if (search != null && !search.trim().isEmpty()) {
            return exerciseRepository.findByNameContainingIgnoreCase(search.trim(), pageable)
                    .map(this::convertToDTO);
        }
        
        if (muscleGroup != null && !muscleGroup.trim().isEmpty()) {
            return exerciseRepository.findByMuscleGroupsContaining(muscleGroup.trim(), pageable)
                    .map(this::convertToDTO);
        }
        
        if (equipment != null && !equipment.trim().isEmpty()) {
            return exerciseRepository.findByEquipmentContaining(equipment.trim(), pageable)
                    .map(this::convertToDTO);
        }
        
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            try {
                DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(difficulty.toUpperCase());
                return exerciseRepository.findByDifficulty(difficultyLevel, pageable)
                        .map(this::convertToDTO);
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid difficulty level: {}", difficulty);
                return Page.empty(pageable);
            }
        }
        
        return exerciseRepository.findAll(pageable).map(this::convertToDTO);
    }

    // Get all exercises without pagination (for dropdowns, etc.)
    public List<ExerciseDTO> getAllExercisesList(String search, String muscleGroup, String equipment, String difficulty) {
        logger.info("Getting all exercises list with filters");
        
        if (search != null && !search.trim().isEmpty()) {
            return exerciseRepository.findByNameContainingIgnoreCase(search.trim())
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        
        if (muscleGroup != null && !muscleGroup.trim().isEmpty()) {
            return exerciseRepository.findByMuscleGroupsContaining(muscleGroup.trim())
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        
        if (equipment != null && !equipment.trim().isEmpty()) {
            return exerciseRepository.findByEquipmentContaining(equipment.trim())
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
        
        if (difficulty != null && !difficulty.trim().isEmpty()) {
            try {
                DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(difficulty.toUpperCase());
                return exerciseRepository.findByDifficulty(difficultyLevel)
                        .stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid difficulty level: {}", difficulty);
                return List.of();
            }
        }
        
        return exerciseRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get exercise by ID
    public ExerciseDTO getExerciseById(Long id) {
        logger.info("Getting exercise by ID: {}", id);
        return exerciseRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    // Create new exercise
    public ExerciseDTO createExercise(ExerciseRequestDTO exerciseRequest) {
        logger.info("Creating new exercise: {}", exerciseRequest.getName());
        
        validateExerciseRequest(exerciseRequest);
        
        Exercise exercise = new Exercise();
        exercise.setName(exerciseRequest.getName());
        exercise.setDescription(exerciseRequest.getDescription());
        exercise.setCategory(ExerciseCategory.valueOf(exerciseRequest.getCategory().toUpperCase()));
        exercise.setDifficulty(DifficultyLevel.valueOf(exerciseRequest.getDifficulty().toUpperCase()));
        exercise.setMuscleGroups(exerciseRequest.getMuscleGroups());
        exercise.setEquipment(exerciseRequest.getEquipment());
        exercise.setInstructions(exerciseRequest.getInstructions());
        exercise.setTips(exerciseRequest.getTips());
        exercise.setWarnings(exerciseRequest.getWarnings());
        exercise.setCreatedAt(LocalDateTime.now());
        exercise.setUpdatedAt(LocalDateTime.now());
        
        Exercise savedExercise = exerciseRepository.save(exercise);
        logger.info("Exercise created successfully with ID: {}", savedExercise.getId());
        
        return convertToDTO(savedExercise);
    }

    // Update exercise
    public ExerciseDTO updateExercise(Long id, ExerciseRequestDTO exerciseRequest) {
        logger.info("Updating exercise with ID: {}", id);
        
        validateExerciseRequest(exerciseRequest);
        
        return exerciseRepository.findById(id)
                .map(exercise -> {
                    exercise.setName(exerciseRequest.getName());
                    exercise.setDescription(exerciseRequest.getDescription());
                    exercise.setCategory(ExerciseCategory.valueOf(exerciseRequest.getCategory().toUpperCase()));
                    exercise.setDifficulty(DifficultyLevel.valueOf(exerciseRequest.getDifficulty().toUpperCase()));
                    exercise.setMuscleGroups(exerciseRequest.getMuscleGroups());
                    exercise.setEquipment(exerciseRequest.getEquipment());
                    exercise.setInstructions(exerciseRequest.getInstructions());
                    exercise.setTips(exerciseRequest.getTips());
                    exercise.setWarnings(exerciseRequest.getWarnings());
                    exercise.setUpdatedAt(LocalDateTime.now());
                    
                    Exercise savedExercise = exerciseRepository.save(exercise);
                    logger.info("Exercise updated successfully: {}", savedExercise.getName());
                    
                    return convertToDTO(savedExercise);
                })
                .orElse(null);
    }

    // Delete exercise
    public boolean deleteExercise(Long id) {
        logger.info("Deleting exercise with ID: {}", id);
        
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
            logger.info("Exercise deleted successfully with ID: {}", id);
            return true;
        } else {
            logger.warn("Exercise not found for deletion with ID: {}", id);
            return false;
        }
    }

    // Search exercises
    public Page<ExerciseDTO> searchExercises(String query, Pageable pageable) {
        logger.info("Searching exercises with query: {}", query);
        return exerciseRepository.findByNameContainingIgnoreCase(query, pageable)
                .map(this::convertToDTO);
    }

    // Get exercises by muscle group
    public Page<ExerciseDTO> getExercisesByMuscleGroup(String muscleGroup, Pageable pageable) {
        logger.info("Getting exercises by muscle group: {}", muscleGroup);
        return exerciseRepository.findByMuscleGroupsContaining(muscleGroup, pageable)
                .map(this::convertToDTO);
    }

    // Get exercises by equipment
    public Page<ExerciseDTO> getExercisesByEquipment(String equipment, Pageable pageable) {
        logger.info("Getting exercises by equipment: {}", equipment);
        return exerciseRepository.findByEquipmentContaining(equipment, pageable)
                .map(this::convertToDTO);
    }

    // Get exercises by difficulty
    public Page<ExerciseDTO> getExercisesByDifficulty(String difficulty, Pageable pageable) {
        logger.info("Getting exercises by difficulty: {}", difficulty);
        try {
            DifficultyLevel difficultyLevel = DifficultyLevel.valueOf(difficulty.toUpperCase());
            return exerciseRepository.findByDifficulty(difficultyLevel, pageable)
                    .map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid difficulty level: {}", difficulty);
            return Page.empty(pageable);
        }
    }

    // Validate exercise request
    private void validateExerciseRequest(ExerciseRequestDTO exerciseRequest) {
        if (exerciseRequest.getName() == null || exerciseRequest.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa ćwiczenia jest wymagana");
        }
        
        if (exerciseRequest.getCategory() == null || exerciseRequest.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Kategoria ćwiczenia jest wymagana");
        }
        
        if (exerciseRequest.getDifficulty() == null || exerciseRequest.getDifficulty().trim().isEmpty()) {
            throw new IllegalArgumentException("Poziom trudności jest wymagany");
        }
        
        try {
            ExerciseCategory.valueOf(exerciseRequest.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nieprawidłowa kategoria ćwiczenia");
        }
        
        try {
            DifficultyLevel.valueOf(exerciseRequest.getDifficulty().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Nieprawidłowy poziom trudności");
        }
    }

    // Convert Exercise entity to DTO
    private ExerciseDTO convertToDTO(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setDescription(exercise.getDescription());
        dto.setCategory(exercise.getCategory().name());
        dto.setDifficulty(exercise.getDifficulty().name());
        dto.setMuscleGroups(exercise.getMuscleGroups());
        dto.setEquipment(exercise.getEquipment());
        dto.setInstructions(exercise.getInstructions());
        dto.setTips(exercise.getTips());
        dto.setWarnings(exercise.getWarnings());
        dto.setCreatedAt(exercise.getCreatedAt());
        dto.setUpdatedAt(exercise.getUpdatedAt());
        return dto;
    }
}
