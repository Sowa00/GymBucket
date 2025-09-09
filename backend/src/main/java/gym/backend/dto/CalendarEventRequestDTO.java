package gym.backend.dto;

import gym.backend.model.CalendarEvent;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarEventRequestDTO {
    
    // @NotBlank(message = "Title is required")
    // @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;
    
    // @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    // @NotNull(message = "Event type is required")
    private CalendarEvent.EventType eventType;
    
    // @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    // @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    // @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    // @NotNull(message = "End time is required")
    private LocalTime endTime;
    
    // @Size(max = 500, message = "Location must not exceed 500 characters")
    private String location;
    
    private CalendarEvent.EventStatus status = CalendarEvent.EventStatus.SCHEDULED;
    
    private CalendarEvent.Priority priority = CalendarEvent.Priority.MEDIUM;
    
    private Boolean isRecurring = false;
    
    private CalendarEvent.RecurrencePattern recurrencePattern;
    
    private LocalDate recurrenceEndDate;
    
    private Long clientId;
    
    // @NotNull(message = "Trainer ID is required")
    private Long trainerId;
    
    private Long workoutPlanId;
    
    private Long nutritionPlanId;
    
    // @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
    
    private Integer reminderMinutes = 15;

    // Constructors
    public CalendarEventRequestDTO() {}

    public CalendarEventRequestDTO(String title, CalendarEvent.EventType eventType, 
                                  LocalDate startDate, LocalTime startTime, 
                                  LocalDate endDate, LocalTime endTime, Long trainerId) {
        this.title = title;
        this.eventType = eventType;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.trainerId = trainerId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CalendarEvent.EventType getEventType() {
        return eventType;
    }

    public void setEventType(CalendarEvent.EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CalendarEvent.EventStatus getStatus() {
        return status;
    }

    public void setStatus(CalendarEvent.EventStatus status) {
        this.status = status;
    }

    public CalendarEvent.Priority getPriority() {
        return priority;
    }

    public void setPriority(CalendarEvent.Priority priority) {
        this.priority = priority;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public CalendarEvent.RecurrencePattern getRecurrencePattern() {
        return recurrencePattern;
    }

    public void setRecurrencePattern(CalendarEvent.RecurrencePattern recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public LocalDate getRecurrenceEndDate() {
        return recurrenceEndDate;
    }

    public void setRecurrenceEndDate(LocalDate recurrenceEndDate) {
        this.recurrenceEndDate = recurrenceEndDate;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public Long getWorkoutPlanId() {
        return workoutPlanId;
    }

    public void setWorkoutPlanId(Long workoutPlanId) {
        this.workoutPlanId = workoutPlanId;
    }

    public Long getNutritionPlanId() {
        return nutritionPlanId;
    }

    public void setNutritionPlanId(Long nutritionPlanId) {
        this.nutritionPlanId = nutritionPlanId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(Integer reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }

    // Helper methods
    public boolean hasClient() {
        return clientId != null;
    }

    public boolean hasWorkoutPlan() {
        return workoutPlanId != null;
    }

    public boolean hasNutritionPlan() {
        return nutritionPlanId != null;
    }

    public boolean isRecurringEvent() {
        return isRecurring != null && isRecurring;
    }

    @Override
    public String toString() {
        return "CalendarEventRequestDTO{" +
                "title='" + title + '\'' +
                ", eventType=" + eventType +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", endDate=" + endDate +
                ", endTime=" + endTime +
                ", trainerId=" + trainerId +
                '}';
    }
}
