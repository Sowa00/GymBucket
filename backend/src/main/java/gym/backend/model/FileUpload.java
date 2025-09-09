package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_uploads")
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "stored_filename", nullable = false, length = 255)
    private String storedFilename;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false, length = 50)
    private FileType fileType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FileCategory category;

    @Column(name = "entity_type", length = 50)
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(name = "is_public")
    private Boolean isPublic = false;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id")
    private WorkoutPlan workoutPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrition_plan_id")
    private NutritionPlan nutritionPlan;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

    @Column(name = "access_count")
    private Integer accessCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public FileUpload() {}

    public FileUpload(String originalFilename, String storedFilename, String filePath, 
                     Long fileSize, String mimeType, FileType fileType, FileCategory category, 
                     User uploadedBy) {
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.fileType = fileType;
        this.category = category;
        this.uploadedBy = uploadedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public FileCategory getCategory() {
        return category;
    }

    public void setCategory(FileCategory category) {
        this.category = category;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
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

    // Additional getters and setters for new fields
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public NutritionPlan getNutritionPlan() {
        return nutritionPlan;
    }

    public void setNutritionPlan(NutritionPlan nutritionPlan) {
        this.nutritionPlan = nutritionPlan;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public Integer getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }

    // Enums
    public enum FileType {
        IMAGE("Obraz"),
        DOCUMENT("Dokument"),
        VIDEO("Wideo"),
        AUDIO("Audio"),
        PDF("PDF"),
        SPREADSHEET("Arkusz kalkulacyjny"),
        PRESENTATION("Prezentacja"),
        ARCHIVE("Archiwum"),
        OTHER("Inne");

        private final String displayName;

        FileType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum FileCategory {
        PROFILE_PHOTO("Zdjęcie profilowe"),
        EXERCISE_IMAGE("Zdjęcie ćwiczenia"),
        PROGRESS_PHOTO("Zdjęcie postępów"),
        DOCUMENT("Dokument"),
        WORKOUT_VIDEO("Wideo treningu"),
        NUTRITION_IMAGE("Zdjęcie posiłku"),
        CERTIFICATE("Certyfikat"),
        OTHER("Inne");

        private final String displayName;

        FileCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Helper methods
    public String getFileExtension() {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
    }

    public String getFormattedFileSize() {
        if (fileSize == null) {
            return "0 B";
        }
        
        long bytes = fileSize;
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    public boolean isImage() {
        return fileType == FileType.IMAGE;
    }

    public boolean isVideo() {
        return fileType == FileType.VIDEO;
    }

    public boolean isDocument() {
        return fileType == FileType.DOCUMENT;
    }

    public void associateWithEntity(String entityType, Long entityId) {
        this.entityType = entityType;
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return "FileUpload{" +
                "id=" + id +
                ", originalFilename='" + originalFilename + '\'' +
                ", fileType=" + fileType +
                ", category=" + category +
                ", fileSize=" + getFormattedFileSize() +
                ", uploadedBy=" + (uploadedBy != null ? uploadedBy.getFullName() : "null") +
                '}';
    }

    // Additional methods needed by FileUploadService
    public String getFileName() {
        return storedFilename;
    }

    public void setFileName(String fileName) {
        this.storedFilename = fileName;
    }

    public String getOriginalFileName() {
        return originalFilename;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFilename = originalFileName;
    }

    public String getFileUrl() {
        return filePath;
    }

    public void setFileUrl(String fileUrl) {
        this.filePath = fileUrl;
    }

    public Long getUploadedById() {
        return uploadedBy != null ? uploadedBy.getId() : null;
    }

    public void setUploadedById(Long uploadedById) {
        // This method is used by services but doesn't actually set the uploadedBy
        // The uploadedBy should be set through the setUploadedBy method
    }

    public Long getClientId() {
        return client != null ? client.getId() : null;
    }

    public void setClientId(Long clientId) {
        // This method is used by services but doesn't actually set the client
        // The client should be set through the setClient method
    }

    public Long getWorkoutPlanId() {
        return workoutPlan != null ? workoutPlan.getId() : null;
    }

    public void setWorkoutPlanId(Long workoutPlanId) {
        // This method is used by services but doesn't actually set the workout plan
        // The workout plan should be set through the setWorkoutPlan method
    }

    public Long getNutritionPlanId() {
        return nutritionPlan != null ? nutritionPlan.getId() : null;
    }

    public void setNutritionPlanId(Long nutritionPlanId) {
        // This method is used by services but doesn't actually set the nutrition plan
        // The nutrition plan should be set through the setNutritionPlan method
    }
}
