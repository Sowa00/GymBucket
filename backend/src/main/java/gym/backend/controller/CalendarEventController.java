package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.CalendarEventDTO;
import gym.backend.dto.CalendarEventRequestDTO;
import gym.backend.service.CalendarEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendar-events")
@CrossOrigin(origins = "http://localhost:4200")
public class CalendarEventController {
    
    private static final Logger logger = LoggerFactory.getLogger(CalendarEventController.class);
    
    @Autowired
    private CalendarEventService calendarEventService;

    // Get all calendar events with pagination
    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllCalendarEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            logger.info("Fetching all calendar events with pagination - page: {}, size: {}", page, size);
            Page<CalendarEventDTO> events = calendarEventService.getAllCalendarEvents(page, size, sortBy, sortDir);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Calendar events fetched successfully", events));
        } catch (Exception e) {
            logger.error("Error fetching calendar events", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching calendar events: " + e.getMessage(), null));
        }
    }

    // Get all calendar events as list
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllCalendarEventsList() {
        try {
            logger.info("Fetching all calendar events as list");
            List<CalendarEventDTO> events = calendarEventService.getAllCalendarEventsList();
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Calendar events fetched successfully", events));
        } catch (Exception e) {
            logger.error("Error fetching calendar events list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching calendar events: " + e.getMessage(), null));
        }
    }

    // Get calendar event by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getCalendarEventById(@PathVariable Long id) {
        try {
            logger.info("Fetching calendar event by ID: {}", id);
            Optional<CalendarEventDTO> event = calendarEventService.getCalendarEventById(id);
            
            if (event.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Calendar event fetched successfully", event.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Calendar event not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching calendar event by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching calendar event: " + e.getMessage(), null));
        }
    }

    // Create new calendar event
    @PostMapping
    public ResponseEntity<ApiResponseDTO> createCalendarEvent(
            @RequestBody CalendarEventRequestDTO request,
            @RequestHeader("X-User-ID") Long userId) {
        
        try {
            logger.info("Creating new calendar event: {} for user: {}", request.getTitle(), userId);
            CalendarEventDTO event = calendarEventService.createCalendarEvent(request, userId);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Calendar event created successfully", event));
        } catch (Exception e) {
            logger.error("Error creating calendar event", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating calendar event: " + e.getMessage(), null));
        }
    }

    // Update calendar event
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateCalendarEvent(
            @PathVariable Long id,
            @RequestBody CalendarEventRequestDTO request,
            @RequestHeader("X-User-ID") Long userId) {
        
        try {
            logger.info("Updating calendar event: {} for user: {}", id, userId);
            CalendarEventDTO event = calendarEventService.updateCalendarEvent(id, request, userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Calendar event updated successfully", event));
        } catch (Exception e) {
            logger.error("Error updating calendar event: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating calendar event: " + e.getMessage(), null));
        }
    }

    // Delete calendar event
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteCalendarEvent(
            @PathVariable Long id,
            @RequestHeader("X-User-ID") Long userId) {
        
        try {
            logger.info("Deleting calendar event: {} for user: {}", id, userId);
            calendarEventService.deleteCalendarEvent(id, userId);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Calendar event deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting calendar event: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting calendar event: " + e.getMessage(), null));
        }
    }

    // Search calendar events
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO> searchCalendarEvents(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            logger.info("Searching calendar events with term: {}", searchTerm);
            Page<CalendarEventDTO> events = calendarEventService.searchCalendarEvents(searchTerm, page, size);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Calendar events search completed", events));
        } catch (Exception e) {
            logger.error("Error searching calendar events", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error searching calendar events: " + e.getMessage(), null));
        }
    }

    // Get calendar events by date range
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponseDTO> getCalendarEventsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        
        try {
            logger.info("Fetching calendar events from {} to {}", startDate, endDate);
            List<CalendarEventDTO> events = calendarEventService.getCalendarEventsByDateRange(startDate, endDate);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Calendar events fetched successfully", events));
        } catch (Exception e) {
            logger.error("Error fetching calendar events by date range", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching calendar events: " + e.getMessage(), null));
        }
    }

    // Get calendar events by client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponseDTO> getCalendarEventsByClient(@PathVariable Long clientId) {
        try {
            logger.info("Fetching calendar events for client: {}", clientId);
            List<CalendarEventDTO> events = calendarEventService.getCalendarEventsByClient(clientId);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Calendar events fetched successfully", events));
        } catch (Exception e) {
            logger.error("Error fetching calendar events for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching calendar events: " + e.getMessage(), null));
        }
    }

    // Get calendar events by trainer
    @GetMapping("/trainer/{trainerId}")
    public ResponseEntity<ApiResponseDTO> getCalendarEventsByTrainer(@PathVariable Long trainerId) {
        try {
            logger.info("Fetching calendar events for trainer: {}", trainerId);
            List<CalendarEventDTO> events = calendarEventService.getCalendarEventsByTrainer(trainerId);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Calendar events fetched successfully", events));
        } catch (Exception e) {
            logger.error("Error fetching calendar events for trainer: {}", trainerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching calendar events: " + e.getMessage(), null));
        }
    }

    // Get today's calendar events
    @GetMapping("/today")
    public ResponseEntity<ApiResponseDTO> getTodaysCalendarEvents() {
        try {
            logger.info("Fetching today's calendar events");
            List<CalendarEventDTO> events = calendarEventService.getTodaysCalendarEvents();
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Today's calendar events fetched successfully", events));
        } catch (Exception e) {
            logger.error("Error fetching today's calendar events", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching today's calendar events: " + e.getMessage(), null));
        }
    }

    // Get upcoming calendar events
    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponseDTO> getUpcomingCalendarEvents(
            @RequestParam(defaultValue = "7") int days) {
        
        try {
            logger.info("Fetching upcoming calendar events for next {} days", days);
            List<CalendarEventDTO> events = calendarEventService.getUpcomingCalendarEvents(days);
            
            return ResponseEntity.ok(new ApiResponseDTO(true, "Upcoming calendar events fetched successfully", events));
        } catch (Exception e) {
            logger.error("Error fetching upcoming calendar events", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching upcoming calendar events: " + e.getMessage(), null));
        }
    }
}
