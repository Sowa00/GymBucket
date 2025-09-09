package gym.backend.service;

import gym.backend.dto.MealLogDTO;
import gym.backend.dto.MealLogRequestDTO;
import gym.backend.model.MealLog;
import gym.backend.model.User;
import gym.backend.model.Meal;
import gym.backend.model.NutritionPlan;
import gym.backend.repository.MealLogRepository;
import gym.backend.repository.UserRepository;
import gym.backend.repository.MealRepository;
import gym.backend.repository.NutritionPlanRepository;
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
public class MealLogService {
    
    private static final Logger logger = LoggerFactory.getLogger(MealLogService.class);
    
    @Autowired
    private MealLogRepository mealLogRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MealRepository mealRepository;
    
    @Autowired
    private NutritionPlanRepository nutritionPlanRepository;

    // Get all meal logs with pagination
    public Page<MealLogDTO> getAllMealLogs(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all meal logs with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MealLog> mealLogs = mealLogRepository.findAll(pageable);
        
        return mealLogs.map(this::convertToDTO);
    }

    // Get all meal logs as list
    public List<MealLogDTO> getAllMealLogsList() {
        logger.info("Fetching all meal logs as list");
        List<MealLog> mealLogs = mealLogRepository.findAll();
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get meal log by ID
    public Optional<MealLogDTO> getMealLogById(Long id) {
        logger.info("Fetching meal log by ID: {}", id);
        return mealLogRepository.findById(id).map(this::convertToDTO);
    }

    // Create new meal log
    public MealLogDTO createMealLog(MealLogRequestDTO request) {
        logger.info("Creating new meal log for client: {} and meal: {}", 
                   request.getClientId(), request.getMealId());
        
        Optional<User> clientOpt = userRepository.findById(request.getClientId());
        if (clientOpt.isEmpty()) {
            throw new RuntimeException("Client not found with ID: " + request.getClientId());
        }
        
        Optional<Meal> mealOpt = mealRepository.findById(request.getMealId());
        if (mealOpt.isEmpty()) {
            throw new RuntimeException("Meal not found with ID: " + request.getMealId());
        }
        
        Optional<User> loggedByOpt = userRepository.findById(request.getLoggedById());
        if (loggedByOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + request.getLoggedById());
        }
        
        MealLog mealLog = new MealLog();
        mealLog.setClientId(request.getClientId());
        mealLog.setMealId(request.getMealId());
        mealLog.setNutritionPlanId(request.getNutritionPlanId());
        mealLog.setLogDate(request.getLogDate());
        mealLog.setMealTime(request.getMealTime());
        mealLog.setPortionSize(request.getPortionSize());
        mealLog.setActualCalories(request.getActualCalories());
        mealLog.setActualProtein(request.getActualProtein());
        mealLog.setActualCarbs(request.getActualCarbs());
        mealLog.setActualFat(request.getActualFat());
        mealLog.setNotes(request.getNotes());
        mealLog.setLoggedById(request.getLoggedById());
        mealLog.setCreatedAt(LocalDateTime.now());
        mealLog.setUpdatedAt(LocalDateTime.now());
        
        MealLog savedMealLog = mealLogRepository.save(mealLog);
        logger.info("Meal log created successfully with ID: {}", savedMealLog.getId());
        
        return convertToDTO(savedMealLog);
    }

    // Update meal log
    public MealLogDTO updateMealLog(Long id, MealLogRequestDTO request) {
        logger.info("Updating meal log: {}", id);
        
        Optional<MealLog> mealLogOpt = mealLogRepository.findById(id);
        if (mealLogOpt.isEmpty()) {
            throw new RuntimeException("Meal log not found with ID: " + id);
        }
        
        MealLog mealLog = mealLogOpt.get();
        mealLog.setLogDate(request.getLogDate());
        mealLog.setMealTime(request.getMealTime());
        mealLog.setPortionSize(request.getPortionSize());
        mealLog.setActualCalories(request.getActualCalories());
        mealLog.setActualProtein(request.getActualProtein());
        mealLog.setActualCarbs(request.getActualCarbs());
        mealLog.setActualFat(request.getActualFat());
        mealLog.setNotes(request.getNotes());
        mealLog.setLoggedById(request.getLoggedById());
        mealLog.setUpdatedAt(LocalDateTime.now());
        
        MealLog savedMealLog = mealLogRepository.save(mealLog);
        logger.info("Meal log updated successfully with ID: {}", savedMealLog.getId());
        
        return convertToDTO(savedMealLog);
    }

    // Delete meal log
    public void deleteMealLog(Long id) {
        logger.info("Deleting meal log: {}", id);
        
        Optional<MealLog> mealLogOpt = mealLogRepository.findById(id);
        if (mealLogOpt.isEmpty()) {
            throw new RuntimeException("Meal log not found with ID: " + id);
        }
        
        mealLogRepository.deleteById(id);
        logger.info("Meal log deleted successfully with ID: {}", id);
    }

    // Get meal logs by client
    public List<MealLogDTO> getMealLogsByClient(Long clientId) {
        logger.info("Fetching meal logs for client: {}", clientId);
        
        List<MealLog> mealLogs = mealLogRepository.findByClient_Id(clientId);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Additional methods needed by MealLogController
    public List<MealLogDTO> getRecentMealLogsByClient(Long clientId, int limit) {
        logger.info("Fetching recent meal logs for client: {} with limit: {}", clientId, limit);
        
        Pageable pageable = PageRequest.of(0, limit, Sort.by("logDate").descending());
        Page<MealLog> mealLogs = mealLogRepository.findByClient_Id(clientId, pageable);
        return mealLogs.getContent().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Map<String, Object> getMealLogStatsByClient(Long clientId) {
        logger.info("Fetching meal log stats for client: {}", clientId);
        
        List<MealLog> mealLogs = mealLogRepository.findByClient_Id(clientId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLogs", mealLogs.size());
        stats.put("totalCalories", mealLogs.stream().mapToDouble(ml -> ml.getActualCalories() != null ? ml.getActualCalories() : 0).sum());
        stats.put("totalProtein", mealLogs.stream().mapToDouble(ml -> ml.getActualProtein() != null ? ml.getActualProtein() : 0).sum());
        stats.put("totalCarbs", mealLogs.stream().mapToDouble(ml -> ml.getActualCarbs() != null ? ml.getActualCarbs() : 0).sum());
        stats.put("totalFat", mealLogs.stream().mapToDouble(ml -> ml.getActualFat() != null ? ml.getActualFat() : 0).sum());
        
        return stats;
    }

    public List<MealLogDTO> getMealLogsByClientAndDate(Long clientId, String date) {
        logger.info("Fetching meal logs for client: {} on date: {}", clientId, date);
        
        LocalDate logDate = LocalDate.parse(date);
        List<MealLog> mealLogs = mealLogRepository.findByClient_IdAndLogDateBetween(clientId, logDate, logDate);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get meal logs by client with pagination
    public Page<MealLogDTO> getMealLogsByClient(Long clientId, int page, int size) {
        logger.info("Fetching meal logs for client: {} with pagination", clientId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("logDate").descending());
        Page<MealLog> mealLogs = mealLogRepository.findByClient_Id(clientId, pageable);
        
        return mealLogs.map(this::convertToDTO);
    }

    // Get meal logs by meal
    public List<MealLogDTO> getMealLogsByMeal(Long mealId) {
        logger.info("Fetching meal logs for meal: {}", mealId);
        
        List<MealLog> mealLogs = mealLogRepository.findByMeal_Id(mealId);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get meal logs by nutrition plan
    public List<MealLogDTO> getMealLogsByNutritionPlan(Long nutritionPlanId) {
        logger.info("Fetching meal logs for nutrition plan: {}", nutritionPlanId);
        
        List<MealLog> mealLogs = mealLogRepository.findByNutritionPlan_Id(nutritionPlanId);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get meal logs by logged by user
    public List<MealLogDTO> getMealLogsByLoggedBy(Long loggedById) {
        logger.info("Fetching meal logs logged by user: {}", loggedById);
        
        List<MealLog> mealLogs = mealLogRepository.findByLoggedBy_Id(loggedById);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get meal logs by date range
    public List<MealLogDTO> getMealLogsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching meal logs from {} to {}", startDate, endDate);
        
        List<MealLog> mealLogs = mealLogRepository.findByLogDateBetween(startDate, endDate);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get meal logs by client and date range
    public List<MealLogDTO> getMealLogsByClientAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching meal logs for client: {} from {} to {}", clientId, startDate, endDate);
        
        List<MealLog> mealLogs = mealLogRepository.findByClient_IdAndLogDateBetween(clientId, startDate, endDate);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent meal logs
    public List<MealLogDTO> getRecentMealLogs(int days) {
        logger.info("Fetching recent meal logs from last {} days", days);
        
        LocalDate since = LocalDate.now().minusDays(days);
        List<MealLog> mealLogs = mealLogRepository.findByLogDateAfter(since);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get today's meal logs
    public List<MealLogDTO> getTodaysMealLogs() {
        logger.info("Fetching today's meal logs");
        
        LocalDate today = LocalDate.now();
        List<MealLog> mealLogs = mealLogRepository.findByLogDate(today);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this week's meal logs
    public List<MealLogDTO> getThisWeeksMealLogs() {
        logger.info("Fetching this week's meal logs");
        
        LocalDate startOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        List<MealLog> mealLogs = mealLogRepository.findByLogDateBetween(startOfWeek, endOfWeek);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this month's meal logs
    public List<MealLogDTO> getThisMonthsMealLogs() {
        logger.info("Fetching this month's meal logs");
        
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<MealLog> mealLogs = mealLogRepository.findByLogDateBetween(startOfMonth, endOfMonth);
        return mealLogs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    // Get total calories consumed for client
    public Double getTotalCaloriesConsumedForClient(Long clientId) {
        logger.info("Calculating total calories consumed for client: {}", clientId);
        
        List<MealLog> mealLogs = mealLogRepository.findByClient_IdAndActualCaloriesIsNotNull(clientId);
        return mealLogs.stream().mapToDouble(MealLog::getActualCalories).sum();
    }

    // Get total protein consumed for client
    public Double getTotalProteinConsumedForClient(Long clientId) {
        logger.info("Calculating total protein consumed for client: {}", clientId);
        
        List<MealLog> mealLogs = mealLogRepository.findByClient_IdAndActualProteinIsNotNull(clientId);
        return mealLogs.stream().mapToDouble(MealLog::getActualProtein).sum();
    }

    // Get total carbs consumed for client
    public Double getTotalCarbsConsumedForClient(Long clientId) {
        logger.info("Calculating total carbs consumed for client: {}", clientId);
        
        List<MealLog> mealLogs = mealLogRepository.findByClient_IdAndActualCarbsIsNotNull(clientId);
        return mealLogs.stream().mapToDouble(MealLog::getActualCarbs).sum();
    }

    // Get total fat consumed for client
    public Double getTotalFatConsumedForClient(Long clientId) {
        logger.info("Calculating total fat consumed for client: {}", clientId);
        
        List<MealLog> mealLogs = mealLogRepository.findByClient_IdAndActualFatIsNotNull(clientId);
        return mealLogs.stream().mapToDouble(MealLog::getActualFat).sum();
    }

    // Convert entity to DTO
    private MealLogDTO convertToDTO(MealLog mealLog) {
        MealLogDTO dto = new MealLogDTO();
        dto.setId(mealLog.getId());
        dto.setClientId(mealLog.getClientId());
        dto.setMealId(mealLog.getMealId());
        dto.setNutritionPlanId(mealLog.getNutritionPlanId());
        dto.setLogDate(mealLog.getLogDate());
        dto.setMealTime(mealLog.getMealTime());
        dto.setPortionSize(mealLog.getPortionSize());
        dto.setActualCalories(mealLog.getActualCalories());
        dto.setActualProtein(mealLog.getActualProtein());
        dto.setActualCarbs(mealLog.getActualCarbs());
        dto.setActualFat(mealLog.getActualFat());
        dto.setNotes(mealLog.getNotes());
        dto.setLoggedById(mealLog.getLoggedById());
        dto.setCreatedAt(mealLog.getCreatedAt());
        dto.setUpdatedAt(mealLog.getUpdatedAt());
        
        // Set client name if available
        if (mealLog.getClientId() != null) {
            Optional<User> clientOpt = userRepository.findById(mealLog.getClientId());
            if (clientOpt.isPresent()) {
                dto.setClientName(clientOpt.get().getFirstName() + " " + clientOpt.get().getLastName());
            }
        }
        
        // Set meal name if available
        if (mealLog.getMealId() != null) {
            Optional<Meal> mealOpt = mealRepository.findById(mealLog.getMealId());
            if (mealOpt.isPresent()) {
                dto.setMealName(mealOpt.get().getName());
            }
        }
        
        // Set nutrition plan name if available
        if (mealLog.getNutritionPlanId() != null) {
            Optional<NutritionPlan> nutritionPlanOpt = nutritionPlanRepository.findById(mealLog.getNutritionPlanId());
            if (nutritionPlanOpt.isPresent()) {
                dto.setNutritionPlanName(nutritionPlanOpt.get().getName());
            }
        }
        
        // Set logged by name if available
        if (mealLog.getLoggedById() != null) {
            Optional<User> loggedByOpt = userRepository.findById(mealLog.getLoggedById());
            if (loggedByOpt.isPresent()) {
                dto.setLoggedByName(loggedByOpt.get().getFirstName() + " " + loggedByOpt.get().getLastName());
            }
        }
        
        return dto;
    }
}
