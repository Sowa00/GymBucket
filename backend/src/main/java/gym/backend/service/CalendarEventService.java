package gym.backend.service;

import gym.backend.dto.CalendarEventDTO;
import gym.backend.dto.CalendarEventRequestDTO;
import gym.backend.model.CalendarEvent;
import gym.backend.model.User;
import gym.backend.repository.CalendarEventRepository;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CalendarEventService {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarEventService.class);
    
    @Autowired
    private CalendarEventRepository calendarEventRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Get all calendar events with pagination
    public Page<CalendarEventDTO> getAllCalendarEvents(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all calendar events with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CalendarEvent> events = calendarEventRepository.findAll(pageable);
        
        return events.map(this::convertToDTO);
    }

    // Get all calendar events as list
    public List<CalendarEventDTO> getAllCalendarEventsList() {
        logger.info("Fetching all calendar events as list");
        List<CalendarEvent> events = calendarEventRepository.findAll();
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get calendar event by ID
    public Optional<CalendarEventDTO> getCalendarEventById(Long id) {
        logger.info("Fetching calendar event by ID: {}", id);
        return calendarEventRepository.findById(id).map(this::convertToDTO);
    }

    // Create new calendar event
    public CalendarEventDTO createCalendarEvent(CalendarEventRequestDTO request, Long userId) {
        logger.info("Creating new calendar event: {} for user: {}", request.getTitle(), userId);
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        CalendarEvent event = new CalendarEvent();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventType(request.getEventType());
        event.setStartDate(request.getStartDate());
        event.setStartTime(request.getStartTime());
        event.setEndDate(request.getEndDate());
        event.setEndTime(request.getEndTime());
        event.setLocation(request.getLocation());
        event.setStatus(request.getStatus());
        event.setPriority(request.getPriority());
        event.setIsRecurring(request.getIsRecurring());
        event.setRecurrencePattern(request.getRecurrencePattern());
        event.setRecurrenceEndDate(request.getRecurrenceEndDate());
        event.setClientId(request.getClientId());
        event.setTrainerId(request.getTrainerId());
        event.setWorkoutPlanId(request.getWorkoutPlanId());
        event.setNutritionPlanId(request.getNutritionPlanId());
        event.setNotes(request.getNotes());
        event.setReminderMinutes(request.getReminderMinutes());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        
        CalendarEvent savedEvent = calendarEventRepository.save(event);
        logger.info("Calendar event created successfully with ID: {}", savedEvent.getId());
        
        return convertToDTO(savedEvent);
    }

    // Update calendar event
    public CalendarEventDTO updateCalendarEvent(Long id, CalendarEventRequestDTO request, Long userId) {
        logger.info("Updating calendar event: {} for user: {}", id, userId);
        
        Optional<CalendarEvent> eventOpt = calendarEventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new RuntimeException("Calendar event not found with ID: " + id);
        }
        
        CalendarEvent event = eventOpt.get();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventType(request.getEventType());
        event.setStartDate(request.getStartDate());
        event.setStartTime(request.getStartTime());
        event.setEndDate(request.getEndDate());
        event.setEndTime(request.getEndTime());
        event.setLocation(request.getLocation());
        event.setStatus(request.getStatus());
        event.setPriority(request.getPriority());
        event.setIsRecurring(request.getIsRecurring());
        event.setRecurrencePattern(request.getRecurrencePattern());
        event.setRecurrenceEndDate(request.getRecurrenceEndDate());
        event.setClientId(request.getClientId());
        event.setTrainerId(request.getTrainerId());
        event.setWorkoutPlanId(request.getWorkoutPlanId());
        event.setNutritionPlanId(request.getNutritionPlanId());
        event.setNotes(request.getNotes());
        event.setReminderMinutes(request.getReminderMinutes());
        event.setUpdatedAt(LocalDateTime.now());
        
        CalendarEvent savedEvent = calendarEventRepository.save(event);
        logger.info("Calendar event updated successfully with ID: {}", savedEvent.getId());
        
        return convertToDTO(savedEvent);
    }

    // Delete calendar event
    public void deleteCalendarEvent(Long id, Long userId) {
        logger.info("Deleting calendar event: {} for user: {}", id, userId);
        
        Optional<CalendarEvent> eventOpt = calendarEventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            throw new RuntimeException("Calendar event not found with ID: " + id);
        }
        
        calendarEventRepository.deleteById(id);
        logger.info("Calendar event deleted successfully with ID: {}", id);
    }

    // Search calendar events
    public Page<CalendarEventDTO> searchCalendarEvents(String searchTerm, int page, int size) {
        logger.info("Searching calendar events with term: {}", searchTerm);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<CalendarEvent> events = calendarEventRepository.findByTitleContainingIgnoreCase(searchTerm, pageable);
        
        return events.map(this::convertToDTO);
    }

    // Get calendar events by date range
    public List<CalendarEventDTO> getCalendarEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching calendar events from {} to {}", startDate, endDate);
        
        List<CalendarEvent> events = calendarEventRepository.findByStartDateBetween(startDate, endDate);
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get calendar events by client
    public List<CalendarEventDTO> getCalendarEventsByClient(Long clientId) {
        logger.info("Fetching calendar events for client: {}", clientId);
        
        List<CalendarEvent> events = calendarEventRepository.findByClient_Id(clientId);
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get calendar events by trainer
    public List<CalendarEventDTO> getCalendarEventsByTrainer(Long trainerId) {
        logger.info("Fetching calendar events for trainer: {}", trainerId);
        
        List<CalendarEvent> events = calendarEventRepository.findByTrainer_Id(trainerId);
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get today's calendar events
    public List<CalendarEventDTO> getTodaysCalendarEvents() {
        logger.info("Fetching today's calendar events");
        
        LocalDate today = LocalDate.now();
        List<CalendarEvent> events = calendarEventRepository.findByStartDate(today);
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get upcoming calendar events
    public List<CalendarEventDTO> getUpcomingCalendarEvents(int days) {
        logger.info("Fetching upcoming calendar events for next {} days", days);
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        List<CalendarEvent> events = calendarEventRepository.findByStartDateBetween(startDate, endDate);
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Convert entity to DTO
    private CalendarEventDTO convertToDTO(CalendarEvent event) {
        CalendarEventDTO dto = new CalendarEventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setEventType(event.getEventType());
        dto.setStartDate(event.getStartDate());
        dto.setStartTime(event.getStartTime());
        dto.setEndDate(event.getEndDate());
        dto.setEndTime(event.getEndTime());
        dto.setLocation(event.getLocation());
        dto.setStatus(event.getStatus());
        dto.setPriority(event.getPriority());
        dto.setIsRecurring(event.getIsRecurring());
        dto.setRecurrencePattern(event.getRecurrencePattern());
        dto.setRecurrenceEndDate(event.getRecurrenceEndDate());
        dto.setClientId(event.getClientId());
        dto.setTrainerId(event.getTrainerId());
        dto.setWorkoutPlanId(event.getWorkoutPlanId());
        dto.setNutritionPlanId(event.getNutritionPlanId());
        dto.setNotes(event.getNotes());
        dto.setReminderMinutes(event.getReminderMinutes());
        dto.setCreatedAt(event.getCreatedAt());
        dto.setUpdatedAt(event.getUpdatedAt());
        
        // Set related entity names if available
        if (event.getClientId() != null) {
            Optional<User> clientOpt = userRepository.findById(event.getClientId());
            if (clientOpt.isPresent()) {
                dto.setClientName(clientOpt.get().getFirstName() + " " + clientOpt.get().getLastName());
            }
        }
        
        if (event.getTrainerId() != null) {
            Optional<User> trainerOpt = userRepository.findById(event.getTrainerId());
            if (trainerOpt.isPresent()) {
                dto.setTrainerName(trainerOpt.get().getFirstName() + " " + trainerOpt.get().getLastName());
            }
        }
        
        return dto;
    }
}
