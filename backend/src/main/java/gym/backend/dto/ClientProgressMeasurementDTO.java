package gym.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientProgressMeasurementDTO {
    private Long id;
    private Long clientId;
    private String clientName;
    private LocalDate measurementDate;
    private Double weight;
    private Double bodyFatPercentage;
    private Double muscleMass;
    private String measurements; // JSON string
    private String photos; // JSON string
    private String notes;
    private Long measuredById;
    private String measuredByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ClientProgressMeasurementDTO() {}

    public ClientProgressMeasurementDTO(Long id, Long clientId, String clientName, 
                                      LocalDate measurementDate, Double weight) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.measurementDate = measurementDate;
        this.weight = weight;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMeasuredByName() {
        return measuredByName;
    }

    public void setMeasuredByName(String measuredByName) {
        this.measuredByName = measuredByName;
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

    public String getFormattedWeight() {
        if (weight == null) {
            return "N/A";
        }
        return String.format("%.1f kg", weight);
    }

    public String getFormattedBodyFat() {
        if (bodyFatPercentage == null) {
            return "N/A";
        }
        return String.format("%.1f%%", bodyFatPercentage);
    }

    public String getFormattedMuscleMass() {
        if (muscleMass == null) {
            return "N/A";
        }
        return String.format("%.1f kg", muscleMass);
    }

    public String getFormattedMeasurementDate() {
        if (measurementDate == null) {
            return "N/A";
        }
        return measurementDate.toString();
    }

    public boolean isRecent() {
        if (measurementDate == null) {
            return false;
        }
        return measurementDate.isAfter(LocalDate.now().minusDays(30));
    }

    public boolean isToday() {
        return measurementDate != null && measurementDate.equals(LocalDate.now());
    }

    public boolean isThisWeek() {
        if (measurementDate == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return !measurementDate.isBefore(startOfWeek) && !measurementDate.isAfter(endOfWeek);
    }

    public boolean isThisMonth() {
        if (measurementDate == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return measurementDate.getYear() == now.getYear() && 
               measurementDate.getMonth() == now.getMonth();
    }

    @Override
    public String toString() {
        return "ClientProgressMeasurementDTO{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", measurementDate=" + measurementDate +
                ", weight=" + weight +
                ", bodyFatPercentage=" + bodyFatPercentage +
                ", muscleMass=" + muscleMass +
                '}';
    }
}
