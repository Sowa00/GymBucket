package gym.backend.service;

import gym.backend.dto.ClientGoalDTO;
import gym.backend.dto.ClientGoalRequestDTO;
import gym.backend.model.ClientGoal;
import gym.backend.model.User;
import gym.backend.repository.ClientGoalRepository;
import gym.backend.repository.UserRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientGoalService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientGoalService.class);
    
    @Autowired
    private ClientGoalRepository goalRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Get all client goals with pagination
    public Page<ClientGoalDTO> getAllClientGoals(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all client goals with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClientGoal> goals = goalRepository.findAll(pageable);
        
        return goals.map(this::convertToDTO);
    }

    // Get all client goals as list
    public List<ClientGoalDTO> getAllClientGoalsList() {
        logger.info("Fetching all client goals as list");
        List<ClientGoal> goals = goalRepository.findAll();
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get client goal by ID
    public Optional<ClientGoalDTO> getClientGoalById(Long id) {
        logger.info("Fetching client goal by ID: {}", id);
        return goalRepository.findById(id).map(this::convertToDTO);
    }

    // Create new client goal
    public ClientGoalDTO createClientGoal(ClientGoalRequestDTO request) {
        logger.info("Creating new client goal: {} for client: {}", request.getTitle(), request.getClientId());
        
        Optional<User> clientOpt = userRepository.findById(request.getClientId());
        if (clientOpt.isEmpty()) {
            throw new RuntimeException("Client not found with ID: " + request.getClientId());
        }
        
        Optional<User> assignedByOpt = userRepository.findById(request.getAssignedById());
        if (assignedByOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + request.getAssignedById());
        }
        
        ClientGoal goal = new ClientGoal();
        goal.setClientId(request.getClientId());
        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setGoalType(request.getGoalType());
        goal.setCategory(request.getCategory());
        goal.setStatus(request.getStatus());
        goal.setStartDate(request.getStartDate());
        goal.setTargetDate(request.getTargetDate());
        goal.setTargetValue(request.getTargetValue());
        goal.setUnit(request.getUnit());
        goal.setProgressNotes(request.getProgressNotes());
        goal.setAssignedById(request.getAssignedById());
        // Convert Integer priority to Priority enum
        if (request.getPriority() != null) {
            goal.setPriority(ClientGoal.Priority.values()[request.getPriority()]);
        }
        goal.setIsPublic(request.getIsPublic());
        goal.setCreatedAt(LocalDateTime.now());
        goal.setUpdatedAt(LocalDateTime.now());
        
        ClientGoal savedGoal = goalRepository.save(goal);
        logger.info("Client goal created successfully with ID: {}", savedGoal.getId());
        
        return convertToDTO(savedGoal);
    }

    // Update client goal
    public ClientGoalDTO updateClientGoal(Long id, ClientGoalRequestDTO request) {
        logger.info("Updating client goal: {}", id);
        
        Optional<ClientGoal> goalOpt = goalRepository.findById(id);
        if (goalOpt.isEmpty()) {
            throw new RuntimeException("Client goal not found with ID: " + id);
        }
        
        ClientGoal goal = goalOpt.get();
        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setGoalType(request.getGoalType());
        goal.setCategory(request.getCategory());
        goal.setStatus(request.getStatus());
        goal.setStartDate(request.getStartDate());
        goal.setTargetDate(request.getTargetDate());
        goal.setTargetValue(request.getTargetValue());
        goal.setUnit(request.getUnit());
        goal.setProgressNotes(request.getProgressNotes());
        goal.setAssignedById(request.getAssignedById());
        // Convert Integer priority to Priority enum
        if (request.getPriority() != null) {
            goal.setPriority(ClientGoal.Priority.values()[request.getPriority()]);
        }
        goal.setIsPublic(request.getIsPublic());
        goal.setUpdatedAt(LocalDateTime.now());
        
        ClientGoal savedGoal = goalRepository.save(goal);
        logger.info("Client goal updated successfully with ID: {}", savedGoal.getId());
        
        return convertToDTO(savedGoal);
    }

    // Delete client goal
    public void deleteClientGoal(Long id) {
        logger.info("Deleting client goal: {}", id);
        
        Optional<ClientGoal> goalOpt = goalRepository.findById(id);
        if (goalOpt.isEmpty()) {
            throw new RuntimeException("Client goal not found with ID: " + id);
        }
        
        goalRepository.deleteById(id);
        logger.info("Client goal deleted successfully with ID: {}", id);
    }

    // Get client goals by client
    public List<ClientGoalDTO> getClientGoalsByClient(Long clientId) {
        logger.info("Fetching client goals for client: {}", clientId);
        
        List<ClientGoal> goals = goalRepository.findByClient_Id(clientId);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get client goals by client with pagination
    public Page<ClientGoalDTO> getClientGoalsByClient(Long clientId, int page, int size) {
        logger.info("Fetching client goals for client: {} with pagination", clientId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("targetDate").ascending());
        Page<ClientGoal> goals = goalRepository.findByClient_Id(clientId, pageable);
        
        return goals.map(this::convertToDTO);
    }

    // Get active client goals by client
    public List<ClientGoalDTO> getActiveClientGoalsByClient(Long clientId) {
        logger.info("Fetching active client goals for client: {}", clientId);
        
        List<ClientGoal> goals = goalRepository.findByClient_IdAndStatus(clientId, ClientGoal.GoalStatus.ACTIVE);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get completed client goals by client
    public List<ClientGoalDTO> getCompletedClientGoalsByClient(Long clientId) {
        logger.info("Fetching completed client goals for client: {}", clientId);
        
        List<ClientGoal> goals = goalRepository.findByClient_IdAndStatus(clientId, ClientGoal.GoalStatus.COMPLETED);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get client goals by goal type
    public List<ClientGoalDTO> getClientGoalsByGoalType(ClientGoal.GoalType goalType) {
        logger.info("Fetching client goals by goal type: {}", goalType);
        
        List<ClientGoal> goals = goalRepository.findByGoalType(goalType);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get client goals by category
    public List<ClientGoalDTO> getClientGoalsByCategory(ClientGoal.GoalCategory category) {
        logger.info("Fetching client goals by category: {}", category);
        
        List<ClientGoal> goals = goalRepository.findByCategory(category);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get client goals by status
    public List<ClientGoalDTO> getClientGoalsByStatus(ClientGoal.GoalStatus status) {
        logger.info("Fetching client goals by status: {}", status);
        
        List<ClientGoal> goals = goalRepository.findByStatus(status);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get client goals by assigned by user
    public List<ClientGoalDTO> getClientGoalsByAssignedBy(Long assignedById) {
        logger.info("Fetching client goals assigned by user: {}", assignedById);
        
        List<ClientGoal> goals = goalRepository.findByAssignedBy_Id(assignedById);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get client goals by priority
    public List<ClientGoalDTO> getClientGoalsByPriority(Integer priority) {
        logger.info("Fetching client goals by priority: {}", priority);
        
        // Convert Integer priority to Priority enum
        ClientGoal.Priority priorityEnum = null;
        if (priority != null) {
            priorityEnum = ClientGoal.Priority.values()[priority];
        }
        
        List<ClientGoal> goals = goalRepository.findByPriority(priorityEnum);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get public client goals
    public List<ClientGoalDTO> getPublicClientGoals() {
        logger.info("Fetching public client goals");
        
        List<ClientGoal> goals = goalRepository.findByIsPublicTrue();
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get private client goals
    public List<ClientGoalDTO> getPrivateClientGoals() {
        logger.info("Fetching private client goals");
        
        List<ClientGoal> goals = goalRepository.findByIsPublicFalse();
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get overdue client goals
    public List<ClientGoalDTO> getOverdueClientGoals() {
        logger.info("Fetching overdue client goals");
        
        LocalDate today = LocalDate.now();
        List<ClientGoal> goals = goalRepository.findByTargetDateBeforeAndStatus(today, ClientGoal.GoalStatus.ACTIVE);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get upcoming client goals
    public List<ClientGoalDTO> getUpcomingClientGoals(int days) {
        logger.info("Fetching upcoming client goals for next {} days", days);
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        List<ClientGoal> goals = goalRepository.findByTargetDateBetweenAndStatus(startDate, endDate, ClientGoal.GoalStatus.ACTIVE);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent client goals
    public List<ClientGoalDTO> getRecentClientGoals(int days) {
        logger.info("Fetching recent client goals from last {} days", days);
        
        LocalDate since = LocalDate.now().minusDays(days);
        List<ClientGoal> goals = goalRepository.findByStartDateAfter(since);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this week's client goals
    public List<ClientGoalDTO> getThisWeeksClientGoals() {
        logger.info("Fetching this week's client goals");
        
        LocalDate startOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        List<ClientGoal> goals = goalRepository.findByStartDateBetween(startOfWeek, endOfWeek);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this month's client goals
    public List<ClientGoalDTO> getThisMonthsClientGoals() {
        logger.info("Fetching this month's client goals");
        
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<ClientGoal> goals = goalRepository.findByStartDateBetween(startOfMonth, endOfMonth);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Update goal status
    public ClientGoalDTO updateGoalStatus(Long id, ClientGoal.GoalStatus status) {
        logger.info("Updating goal status to {} for goal: {}", status, id);
        
        Optional<ClientGoal> goalOpt = goalRepository.findById(id);
        if (goalOpt.isEmpty()) {
            throw new RuntimeException("Client goal not found with ID: " + id);
        }
        
        ClientGoal goal = goalOpt.get();
        goal.setStatus(status);
        
        if (status == ClientGoal.GoalStatus.COMPLETED) {
            goal.setCompletedDate(LocalDate.now());
        }
        
        goal.setUpdatedAt(LocalDateTime.now());
        
        ClientGoal savedGoal = goalRepository.save(goal);
        logger.info("Goal status updated successfully for ID: {}", savedGoal.getId());
        
        return convertToDTO(savedGoal);
    }

    // Update goal progress
    public ClientGoalDTO updateGoalProgress(Long id, Double currentValue, String progressNotes) {
        logger.info("Updating goal progress to {} for goal: {}", currentValue, id);
        
        Optional<ClientGoal> goalOpt = goalRepository.findById(id);
        if (goalOpt.isEmpty()) {
            throw new RuntimeException("Client goal not found with ID: " + id);
        }
        
        ClientGoal goal = goalOpt.get();
        goal.setCurrentValue(currentValue);
        goal.setProgressNotes(progressNotes);
        goal.setUpdatedAt(LocalDateTime.now());
        
        ClientGoal savedGoal = goalRepository.save(goal);
        logger.info("Goal progress updated successfully for ID: {}", savedGoal.getId());
        
        return convertToDTO(savedGoal);
    }

    // Convert entity to DTO
    private ClientGoalDTO convertToDTO(ClientGoal goal) {
        ClientGoalDTO dto = new ClientGoalDTO();
        dto.setId(goal.getId());
        dto.setClientId(goal.getClientId());
        dto.setTitle(goal.getTitle());
        dto.setDescription(goal.getDescription());
        dto.setGoalType(goal.getGoalType());
        dto.setCategory(goal.getCategory());
        dto.setStatus(goal.getStatus());
        dto.setStartDate(goal.getStartDate());
        dto.setTargetDate(goal.getTargetDate());
        dto.setCompletedDate(goal.getCompletedAt() != null ? goal.getCompletedAt().toLocalDate() : null);
        dto.setTargetValue(goal.getTargetValue());
        dto.setCurrentValue(goal.getCurrentValue());
        dto.setUnit(goal.getUnit());
        dto.setProgressNotes(goal.getProgressNotes());
        dto.setAssignedById(goal.getAssignedById());
        // Convert Priority enum to Integer
        dto.setPriority(goal.getPriority() != null ? goal.getPriority().ordinal() : null);
        dto.setIsPublic(goal.getIsPublic());
        dto.setCreatedAt(goal.getCreatedAt());
        dto.setUpdatedAt(goal.getUpdatedAt());
        
        // Set client name if available
        if (goal.getClientId() != null) {
            Optional<User> clientOpt = userRepository.findById(goal.getClientId());
            if (clientOpt.isPresent()) {
                dto.setClientName(clientOpt.get().getFirstName() + " " + clientOpt.get().getLastName());
            }
        }
        
        // Set assigned by name if available
        if (goal.getAssignedById() != null) {
            Optional<User> assignedByOpt = userRepository.findById(goal.getAssignedById());
            if (assignedByOpt.isPresent()) {
                dto.setAssignedByName(assignedByOpt.get().getFirstName() + " " + assignedByOpt.get().getLastName());
            }
        }
        
        return dto;
    }

    // Additional methods needed by ClientGoalController
    public List<ClientGoalDTO> getActiveClientGoals() {
        logger.info("Fetching active client goals");
        
        List<ClientGoal> goals = goalRepository.findByStatus(ClientGoal.GoalStatus.ACTIVE);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ClientGoalDTO> getCompletedClientGoals() {
        logger.info("Fetching completed client goals");
        
        List<ClientGoal> goals = goalRepository.findByStatus(ClientGoal.GoalStatus.COMPLETED);
        return goals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public boolean completeClientGoal(Long goalId) {
        logger.info("Completing client goal with ID: {}", goalId);
        
        return goalRepository.findById(goalId)
                .map(goal -> {
                    goal.setStatus(ClientGoal.GoalStatus.COMPLETED);
                    goal.setCompletedAt(LocalDateTime.now());
                    goalRepository.save(goal);
                    logger.info("Client goal completed successfully with ID: {}", goalId);
                    return true;
                })
                .orElse(false);
    }

    public boolean activateClientGoal(Long goalId) {
        logger.info("Activating client goal with ID: {}", goalId);
        
        return goalRepository.findById(goalId)
                .map(goal -> {
                    goal.setStatus(ClientGoal.GoalStatus.ACTIVE);
                    goalRepository.save(goal);
                    logger.info("Client goal activated successfully with ID: {}", goalId);
                    return true;
                })
                .orElse(false);
    }

    public boolean deactivateClientGoal(Long goalId) {
        logger.info("Deactivating client goal with ID: {}", goalId);
        
        return goalRepository.findById(goalId)
                .map(goal -> {
                    goal.setStatus(ClientGoal.GoalStatus.PAUSED);
                    goalRepository.save(goal);
                    logger.info("Client goal deactivated successfully with ID: {}", goalId);
                    return true;
                })
                .orElse(false);
    }
}
