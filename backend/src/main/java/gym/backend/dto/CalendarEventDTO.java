package gym.backend.dto;

import gym.backend.model.CalendarEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CalendarEventDTO {
    private Long id;
    private String title;
    private String description;
    private CalendarEvent.EventType eventType;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String location;
    private CalendarEvent.EventStatus status;
    private CalendarEvent.Priority priority;
    private Boolean isRecurring;
    private CalendarEvent.RecurrencePattern recurrencePattern;
    private LocalDate recurrenceEndDate;
    private Long clientId;
    private String clientName;
    private Long trainerId;
    private String trainerName;
    private Long workoutPlanId;
    private String workoutPlanName;
    private Long nutritionPlanId;
    private String nutritionPlanName;
    private String notes;
    private Integer reminderMinutes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public CalendarEventDTO() {}

    public CalendarEventDTO(Long id, String title, CalendarEvent.EventType eventType, 
                           LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        this.id = id;
        this.title = title;
        this.eventType = eventType;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public Long getWorkoutPlanId() {
        return workoutPlanId;
    }

    public void setWorkoutPlanId(Long workoutPlanId) {
        this.workoutPlanId = workoutPlanId;
    }

    public String getWorkoutPlanName() {
        return workoutPlanName;
    }

    public void setWorkoutPlanName(String workoutPlanName) {
        this.workoutPlanName = workoutPlanName;
    }

    public Long getNutritionPlanId() {
        return nutritionPlanId;
    }

    public void setNutritionPlanId(Long nutritionPlanId) {
        this.nutritionPlanId = nutritionPlanId;
    }

    public String getNutritionPlanName() {
        return nutritionPlanName;
    }

    public void setNutritionPlanName(String nutritionPlanName) {
        this.nutritionPlanName = nutritionPlanName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public String getFormattedStartDateTime() {
        if (startDate != null && startTime != null) {
            return startDate.toString() + " " + startTime.toString();
        }
        return "";
    }

    public String getFormattedEndDateTime() {
        if (endDate != null && endTime != null) {
            return endDate.toString() + " " + endTime.toString();
        }
        return "";
    }

    public boolean isToday() {
        return startDate != null && startDate.equals(LocalDate.now());
    }

    public boolean isPast() {
        return startDate != null && startDate.isBefore(LocalDate.now());
    }

    public boolean isFuture() {
        return startDate != null && startDate.isAfter(LocalDate.now());
    }

    @Override
    public String toString() {
        return "CalendarEventDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", eventType=" + eventType +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", status=" + status +
                '}';
    }
}
