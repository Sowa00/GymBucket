package gym.backend.dto;

import gym.backend.model.FileUpload;

import java.time.LocalDateTime;

public class FileUploadDTO {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String fileUrl;
    private String mimeType;
    private Long fileSize;
    private FileUpload.FileType fileType;
    private FileUpload.FileCategory category;
    private String description;
    private Long uploadedById;
    private String uploadedByName;
    private Long clientId;
    private String clientName;
    private Long workoutPlanId;
    private String workoutPlanName;
    private Long nutritionPlanId;
    private String nutritionPlanName;
    private Boolean isPublic;
    private String tags; // JSON string
    private LocalDateTime uploadedAt;
    private LocalDateTime lastAccessedAt;
    private Integer accessCount;

    // Constructors
    public FileUploadDTO() {}

    public FileUploadDTO(Long id, String fileName, String originalFileName, 
                        String filePath, String mimeType, Long fileSize, 
                        FileUpload.FileType fileType) {
        this.id = id;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public FileUpload.FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileUpload.FileType fileType) {
        this.fileType = fileType;
    }

    public FileUpload.FileCategory getCategory() {
        return category;
    }

    public void setCategory(FileUpload.FileCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUploadedById() {
        return uploadedById;
    }

    public void setUploadedById(Long uploadedById) {
        this.uploadedById = uploadedById;
    }

    public String getUploadedByName() {
        return uploadedByName;
    }

    public void setUploadedByName(String uploadedByName) {
        this.uploadedByName = uploadedByName;
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

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
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

    // Helper methods
    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public boolean hasClient() {
        return clientId != null;
    }

    public boolean hasWorkoutPlan() {
        return workoutPlanId != null;
    }

    public boolean hasNutritionPlan() {
        return nutritionPlanId != null;
    }

    public boolean isPublic() {
        return isPublic != null && isPublic;
    }

    public boolean isPrivate() {
        return !isPublic();
    }

    public boolean hasTags() {
        return tags != null && !tags.trim().isEmpty();
    }

    public boolean hasFileUrl() {
        return fileUrl != null && !fileUrl.trim().isEmpty();
    }

    public String getFormattedFileSize() {
        if (fileSize == null) {
            return "N/A";
        }
        
        if (fileSize < 1024) {
            return fileSize + " B";
        } else if (fileSize < 1024 * 1024) {
            return String.format("%.1f KB", fileSize / 1024.0);
        } else if (fileSize < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", fileSize / (1024.0 * 1024.0 * 1024.0));
        }
    }

    public String getFormattedUploadedAt() {
        if (uploadedAt == null) {
            return "N/A";
        }
        
        LocalDateTime now = LocalDateTime.now();
        long minutesAgo = java.time.temporal.ChronoUnit.MINUTES.between(uploadedAt, now);
        
        if (minutesAgo < 1) {
            return "Teraz";
        } else if (minutesAgo < 60) {
            return minutesAgo + " min temu";
        } else if (minutesAgo < 1440) { // 24 hours
            long hoursAgo = minutesAgo / 60;
            return hoursAgo + " godz. temu";
        } else {
            long daysAgo = minutesAgo / 1440;
            return daysAgo + " dni temu";
        }
    }

    public String getFileIcon() {
        if (fileType == null) {
            return "📄";
        }
        
        switch (fileType) {
            case IMAGE:
                return "🖼️";
            case VIDEO:
                return "🎥";
            case AUDIO:
                return "🎵";
            case DOCUMENT:
                return "📄";
            case PDF:
                return "📕";
            case SPREADSHEET:
                return "📊";
            case PRESENTATION:
                return "📽️";
            case ARCHIVE:
                return "📦";
            case OTHER:
                return "📄";
            default:
                return "📄";
        }
    }

    public String getCategoryIcon() {
        if (category == null) {
            return "📁";
        }
        
        switch (category) {
            case PROFILE_PHOTO:
                return "👤";
            case WORKOUT_VIDEO:
                return "💪";
            case NUTRITION_IMAGE:
                return "🥗";
            case PROGRESS_PHOTO:
                return "📸";
            case DOCUMENT:
                return "📄";
            case CERTIFICATE:
                return "🏆";
            case OTHER:
                return "📁";
            default:
                return "📁";
        }
    }

    public boolean isImage() {
        return fileType == FileUpload.FileType.IMAGE;
    }

    public boolean isVideo() {
        return fileType == FileUpload.FileType.VIDEO;
    }

    public boolean isAudio() {
        return fileType == FileUpload.FileType.AUDIO;
    }

    public boolean isDocument() {
        return fileType == FileUpload.FileType.DOCUMENT;
    }

    public boolean isPdf() {
        return fileType == FileUpload.FileType.PDF;
    }

    public boolean isSpreadsheet() {
        return fileType == FileUpload.FileType.SPREADSHEET;
    }

    public boolean isPresentation() {
        return fileType == FileUpload.FileType.PRESENTATION;
    }

    public boolean isArchive() {
        return fileType == FileUpload.FileType.ARCHIVE;
    }

    public boolean isRecent() {
        if (uploadedAt == null) {
            return false;
        }
        return uploadedAt.isAfter(LocalDateTime.now().minusDays(7));
    }

    public boolean isToday() {
        if (uploadedAt == null) {
            return false;
        }
        return uploadedAt.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }

    public boolean isThisWeek() {
        if (uploadedAt == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDateTime endOfWeek = startOfWeek.plusDays(6);
        return !uploadedAt.isBefore(startOfWeek) && !uploadedAt.isAfter(endOfWeek);
    }

    public boolean isThisMonth() {
        if (uploadedAt == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        return uploadedAt.getYear() == now.getYear() && 
               uploadedAt.getMonth() == now.getMonth();
    }

    public boolean isLargeFile() {
        if (fileSize == null) {
            return false;
        }
        return fileSize > 10 * 1024 * 1024; // 10MB
    }

    public boolean isSmallFile() {
        if (fileSize == null) {
            return false;
        }
        return fileSize < 1024 * 1024; // 1MB
    }

    public boolean isMediumFile() {
        if (fileSize == null) {
            return false;
        }
        return fileSize >= 1024 * 1024 && fileSize <= 10 * 1024 * 1024; // 1MB to 10MB
    }

    @Override
    public String toString() {
        return "FileUploadDTO{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", originalFileName='" + originalFileName + '\'' +
                ", fileType=" + fileType +
                ", category=" + category +
                ", fileSize=" + fileSize +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
