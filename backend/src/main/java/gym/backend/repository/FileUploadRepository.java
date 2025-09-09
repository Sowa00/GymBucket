package gym.backend.repository;

import gym.backend.model.FileUpload;
import gym.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

    // Find files by user
    List<FileUpload> findByUploadedBy(User user);
    
    // Find files by user with pagination
    Page<FileUpload> findByUploadedBy(User user, Pageable pageable);
    
    // Additional methods needed by FileUploadService
    List<FileUpload> findByUploadedBy_Id(Long userId);
    List<FileUpload> findByFileType(String fileType);
    List<FileUpload> findByClient_Id(Long clientId);
    List<FileUpload> findByWorkoutPlan_Id(Long workoutPlanId);
    List<FileUpload> findByNutritionPlan_Id(Long nutritionPlanId);
    List<FileUpload> findByIsPublicTrue();
    List<FileUpload> findByIsPublicFalse();
    List<FileUpload> findByUploadedAtAfter(java.time.LocalDateTime dateTime);
    List<FileUpload> findByUploadedAtBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    List<FileUpload> findByFileSizeBetween(Long minSize, Long maxSize);
    List<FileUpload> findByFileSizeGreaterThan(Long size);
    List<FileUpload> findByFileSizeLessThan(Long size);
    List<FileUpload> findByTagsContaining(String tag);
    
    @Query("SELECT SUM(f.fileSize) FROM FileUpload f")
    Long getTotalFileSize();

    // Find files by entity type and ID
    List<FileUpload> findByEntityTypeAndEntityId(String entityType, Long entityId);

    // Find files by category
    List<FileUpload> findByCategory(FileUpload.FileCategory category);
    
    // Find files by file type
    List<FileUpload> findByFileType(FileUpload.FileType fileType);
    
    // Find public files
    List<FileUpload> findByIsPublic(Boolean isPublic);
    
    // Find files by MIME type
    List<FileUpload> findByMimeType(String mimeType);
    
    // Find files by original filename
    List<FileUpload> findByOriginalFilenameContaining(String filename);
    
    // Find files by stored filename
    FileUpload findByStoredFilename(String storedFilename);
    
    // Find files by file path
    FileUpload findByFilePath(String filePath);
    
    // Find files by size range
    @Query("SELECT fu FROM FileUpload fu WHERE fu.fileSize BETWEEN :minSize AND :maxSize")
    List<FileUpload> findByFileSizeRange(@Param("minSize") Long minSize, @Param("maxSize") Long maxSize);
    
    // Find large files
    @Query("SELECT fu FROM FileUpload fu WHERE fu.fileSize > :size")
    List<FileUpload> findLargeFiles(@Param("size") Long size);
    
    // Find files by entity type
    List<FileUpload> findByEntityType(String entityType);
    
    // Find files by user and category
    List<FileUpload> findByUploadedByAndCategory(User user, FileUpload.FileCategory category);
    
    // Find files by user and file type
    List<FileUpload> findByUploadedByAndFileType(User user, FileUpload.FileType fileType);
    
    // Find files by user and entity type
    List<FileUpload> findByUploadedByAndEntityType(User user, String entityType);
    
    // Count files by user
    Long countByUploadedBy(User user);
    
    // Count files by category
    Long countByCategory(FileUpload.FileCategory category);
    
    // Count files by file type
    Long countByFileType(FileUpload.FileType fileType);
    
    // Count files by entity type
    Long countByEntityType(String entityType);
    
    // Find recent files
    @Query("SELECT fu FROM FileUpload fu WHERE fu.uploadedBy = :user ORDER BY fu.createdAt DESC")
    List<FileUpload> findRecentByUser(@Param("user") User user);
    
    // Find files by date range
    @Query("SELECT fu FROM FileUpload fu WHERE fu.uploadedBy = :user AND " +
           "fu.createdAt BETWEEN :startDate AND :endDate ORDER BY fu.createdAt DESC")
    List<FileUpload> findByUserAndDateRange(@Param("user") User user, 
                                          @Param("startDate") java.time.LocalDateTime startDate, 
                                          @Param("endDate") java.time.LocalDateTime endDate);
    
    // Find duplicate files (same original filename and size)
    @Query("SELECT fu FROM FileUpload fu WHERE fu.originalFilename = :filename AND fu.fileSize = :size")
    List<FileUpload> findDuplicates(@Param("filename") String filename, @Param("size") Long size);
    
    // Find files by extension
    @Query("SELECT fu FROM FileUpload fu WHERE fu.originalFilename LIKE %:extension")
    List<FileUpload> findByFileExtension(@Param("extension") String extension);
    
    // Find images
    @Query("SELECT fu FROM FileUpload fu WHERE fu.fileType = 'IMAGE'")
    List<FileUpload> findImages();
    
    // Find videos
    @Query("SELECT fu FROM FileUpload fu WHERE fu.fileType = 'VIDEO'")
    List<FileUpload> findVideos();
    
    // Find documents
    @Query("SELECT fu FROM FileUpload fu WHERE fu.fileType = 'DOCUMENT'")
    List<FileUpload> findDocuments();
    
    // Find files by user and public status
    List<FileUpload> findByUploadedByAndIsPublic(User user, Boolean isPublic);
    
    // Find files by entity type and public status
    List<FileUpload> findByEntityTypeAndIsPublic(String entityType, Boolean isPublic);
    
    // Find files by category and public status
    List<FileUpload> findByCategoryAndIsPublic(FileUpload.FileCategory category, Boolean isPublic);
}
