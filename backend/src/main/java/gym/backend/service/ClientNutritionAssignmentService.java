                                        package gym.backend.service;

import gym.backend.dto.ClientNutritionAssignmentDTO;
import gym.backend.dto.ClientNutritionAssignmentRequestDTO;
import gym.backend.model.ClientNutritionAssignment;
import gym.backend.model.User;
import gym.backend.model.NutritionPlan;
import gym.backend.repository.ClientNutritionAssignmentRepository;
import gym.backend.repository.UserRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientNutritionAssignmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientNutritionAssignmentService.class);
    
    @Autowired
    private ClientNutritionAssignmentRepository assignmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NutritionPlanRepository nutritionPlanRepository;

    // Get all nutrition assignments with pagination
    public Page<ClientNutritionAssignmentDTO> getAllNutritionAssignments(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all nutrition assignments with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClientNutritionAssignment> assignments = assignmentRepository.findAll(pageable);
        
        return assignments.map(this::convertToDTO);
    }

    // Get all nutrition assignments as list
    public List<ClientNutritionAssignmentDTO> getAllNutritionAssignmentsList() {
        logger.info("Fetching all nutrition assignments as list");
        List<ClientNutritionAssignment> assignments = assignmentRepository.findAll();
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get nutrition assignment by ID
    public Optional<ClientNutritionAssignmentDTO> getNutritionAssignmentById(Long id) {
        logger.info("Fetching nutrition assignment by ID: {}", id);
        return assignmentRepository.findById(id).map(this::convertToDTO);
    }

    // Create new nutrition assignment
    public ClientNutritionAssignmentDTO createNutritionAssignment(ClientNutritionAssignmentRequestDTO request) {
        logger.info("Creating new nutrition assignment for client: {} and nutrition plan: {}", 
                   request.getClientId(), request.getNutritionPlanId());
        
        Optional<User> clientOpt = userRepository.findById(request.getClientId());
        if (clientOpt.isEmpty()) {
            throw new RuntimeException("Client not found with ID: " + request.getClientId());
        }
        
        Optional<NutritionPlan> nutritionPlanOpt = nutritionPlanRepository.findById(request.getNutritionPlanId());
        if (nutritionPlanOpt.isEmpty()) {
            throw new RuntimeException("Nutrition plan not found with ID: " + request.getNutritionPlanId());
        }
        
        Optional<User> assignedByOpt = userRepository.findById(request.getAssignedById());
        if (assignedByOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + request.getAssignedById());
        }
        
        ClientNutritionAssignment assignment = new ClientNutritionAssignment();
        assignment.setClientId(request.getClientId());
        assignment.setNutritionPlanId(request.getNutritionPlanId());
        assignment.setAssignedById(request.getAssignedById());
        assignment.setAssignedDate(LocalDate.now());
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        assignment.setStatus(request.getStatus());
        assignment.setNotes(request.getNotes());
        assignment.setProgressNotes(request.getProgressNotes());
        assignment.setCompletionPercentage(request.getCompletionPercentage());
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setUpdatedAt(LocalDateTime.now());
        
        ClientNutritionAssignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Nutrition assignment created successfully with ID: {}", savedAssignment.getId());
        
        return convertToDTO(savedAssignment);
    }

    // Update nutrition assignment
    public ClientNutritionAssignmentDTO updateNutritionAssignment(Long id, ClientNutritionAssignmentRequestDTO request) {
        logger.info("Updating nutrition assignment: {}", id);
        
        Optional<ClientNutritionAssignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Nutrition assignment not found with ID: " + id);
        }
        
        ClientNutritionAssignment assignment = assignmentOpt.get();
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        assignment.setStatus(request.getStatus());
        assignment.setNotes(request.getNotes());
        assignment.setProgressNotes(request.getProgressNotes());
        assignment.setCompletionPercentage(request.getCompletionPercentage());
        assignment.setUpdatedAt(LocalDateTime.now());
        
        ClientNutritionAssignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Nutrition assignment updated successfully with ID: {}", savedAssignment.getId());
        
        return convertToDTO(savedAssignment);
    }

    // Delete nutrition assignment
    public void deleteNutritionAssignment(Long id) {
        logger.info("Deleting nutrition assignment: {}", id);
        
        Optional<ClientNutritionAssignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Nutrition assignment not found with ID: " + id);
        }
        
        assignmentRepository.deleteById(id);
        logger.info("Nutrition assignment deleted successfully with ID: {}", id);
    }

    // Get nutrition assignments by client
    public List<ClientNutritionAssignmentDTO> getNutritionAssignmentsByClient(Long clientId) {
        logger.info("Fetching nutrition assignments for client: {}", clientId);
        
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByClient_Id(clientId);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get active nutrition assignments by client
    public List<ClientNutritionAssignmentDTO> getActiveNutritionAssignmentsByClient(Long clientId) {
        logger.info("Fetching active nutrition assignments for client: {}", clientId);
        
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByClient_IdAndStatus(clientId, ClientNutritionAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get nutrition assignments by nutrition plan
    public List<ClientNutritionAssignmentDTO> getNutritionAssignmentsByNutritionPlan(Long nutritionPlanId) {
        logger.info("Fetching nutrition assignments for nutrition plan: {}", nutritionPlanId);
        
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByNutritionPlan_Id(nutritionPlanId);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get nutrition assignments by assigned by user
    public List<ClientNutritionAssignmentDTO> getNutritionAssignmentsByAssignedBy(Long assignedById) {
        logger.info("Fetching nutrition assignments assigned by user: {}", assignedById);
        
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByAssignedBy_Id(assignedById);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get nutrition assignments by status
    public List<ClientNutritionAssignmentDTO> getNutritionAssignmentsByStatus(ClientNutritionAssignment.AssignmentStatus status) {
        logger.info("Fetching nutrition assignments by status: {}", status);
        
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByStatus(status);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get nutrition assignments by date range
    public List<ClientNutritionAssignmentDTO> getNutritionAssignmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching nutrition assignments from {} to {}", startDate, endDate);
        
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByStartDateBetween(startDate, endDate);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get current nutrition assignments
    public List<ClientNutritionAssignmentDTO> getCurrentNutritionAssignments() {
        logger.info("Fetching current nutrition assignments");
        
        LocalDate today = LocalDate.now();
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndStatus(
            today, today, ClientNutritionAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get overdue nutrition assignments
    public List<ClientNutritionAssignmentDTO> getOverdueNutritionAssignments() {
        logger.info("Fetching overdue nutrition assignments");
        
        LocalDate today = LocalDate.now();
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByEndDateBeforeAndStatus(today, ClientNutritionAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get upcoming nutrition assignments
    public List<ClientNutritionAssignmentDTO> getUpcomingNutritionAssignments(int days) {
        logger.info("Fetching upcoming nutrition assignments for next {} days", days);
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByStartDateBetweenAndStatus(startDate, endDate, ClientNutritionAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Update assignment status
    public ClientNutritionAssignmentDTO updateAssignmentStatus(Long id, ClientNutritionAssignment.AssignmentStatus status) {
        logger.info("Updating assignment status to {} for assignment: {}", status, id);
        
        Optional<ClientNutritionAssignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Nutrition assignment not found with ID: " + id);
        }
        
        ClientNutritionAssignment assignment = assignmentOpt.get();
        assignment.setStatus(status);
        assignment.setUpdatedAt(LocalDateTime.now());
        
        ClientNutritionAssignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Assignment status updated successfully for ID: {}", savedAssignment.getId());
        
        return convertToDTO(savedAssignment);
    }

    // Update assignment progress
    public ClientNutritionAssignmentDTO updateAssignmentProgress(Long id, Integer completionPercentage, String progressNotes) {
        logger.info("Updating assignment progress to {}% for assignment: {}", completionPercentage, id);
        
        Optional<ClientNutritionAssignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Nutrition assignment not found with ID: " + id);
        }
        
        ClientNutritionAssignment assignment = assignmentOpt.get();
        assignment.setCompletionPercentage(completionPercentage);
        assignment.setProgressNotes(progressNotes);
        assignment.setUpdatedAt(LocalDateTime.now());
        
        ClientNutritionAssignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Assignment progress updated successfully for ID: {}", savedAssignment.getId());
        
        return convertToDTO(savedAssignment);
    }

    // Convert entity to DTO
    private ClientNutritionAssignmentDTO convertToDTO(ClientNutritionAssignment assignment) {
        ClientNutritionAssignmentDTO dto = new ClientNutritionAssignmentDTO();
        dto.setId(assignment.getId());
        dto.setClientId(assignment.getClientId());
        dto.setNutritionPlanId(assignment.getNutritionPlanId());
        dto.setAssignedById(assignment.getAssignedById());
        dto.setAssignedDate(assignment.getAssignedDate());
        dto.setStartDate(assignment.getStartDate());
        dto.setEndDate(assignment.getEndDate());
        dto.setStatus(assignment.getStatus());
        dto.setNotes(assignment.getNotes());
        dto.setProgressNotes(assignment.getProgressNotes());
        dto.setCompletionPercentage(assignment.getCompletionPercentage());
        dto.setLastMealLoggedDate(assignment.getLastMealLoggedDate());
        dto.setTotalMealsLogged(assignment.getTotalMealsLogged());
        dto.setCreatedAt(assignment.getCreatedAt());
        dto.setUpdatedAt(assignment.getUpdatedAt());
        
        // Set client name if available
        if (assignment.getClientId() != null) {
            Optional<User> clientOpt = userRepository.findById(assignment.getClientId());
            if (clientOpt.isPresent()) {
                dto.setClientName(clientOpt.get().getFirstName() + " " + clientOpt.get().getLastName());
            }
        }
        
        // Set nutrition plan name if available
        if (assignment.getNutritionPlanId() != null) {
            Optional<NutritionPlan> nutritionPlanOpt = nutritionPlanRepository.findById(assignment.getNutritionPlanId());
            if (nutritionPlanOpt.isPresent()) {
                dto.setNutritionPlanName(nutritionPlanOpt.get().getName());
            }
        }
        
        // Set assigned by name if available
        if (assignment.getAssignedById() != null) {
            Optional<User> assignedByOpt = userRepository.findById(assignment.getAssignedById());
            if (assignedByOpt.isPresent()) {
                dto.setAssignedByName(assignedByOpt.get().getFirstName() + " " + assignedByOpt.get().getLastName());
            }
        }
        
        return dto;
    }

    // Additional methods needed by ClientNutritionAssignmentController
    public List<ClientNutritionAssignmentDTO> getActiveNutritionAssignments() {
        logger.info("Fetching active nutrition assignments");
        
        List<ClientNutritionAssignment> assignments = assignmentRepository.findByStatus(ClientNutritionAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public boolean activateNutritionAssignment(Long assignmentId) {
        logger.info("Activating nutrition assignment with ID: {}", assignmentId);
        
        return assignmentRepository.findById(assignmentId)
                .map(assignment -> {
                    assignment.setStatus(ClientNutritionAssignment.AssignmentStatus.ACTIVE);
                    assignmentRepository.save(assignment);
                    logger.info("Nutrition assignment activated successfully with ID: {}", assignmentId);
                    return true;
                })
                .orElse(false);
    }

    public boolean deactivateNutritionAssignment(Long assignmentId) {
        logger.info("Deactivating nutrition assignment with ID: {}", assignmentId);
        
        return assignmentRepository.findById(assignmentId)
                .map(assignment -> {
                    assignment.setStatus(ClientNutritionAssignment.AssignmentStatus.PAUSED);
                    assignmentRepository.save(assignment);
                    logger.info("Nutrition assignment deactivated successfully with ID: {}", assignmentId);
                    return true;
                })
                .orElse(false);
    }
}
