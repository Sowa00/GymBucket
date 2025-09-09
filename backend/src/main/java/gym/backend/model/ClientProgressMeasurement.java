package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_progress_measurements")
public class ClientProgressMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "measurement_date", nullable = false)
    private LocalDate measurementDate;

    @Column
    private Double weight; // in kg

    @Column(name = "body_fat_percentage")
    private Double bodyFatPercentage;

    @Column(name = "muscle_mass")
    private Double muscleMass; // in kg

    @Column(columnDefinition = "JSON")
    private String measurements; // JSON object for body measurements (chest, waist, arms, etc.)

    @Column(columnDefinition = "JSON")
    private String photos; // JSON array of photo URLs

    @Column(length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measured_by", nullable = false)
    private User measuredBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public ClientProgressMeasurement() {}

    public ClientProgressMeasurement(Client client, LocalDate measurementDate, User measuredBy) {
        this.client = client;
        this.measurementDate = measurementDate;
        this.measuredBy = measuredBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public User getMeasuredBy() {
        return measuredBy;
    }

    public void setMeasuredBy(User measuredBy) {
        this.measuredBy = measuredBy;
    }

    public Long getClientId() {
        return client != null ? client.getId() : null;
    }

    public void setClientId(Long clientId) {
        // This method is used by services but doesn't actually set the client
        // The client should be set through the setClient method
    }

    public Long getMeasuredById() {
        return measuredBy != null ? measuredBy.getId() : null;
    }

    public void setMeasuredById(Long measuredById) {
        // This method is used by services but doesn't actually set the measuredBy
        // The measuredBy should be set through the setMeasuredBy method
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
    public Double getBMI() {
        if (weight == null || client.getHeight() == null || client.getHeight() <= 0) {
            return null;
        }
        double heightInMeters = client.getHeight() / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    public boolean hasWeightChange(ClientProgressMeasurement previous) {
        if (weight == null || previous.getWeight() == null) {
            return false;
        }
        return !weight.equals(previous.getWeight());
    }

    public Double getWeightChange(ClientProgressMeasurement previous) {
        if (weight == null || previous.getWeight() == null) {
            return null;
        }
        return weight - previous.getWeight();
    }

    public boolean hasBodyFatChange(ClientProgressMeasurement previous) {
        if (bodyFatPercentage == null || previous.getBodyFatPercentage() == null) {
            return false;
        }
        return !bodyFatPercentage.equals(previous.getBodyFatPercentage());
    }

    public Double getBodyFatChange(ClientProgressMeasurement previous) {
        if (bodyFatPercentage == null || previous.getBodyFatPercentage() == null) {
            return null;
        }
        return bodyFatPercentage - previous.getBodyFatPercentage();
    }

    @Override
    public String toString() {
        return "ClientProgressMeasurement{" +
                "id=" + id +
                ", client=" + (client != null ? client.getFullName() : "null") +
                ", measurementDate=" + measurementDate +
                ", weight=" + weight +
                ", bodyFatPercentage=" + bodyFatPercentage +
                ", muscleMass=" + muscleMass +
                '}';
    }
}
