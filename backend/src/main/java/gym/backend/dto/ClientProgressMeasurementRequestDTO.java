package gym.backend.dto;

// import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ClientProgressMeasurementRequestDTO {
    
    // @NotNull(message = "Client ID is required")
    private Long clientId;
    
    // @NotNull(message = "Measurement date is required")
    private LocalDate measurementDate;
    
    private Double weight;
    
    private Double bodyFatPercentage;
    
    private Double muscleMass;
    
    private String measurements; // JSON string for body measurements
    
    private String photos; // JSON string for photo URLs
    
    private String notes;
    
    // @NotNull(message = "Measured by ID is required")
    private Long measuredById;

    // Constructors
    public ClientProgressMeasurementRequestDTO() {}

    public ClientProgressMeasurementRequestDTO(Long clientId, LocalDate measurementDate, Long measuredById) {
        this.clientId = clientId;
        this.measurementDate = measurementDate;
        this.measuredById = measuredById;
    }

    // Getters and Setters
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDate getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(LocalDate measurementDate) {
        this.measurementDate = measurementDate;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public Double getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(Double muscleMass) {
        this.muscleMass = muscleMass;
    }

    public String getMeasurements() {
        return measurements;
    }

    public void setMeasurements(String measurements) {
        this.measurements = measurements;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getMeasuredById() {
        return measuredById;
    }

    public void setMeasuredById(Long measuredById) {
        this.measuredById = measuredById;
    }

    // Helper methods
    public boolean hasWeight() {
        return weight != null;
    }

    public boolean hasBodyFat() {
        return bodyFatPercentage != null;
    }

    public boolean hasMuscleMass() {
        return muscleMass != null;
    }

    public boolean hasMeasurements() {
        return measurements != null && !measurements.trim().isEmpty();
    }

    public boolean hasPhotos() {
        return photos != null && !photos.trim().isEmpty();
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public boolean hasAnyData() {
        return hasWeight() || hasBodyFat() || hasMuscleMass() || hasMeasurements() || hasPhotos() || hasNotes();
    }

    public boolean isToday() {
        return measurementDate != null && measurementDate.equals(LocalDate.now());
    }

    public boolean isFuture() {
        return measurementDate != null && measurementDate.isAfter(LocalDate.now());
    }

    public boolean isPast() {
        return measurementDate != null && measurementDate.isBefore(LocalDate.now());
    }

    public long getDaysFromNow() {
        if (measurementDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), measurementDate);
    }

    public boolean isRecent() {
        return Math.abs(getDaysFromNow()) <= 7;
    }

    public boolean isOld() {
        return getDaysFromNow() < -30;
    }

    @Override
    public String toString() {
        return "ClientProgressMeasurementRequestDTO{" +
                "clientId=" + clientId +
                ", measurementDate=" + measurementDate +
                ", weight=" + weight +
                ", bodyFatPercentage=" + bodyFatPercentage +
                ", muscleMass=" + muscleMass +
                ", measuredById=" + measuredById +
                '}';
    }
}
