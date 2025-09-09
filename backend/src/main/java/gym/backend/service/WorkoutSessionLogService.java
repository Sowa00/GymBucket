package gym.backend.service;

import gym.backend.dto.WorkoutSessionLogDTO;
import gym.backend.dto.WorkoutSessionLogRequestDTO;
import gym.backend.model.WorkoutSessionLog;
import gym.backend.model.User;
import gym.backend.model.WorkoutPlan;
import gym.backend.repository.WorkoutSessionLogRepository;
import gym.backend.repository.UserRepository;
import gym.backend.repository.WorkoutPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkoutSessionLogService {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkoutSessionLogService.class);
    
    @Autowired
    private WorkoutSessionLogRepository sessionLogRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    // Get all workout session logs with pagination
    public Page<WorkoutSessionLogDTO> getAllWorkoutSessionLogs(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all workout session logs with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<WorkoutSessionLog> sessionLogs = sessionLogRepository.findAll(pageable);
        
        return sessionLogs.map(this::convertToDTO);
    }

    // Get all workout session logs as list
    public List<WorkoutSessionLogDTO> getAllWorkoutSessionLogsList() {
        logger.info("Fetching all workout session logs as list");
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findAll();
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout session log by ID
    public Optional<WorkoutSessionLogDTO> getWorkoutSessionLogById(Long id) {
        logger.info("Fetching workout session log by ID: {}", id);
        return sessionLogRepository.findById(id).map(this::convertToDTO);
    }

    // Create new workout session log
    public WorkoutSessionLogDTO createWorkoutSessionLog(WorkoutSessionLogRequestDTO request) {
        logger.info("Creating new workout session log for client: {} and workout plan: {}", 
                   request.getClientId(), request.getWorkoutPlanId());
        
        Optional<User> clientOpt = userRepository.findById(request.getClientId());
        if (clientOpt.isEmpty()) {
            throw new RuntimeException("Client not found with ID: " + request.getClientId());
        }
        
        Optional<WorkoutPlan> workoutPlanOpt = workoutPlanRepository.findById(request.getWorkoutPlanId());
        if (workoutPlanOpt.isEmpty()) {
            throw new RuntimeException("Workout plan not found with ID: " + request.getWorkoutPlanId());
        }
        
        Optional<User> loggedByOpt = userRepository.findById(request.getLoggedById());
        if (loggedByOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + request.getLoggedById());
        }
        
        WorkoutSessionLog sessionLog = new WorkoutSessionLog();
        sessionLog.setClientId(request.getClientId());
        sessionLog.setWorkoutPlanId(request.getWorkoutPlanId());
        sessionLog.setSessionDate(request.getSessionDate());
        sessionLog.setStartTime(request.getStartTime());
        sessionLog.setEndTime(request.getEndTime());
        sessionLog.setTotalDuration(request.getTotalDuration());
        sessionLog.setExercisesCompleted(request.getExercisesCompleted());
        sessionLog.setCaloriesBurned(request.getCaloriesBurned());
        sessionLog.setNotes(request.getNotes());
        sessionLog.setRating(request.getRating());
        sessionLog.setDifficultyRating(request.getDifficultyRating());
        sessionLog.setLoggedById(request.getLoggedById());
        sessionLog.setCreatedAt(LocalDateTime.now());
        sessionLog.setUpdatedAt(LocalDateTime.now());
        
        WorkoutSessionLog savedSessionLog = sessionLogRepository.save(sessionLog);
        logger.info("Workout session log created successfully with ID: {}", savedSessionLog.getId());
        
        return convertToDTO(savedSessionLog);
    }

    // Update workout session log
    public WorkoutSessionLogDTO updateWorkoutSessionLog(Long id, WorkoutSessionLogRequestDTO request) {
        logger.info("Updating workout session log: {}", id);
        
        Optional<WorkoutSessionLog> sessionLogOpt = sessionLogRepository.findById(id);
        if (sessionLogOpt.isEmpty()) {
            throw new RuntimeException("Workout session log not found with ID: " + id);
        }
        
        WorkoutSessionLog sessionLog = sessionLogOpt.get();
        sessionLog.setSessionDate(request.getSessionDate());
        sessionLog.setStartTime(request.getStartTime());
        sessionLog.setEndTime(request.getEndTime());
        sessionLog.setTotalDuration(request.getTotalDuration());
        sessionLog.setExercisesCompleted(request.getExercisesCompleted());
        sessionLog.setCaloriesBurned(request.getCaloriesBurned());
        sessionLog.setNotes(request.getNotes());
        sessionLog.setRating(request.getRating());
        sessionLog.setDifficultyRating(request.getDifficultyRating());
        sessionLog.setLoggedById(request.getLoggedById());
        sessionLog.setUpdatedAt(LocalDateTime.now());
        
        WorkoutSessionLog savedSessionLog = sessionLogRepository.save(sessionLog);
        logger.info("Workout session log updated successfully with ID: {}", savedSessionLog.getId());
        
        return convertToDTO(savedSessionLog);
    }

    // Delete workout session log
    public void deleteWorkoutSessionLog(Long id) {
        logger.info("Deleting workout session log: {}", id);
        
        Optional<WorkoutSessionLog> sessionLogOpt = sessionLogRepository.findById(id);
        if (sessionLogOpt.isEmpty()) {
            throw new RuntimeException("Workout session log not found with ID: " + id);
        }
        
        sessionLogRepository.deleteById(id);
        logger.info("Workout session log deleted successfully with ID: {}", id);
    }

    // Get workout session logs by client
    public List<WorkoutSessionLogDTO> getWorkoutSessionLogsByClient(Long clientId) {
        logger.info("Fetching workout session logs for client: {}", clientId);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByClient_Id(clientId);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout session logs by client with pagination
    public Page<WorkoutSessionLogDTO> getWorkoutSessionLogsByClient(Long clientId, int page, int size) {
        logger.info("Fetching workout session logs for client: {} with pagination", clientId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("sessionDate").descending());
        Page<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByClient_Id(clientId, pageable);
        
        return sessionLogs.map(this::convertToDTO);
    }

    // Additional methods needed by WorkoutSessionLogController
    public List<WorkoutSessionLogDTO> getRecentWorkoutSessionLogsByClient(Long clientId, int limit) {
        logger.info("Fetching recent workout session logs for client: {} with limit: {}", clientId, limit);
        
        Pageable pageable = PageRequest.of(0, limit, Sort.by("sessionDate").descending());
        Page<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByClient_Id(clientId, pageable);
        return sessionLogs.getContent().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Map<String, Object> getWorkoutSessionStatsByClient(Long clientId) {
        logger.info("Fetching workout session stats for client: {}", clientId);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByClient_Id(clientId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSessions", sessionLogs.size());
        stats.put("totalDuration", sessionLogs.stream().mapToInt(wsl -> wsl.getTotalDuration() != null ? wsl.getTotalDuration() : 0).sum());
        stats.put("totalCaloriesBurned", sessionLogs.stream().mapToInt(wsl -> wsl.getCaloriesBurned() != null ? wsl.getCaloriesBurned() : 0).sum());
        stats.put("averageRating", sessionLogs.stream().filter(wsl -> wsl.getRating() != null).mapToDouble(wsl -> wsl.getRating().doubleValue()).average().orElse(0.0));
        stats.put("averageDifficulty", sessionLogs.stream().filter(wsl -> wsl.getDifficultyRating() != null).mapToDouble(wsl -> wsl.getDifficultyRating().doubleValue()).average().orElse(0.0));
        
        return stats;
    }

    // Get workout session logs by workout plan
    public List<WorkoutSessionLogDTO> getWorkoutSessionLogsByWorkoutPlan(Long workoutPlanId) {
        logger.info("Fetching workout session logs for workout plan: {}", workoutPlanId);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByWorkoutPlan_Id(workoutPlanId);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout session logs by logged by user
    public List<WorkoutSessionLogDTO> getWorkoutSessionLogsByLoggedBy(Long loggedById) {
        logger.info("Fetching workout session logs logged by user: {}", loggedById);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByLoggedBy_Id(loggedById);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout session logs by date range
    public List<WorkoutSessionLogDTO> getWorkoutSessionLogsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching workout session logs from {} to {}", startDate, endDate);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findBySessionDateBetween(startDate, endDate);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout session logs by client and date range
    public List<WorkoutSessionLogDTO> getWorkoutSessionLogsByClientAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching workout session logs for client: {} from {} to {}", clientId, startDate, endDate);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByClient_IdAndSessionDateBetween(clientId, startDate, endDate);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent workout session logs
    public List<WorkoutSessionLogDTO> getRecentWorkoutSessionLogs(int days) {
        logger.info("Fetching recent workout session logs from last {} days", days);
        
        LocalDate since = LocalDate.now().minusDays(days);
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findBySessionDateAfter(since);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get today's workout session logs
    public List<WorkoutSessionLogDTO> getTodaysWorkoutSessionLogs() {
        logger.info("Fetching today's workout session logs");
        
        LocalDate today = LocalDate.now();
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findBySessionDate(today);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this week's workout session logs
    public List<WorkoutSessionLogDTO> getThisWeeksWorkoutSessionLogs() {
        logger.info("Fetching this week's workout session logs");
        
        LocalDate startOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findBySessionDateBetween(startOfWeek, endOfWeek);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this month's workout session logs
    public List<WorkoutSessionLogDTO> getThisMonthsWorkoutSessionLogs() {
        logger.info("Fetching this month's workout session logs");
        
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findBySessionDateBetween(startOfMonth, endOfMonth);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout session logs by rating
    public List<WorkoutSessionLogDTO> getWorkoutSessionLogsByRating(Integer rating) {
        logger.info("Fetching workout session logs with rating: {}", rating);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByRating(rating);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout session logs by difficulty rating
    public List<WorkoutSessionLogDTO> getWorkoutSessionLogsByDifficultyRating(Integer difficultyRating) {
        logger.info("Fetching workout session logs with difficulty rating: {}", difficultyRating);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByDifficultyRating(difficultyRating);
        return sessionLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get average rating for client
    public Double getAverageRatingForClient(Long clientId) {
        logger.info("Calculating average rating for client: {}", clientId);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByClient_IdAndRatingIsNotNull(clientId);
        if (sessionLogs.isEmpty()) {
            return 0.0;
        }
        
        double sum = sessionLogs.stream().mapToDouble(WorkoutSessionLog::getRating).sum();
        return sum / sessionLogs.size();
    }

    // Get total calories burned for client
    public Integer getTotalCaloriesBurnedForClient(Long clientId) {
        logger.info("Calculating total calories burned for client: {}", clientId);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByClient_IdAndCaloriesBurnedIsNotNull(clientId);
        return sessionLogs.stream().mapToInt(WorkoutSessionLog::getCaloriesBurned).sum();
    }

    // Get total workout time for client
    public Integer getTotalWorkoutTimeForClient(Long clientId) {
        logger.info("Calculating total workout time for client: {}", clientId);
        
        List<WorkoutSessionLog> sessionLogs = sessionLogRepository.findByClient_IdAndTotalDurationIsNotNull(clientId);
        return sessionLogs.stream().mapToInt(WorkoutSessionLog::getTotalDuration).sum();
    }

    // Convert entity to DTO
    private WorkoutSessionLogDTO convertToDTO(WorkoutSessionLog sessionLog) {
        WorkoutSessionLogDTO dto = new WorkoutSessionLogDTO();
        dto.setId(sessionLog.getId());
        dto.setClientId(sessionLog.getClientId());
        dto.setWorkoutPlanId(sessionLog.getWorkoutPlanId());
        dto.setSessionDate(sessionLog.getSessionDate());
        dto.setStartTime(sessionLog.getStartTime());
        dto.setEndTime(sessionLog.getEndTime());
        dto.setTotalDuration(sessionLog.getTotalDuration());
        dto.setExercisesCompleted(sessionLog.getExercisesCompleted());
        dto.setCaloriesBurned(sessionLog.getCaloriesBurned());
        dto.setNotes(sessionLog.getNotes());
        dto.setRating(sessionLog.getRating());
        dto.setDifficultyRating(sessionLog.getDifficultyRating());
        dto.setLoggedById(sessionLog.getLoggedById());
        dto.setCreatedAt(sessionLog.getCreatedAt());
        dto.setUpdatedAt(sessionLog.getUpdatedAt());
        
        // Set client name if available
        if (sessionLog.getClientId() != null) {
            Optional<User> clientOpt = userRepository.findById(sessionLog.getClientId());
            if (clientOpt.isPresent()) {
                dto.setClientName(clientOpt.get().getFirstName() + " " + clientOpt.get().getLastName());
            }
        }
        
        // Set workout plan name if available
        if (sessionLog.getWorkoutPlanId() != null) {
            Optional<WorkoutPlan> workoutPlanOpt = workoutPlanRepository.findById(sessionLog.getWorkoutPlanId());
            if (workoutPlanOpt.isPresent()) {
                dto.setWorkoutPlanName(workoutPlanOpt.get().getName());
            }
        }
        
        // Set logged by name if available
        if (sessionLog.getLoggedById() != null) {
            Optional<User> loggedByOpt = userRepository.findById(sessionLog.getLoggedById());
            if (loggedByOpt.isPresent()) {
                dto.setLoggedByName(loggedByOpt.get().getFirstName() + " " + loggedByOpt.get().getLastName());
            }
        }
        
        return dto;
    }
}
