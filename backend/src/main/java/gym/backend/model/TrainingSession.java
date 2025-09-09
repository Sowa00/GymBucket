package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "training_sessions")
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, length = 50)
    private String sessionType; // e.g., "Trening siłowy", "Cardio", "Konsultacja żywieniowa"

    @Column(length = 500)
    private String location;

    @Column(length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status = SessionStatus.SCHEDULED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @Column
    private Double price;

    @Column
    private Boolean isPaid = false;

    @Column(length = 1000)
    private String feedback;

    @Column
    private Integer rating; // 1-5 stars

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public TrainingSession() {}

    public TrainingSession(LocalDate sessionDate, LocalTime startTime, LocalTime endTime, 
                          String sessionType, Client client, User trainer) {
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionType = sessionType;
        this.client = client;
        this.trainer = trainer;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getTrainer() {
        return trainer;
    }

    public void setTrainer(User trainer) {
        this.trainer = trainer;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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
    public long getDurationInMinutes() {
        if (startTime == null || endTime == null) {
            return 0;
        }
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    public boolean isInPast() {
        if (sessionDate == null || startTime == null) {
            return false;
        }
        LocalDateTime sessionDateTime = LocalDateTime.of(sessionDate, startTime);
        return sessionDateTime.isBefore(LocalDateTime.now());
    }

    public boolean isToday() {
        if (sessionDate == null) {
            return false;
        }
        return sessionDate.equals(LocalDate.now());
    }

    public boolean isUpcoming() {
        if (sessionDate == null || startTime == null) {
            return false;
        }
        LocalDateTime sessionDateTime = LocalDateTime.of(sessionDate, startTime);
        return sessionDateTime.isAfter(LocalDateTime.now());
    }

    @Override
    public String toString() {
        return "TrainingSession{" +
                "id=" + id +
                ", sessionDate=" + sessionDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", sessionType='" + sessionType + '\'' +
                ", client=" + (client != null ? client.getFullName() : "null") +
                ", trainer=" + (trainer != null ? trainer.getFullName() : "null") +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    // Enum for session status
    public enum SessionStatus {
        SCHEDULED("Zaplanowana"),
        CONFIRMED("Potwierdzona"),
        IN_PROGRESS("W trakcie"),
        COMPLETED("Zakończona"),
        CANCELLED("Anulowana"),
        NO_SHOW("Nie stawił się");

        private final String displayName;

        SessionStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
