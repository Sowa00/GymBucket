package gym.backend.service;

import gym.backend.dto.ClientWorkoutAssignmentDTO;
import gym.backend.dto.ClientWorkoutAssignmentRequestDTO;
import gym.backend.model.ClientWorkoutAssignment;
import gym.backend.model.User;
import gym.backend.model.WorkoutPlan;
import gym.backend.repository.ClientWorkoutAssignmentRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientWorkoutAssignmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientWorkoutAssignmentService.class);
    
    @Autowired
    private ClientWorkoutAssignmentRepository assignmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;

    // Get all workout assignments with pagination
    public Page<ClientWorkoutAssignmentDTO> getAllWorkoutAssignments(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all workout assignments with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClientWorkoutAssignment> assignments = assignmentRepository.findAll(pageable);
        
        return assignments.map(this::convertToDTO);
    }

    // Get all workout assignments as list
    public List<ClientWorkoutAssignmentDTO> getAllWorkoutAssignmentsList() {
        logger.info("Fetching all workout assignments as list");
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findAll();
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout assignment by ID
    public Optional<ClientWorkoutAssignmentDTO> getWorkoutAssignmentById(Long id) {
        logger.info("Fetching workout assignment by ID: {}", id);
        return assignmentRepository.findById(id).map(this::convertToDTO);
    }

    // Create new workout assignment
    public ClientWorkoutAssignmentDTO createWorkoutAssignment(ClientWorkoutAssignmentRequestDTO request) {
        logger.info("Creating new workout assignment for client: {} and workout plan: {}", 
                   request.getClientId(), request.getWorkoutPlanId());
        
        Optional<User> clientOpt = userRepository.findById(request.getClientId());
        if (clientOpt.isEmpty()) {
            throw new RuntimeException("Client not found with ID: " + request.getClientId());
        }
        
        Optional<WorkoutPlan> workoutPlanOpt = workoutPlanRepository.findById(request.getWorkoutPlanId());
        if (workoutPlanOpt.isEmpty()) {
            throw new RuntimeException("Workout plan not found with ID: " + request.getWorkoutPlanId());
        }
        
        Optional<User> assignedByOpt = userRepository.findById(request.getAssignedById());
        if (assignedByOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + request.getAssignedById());
        }
        
        ClientWorkoutAssignment assignment = new ClientWorkoutAssignment();
        assignment.setClientId(request.getClientId());
        assignment.setWorkoutPlanId(request.getWorkoutPlanId());
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
        
        ClientWorkoutAssignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Workout assignment created successfully with ID: {}", savedAssignment.getId());
        
        return convertToDTO(savedAssignment);
    }

    // Update workout assignment
    public ClientWorkoutAssignmentDTO updateWorkoutAssignment(Long id, ClientWorkoutAssignmentRequestDTO request) {
        logger.info("Updating workout assignment: {}", id);
        
        Optional<ClientWorkoutAssignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Workout assignment not found with ID: " + id);
        }
        
        ClientWorkoutAssignment assignment = assignmentOpt.get();
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        assignment.setStatus(request.getStatus());
        assignment.setNotes(request.getNotes());
        assignment.setProgressNotes(request.getProgressNotes());
        assignment.setCompletionPercentage(request.getCompletionPercentage());
        assignment.setUpdatedAt(LocalDateTime.now());
        
        ClientWorkoutAssignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Workout assignment updated successfully with ID: {}", savedAssignment.getId());
        
        return convertToDTO(savedAssignment);
    }

    // Delete workout assignment
    public void deleteWorkoutAssignment(Long id) {
        logger.info("Deleting workout assignment: {}", id);
        
        Optional<ClientWorkoutAssignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Workout assignment not found with ID: " + id);
        }
        
        assignmentRepository.deleteById(id);
        logger.info("Workout assignment deleted successfully with ID: {}", id);
    }

    // Get workout assignments by client
    public List<ClientWorkoutAssignmentDTO> getWorkoutAssignmentsByClient(Long clientId) {
        logger.info("Fetching workout assignments for client: {}", clientId);
        
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByClient_Id(clientId);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get active workout assignments by client
    public List<ClientWorkoutAssignmentDTO> getActiveWorkoutAssignmentsByClient(Long clientId) {
        logger.info("Fetching active workout assignments for client: {}", clientId);
        
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByClient_IdAndStatus(clientId, ClientWorkoutAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout assignments by workout plan
    public List<ClientWorkoutAssignmentDTO> getWorkoutAssignmentsByWorkoutPlan(Long workoutPlanId) {
        logger.info("Fetching workout assignments for workout plan: {}", workoutPlanId);
        
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByWorkoutPlan_Id(workoutPlanId);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout assignments by assigned by user
    public List<ClientWorkoutAssignmentDTO> getWorkoutAssignmentsByAssignedBy(Long assignedById) {
        logger.info("Fetching workout assignments assigned by user: {}", assignedById);
        
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByAssignedBy_Id(assignedById);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout assignments by status
    public List<ClientWorkoutAssignmentDTO> getWorkoutAssignmentsByStatus(ClientWorkoutAssignment.AssignmentStatus status) {
        logger.info("Fetching workout assignments by status: {}", status);
        
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByStatus(status);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get workout assignments by date range
    public List<ClientWorkoutAssignmentDTO> getWorkoutAssignmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching workout assignments from {} to {}", startDate, endDate);
        
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByStartDateBetween(startDate, endDate);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get current workout assignments
    public List<ClientWorkoutAssignmentDTO> getCurrentWorkoutAssignments() {
        logger.info("Fetching current workout assignments");
        
        LocalDate today = LocalDate.now();
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndStatus(
            today, today, ClientWorkoutAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get overdue workout assignments
    public List<ClientWorkoutAssignmentDTO> getOverdueWorkoutAssignments() {
        logger.info("Fetching overdue workout assignments");
        
        LocalDate today = LocalDate.now();
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByEndDateBeforeAndStatus(today, ClientWorkoutAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get upcoming workout assignments
    public List<ClientWorkoutAssignmentDTO> getUpcomingWorkoutAssignments(int days) {
        logger.info("Fetching upcoming workout assignments for next {} days", days);
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByStartDateBetweenAndStatus(startDate, endDate, ClientWorkoutAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Update assignment status
    public ClientWorkoutAssignmentDTO updateAssignmentStatus(Long id, ClientWorkoutAssignment.AssignmentStatus status) {
        logger.info("Updating assignment status to {} for assignment: {}", status, id);
        
        Optional<ClientWorkoutAssignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Workout assignment not found with ID: " + id);
        }
        
        ClientWorkoutAssignment assignment = assignmentOpt.get();
        assignment.setStatus(status);
        assignment.setUpdatedAt(LocalDateTime.now());
        
        ClientWorkoutAssignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Assignment status updated successfully for ID: {}", savedAssignment.getId());
        
        return convertToDTO(savedAssignment);
    }

    // Update assignment progress
    public ClientWorkoutAssignmentDTO updateAssignmentProgress(Long id, Integer completionPercentage, String progressNotes) {
        logger.info("Updating assignment progress to {}% for assignment: {}", completionPercentage, id);
        
        Optional<ClientWorkoutAssignment> assignmentOpt = assignmentRepository.findById(id);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Workout assignment not found with ID: " + id);
        }
        
        ClientWorkoutAssignment assignment = assignmentOpt.get();
        assignment.setCompletionPercentage(completionPercentage);
        assignment.setProgressNotes(progressNotes);
        assignment.setUpdatedAt(LocalDateTime.now());
        
        ClientWorkoutAssignment savedAssignment = assignmentRepository.save(assignment);
        logger.info("Assignment progress updated successfully for ID: {}", savedAssignment.getId());
        
        return convertToDTO(savedAssignment);
    }

    // Convert entity to DTO
    private ClientWorkoutAssignmentDTO convertToDTO(ClientWorkoutAssignment assignment) {
        ClientWorkoutAssignmentDTO dto = new ClientWorkoutAssignmentDTO();
        dto.setId(assignment.getId());
        dto.setClientId(assignment.getClientId());
        dto.setWorkoutPlanId(assignment.getWorkoutPlanId());
        dto.setAssignedById(assignment.getAssignedById());
        dto.setAssignedDate(assignment.getAssignedDate());
        dto.setStartDate(assignment.getStartDate());
        dto.setEndDate(assignment.getEndDate());
        dto.setStatus(assignment.getStatus());
        dto.setNotes(assignment.getNotes());
        dto.setProgressNotes(assignment.getProgressNotes());
        dto.setCompletionPercentage(assignment.getCompletionPercentage());
        dto.setLastWorkoutDate(assignment.getLastWorkoutDate());
        dto.setTotalWorkoutsCompleted(assignment.getTotalWorkoutsCompleted());
        dto.setCreatedAt(assignment.getCreatedAt());
        dto.setUpdatedAt(assignment.getUpdatedAt());
        
        // Set client name if available
        if (assignment.getClientId() != null) {
            Optional<User> clientOpt = userRepository.findById(assignment.getClientId());
            if (clientOpt.isPresent()) {
                dto.setClientName(clientOpt.get().getFirstName() + " " + clientOpt.get().getLastName());
            }
        }
        
        // Set workout plan name if available
        if (assignment.getWorkoutPlanId() != null) {
            Optional<WorkoutPlan> workoutPlanOpt = workoutPlanRepository.findById(assignment.getWorkoutPlanId());
            if (workoutPlanOpt.isPresent()) {
                dto.setWorkoutPlanName(workoutPlanOpt.get().getName());
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

    // Additional methods needed by ClientWorkoutAssignmentController
    public List<ClientWorkoutAssignmentDTO> getActiveWorkoutAssignments() {
        logger.info("Fetching active workout assignments");
        
        List<ClientWorkoutAssignment> assignments = assignmentRepository.findByStatus(ClientWorkoutAssignment.AssignmentStatus.ACTIVE);
        return assignments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public boolean activateWorkoutAssignment(Long assignmentId) {
        logger.info("Activating workout assignment with ID: {}", assignmentId);
        
        return assignmentRepository.findById(assignmentId)
                .map(assignment -> {
                    assignment.setStatus(ClientWorkoutAssignment.AssignmentStatus.ACTIVE);
                    assignmentRepository.save(assignment);
                    logger.info("Workout assignment activated successfully with ID: {}", assignmentId);
                    return true;
                })
                .orElse(false);
    }

    public boolean deactivateWorkoutAssignment(Long assignmentId) {
        logger.info("Deactivating workout assignment with ID: {}", assignmentId);
        
        return assignmentRepository.findById(assignmentId)
                .map(assignment -> {
                    assignment.setStatus(ClientWorkoutAssignment.AssignmentStatus.PAUSED);
                    assignmentRepository.save(assignment);
                    logger.info("Workout assignment deactivated successfully with ID: {}", assignmentId);
                    return true;
                })
                .orElse(false);
    }
}
