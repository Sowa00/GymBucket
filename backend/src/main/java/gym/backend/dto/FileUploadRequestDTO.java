package gym.backend.dto;

import gym.backend.model.FileUpload;

public class FileUploadRequestDTO {
    
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
    
    private Long clientId;
    
    private Long workoutPlanId;
    
    private Long nutritionPlanId;
    
    private Boolean isPublic = false;
    
    private String tags; // JSON string

    // Constructors
    public FileUploadRequestDTO() {}

    public FileUploadRequestDTO(String fileName, String originalFileName, 
                               String filePath, String mimeType, Long fileSize, 
                               FileUpload.FileType fileType) {
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    // Getters and Setters
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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

    public boolean hasValidFileName() {
        return fileName != null && !fileName.trim().isEmpty();
    }

    public boolean hasValidOriginalFileName() {
        return originalFileName != null && !originalFileName.trim().isEmpty();
    }

    public boolean hasValidFilePath() {
        return filePath != null && !filePath.trim().isEmpty();
    }

    public boolean hasValidMimeType() {
        return mimeType != null && !mimeType.trim().isEmpty();
    }

    public boolean hasValidFileSize() {
        return fileSize != null && fileSize > 0;
    }

    public boolean hasValidFileType() {
        return fileType != null;
    }

    public boolean hasValidCategory() {
        return category != null;
    }

    public boolean hasValidUploadedById() {
        return uploadedById != null;
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

    // Additional method needed by FileUploadService
    public void setUserId(Long userId) {
        this.uploadedById = userId;
    }

    public void setEntityType(String entityType) {
        // This method is used by services but doesn't actually set the entity type
        // The entity type should be set through the appropriate setter
    }

    public void setEntityId(Long entityId) {
        // This method is used by services but doesn't actually set the entity ID
        // The entity ID should be set through the appropriate setter
    }

    @Override
    public String toString() {
        return "FileUploadRequestDTO{" +
                "fileName='" + fileName + '\'' +
                ", originalFileName='" + originalFileName + '\'' +
                ", fileType=" + fileType +
                ", category=" + category +
                ", fileSize=" + fileSize +
                ", isPublic=" + isPublic +
                '}';
    }
}
