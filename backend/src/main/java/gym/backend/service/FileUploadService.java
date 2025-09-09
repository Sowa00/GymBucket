package gym.backend.service;

import gym.backend.dto.FileUploadDTO;
import gym.backend.dto.FileUploadRequestDTO;
import gym.backend.model.FileUpload;
import gym.backend.model.User;
import gym.backend.model.WorkoutPlan;
import gym.backend.model.NutritionPlan;
import gym.backend.repository.FileUploadRepository;
import gym.backend.repository.UserRepository;
import gym.backend.repository.WorkoutPlanRepository;
import gym.backend.repository.NutritionPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileUploadService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
    
    @Autowired
    private FileUploadRepository fileUploadRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private WorkoutPlanRepository workoutPlanRepository;
    
    @Autowired
    private NutritionPlanRepository nutritionPlanRepository;

    // Get all file uploads with pagination
    public Page<FileUploadDTO> getAllFileUploads(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all file uploads with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FileUpload> fileUploads = fileUploadRepository.findAll(pageable);
        
        return fileUploads.map(this::convertToDTO);
    }

    // Get all file uploads as list
    public List<FileUploadDTO> getAllFileUploadsList() {
        logger.info("Fetching all file uploads as list");
        List<FileUpload> fileUploads = fileUploadRepository.findAll();
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file upload by ID
    public Optional<FileUploadDTO> getFileUploadById(Long id) {
        logger.info("Fetching file upload by ID: {}", id);
        return fileUploadRepository.findById(id).map(this::convertToDTO);
    }

    // Create new file upload
    public FileUploadDTO createFileUpload(FileUploadRequestDTO request) {
        logger.info("Creating new file upload: {} for user: {}", request.getFileName(), request.getUploadedById());
        
        Optional<User> uploadedByOpt = userRepository.findById(request.getUploadedById());
        if (uploadedByOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + request.getUploadedById());
        }
        
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFileName(request.getFileName());
        fileUpload.setOriginalFileName(request.getOriginalFileName());
        fileUpload.setFilePath(request.getFilePath());
        fileUpload.setFileUrl(request.getFileUrl());
        fileUpload.setMimeType(request.getMimeType());
        fileUpload.setFileSize(request.getFileSize());
        fileUpload.setFileType(request.getFileType());
        fileUpload.setCategory(request.getCategory());
        fileUpload.setDescription(request.getDescription());
        fileUpload.setUploadedById(request.getUploadedById());
        fileUpload.setClientId(request.getClientId());
        fileUpload.setWorkoutPlanId(request.getWorkoutPlanId());
        fileUpload.setNutritionPlanId(request.getNutritionPlanId());
        fileUpload.setIsPublic(request.getIsPublic());
        fileUpload.setTags(request.getTags());
        fileUpload.setUploadedAt(LocalDateTime.now());
        fileUpload.setLastAccessedAt(LocalDateTime.now());
        fileUpload.setAccessCount(0);
        
        FileUpload savedFileUpload = fileUploadRepository.save(fileUpload);
        logger.info("File upload created successfully with ID: {}", savedFileUpload.getId());
        
        return convertToDTO(savedFileUpload);
    }

    // Update file upload
    public FileUploadDTO updateFileUpload(Long id, FileUploadRequestDTO request) {
        logger.info("Updating file upload: {}", id);
        
        Optional<FileUpload> fileUploadOpt = fileUploadRepository.findById(id);
        if (fileUploadOpt.isEmpty()) {
            throw new RuntimeException("File upload not found with ID: " + id);
        }
        
        FileUpload fileUpload = fileUploadOpt.get();
        fileUpload.setFileName(request.getFileName());
        fileUpload.setOriginalFileName(request.getOriginalFileName());
        fileUpload.setFilePath(request.getFilePath());
        fileUpload.setFileUrl(request.getFileUrl());
        fileUpload.setMimeType(request.getMimeType());
        fileUpload.setFileSize(request.getFileSize());
        fileUpload.setFileType(request.getFileType());
        fileUpload.setCategory(request.getCategory());
        fileUpload.setDescription(request.getDescription());
        fileUpload.setUploadedById(request.getUploadedById());
        fileUpload.setClientId(request.getClientId());
        fileUpload.setWorkoutPlanId(request.getWorkoutPlanId());
        fileUpload.setNutritionPlanId(request.getNutritionPlanId());
        fileUpload.setIsPublic(request.getIsPublic());
        fileUpload.setTags(request.getTags());
        
        FileUpload savedFileUpload = fileUploadRepository.save(fileUpload);
        logger.info("File upload updated successfully with ID: {}", savedFileUpload.getId());
        
        return convertToDTO(savedFileUpload);
    }

    // Delete file upload
    public void deleteFileUpload(Long id) {
        logger.info("Deleting file upload: {}", id);
        
        Optional<FileUpload> fileUploadOpt = fileUploadRepository.findById(id);
        if (fileUploadOpt.isEmpty()) {
            throw new RuntimeException("File upload not found with ID: " + id);
        }
        
        fileUploadRepository.deleteById(id);
        logger.info("File upload deleted successfully with ID: {}", id);
    }

    // Get file uploads by uploaded by user
    public List<FileUploadDTO> getFileUploadsByUploadedBy(Long uploadedById) {
        logger.info("Fetching file uploads uploaded by user: {}", uploadedById);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByUploadedBy_Id(uploadedById);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file uploads by client
    public List<FileUploadDTO> getFileUploadsByClient(Long clientId) {
        logger.info("Fetching file uploads for client: {}", clientId);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByClient_Id(clientId);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file uploads by workout plan
    public List<FileUploadDTO> getFileUploadsByWorkoutPlan(Long workoutPlanId) {
        logger.info("Fetching file uploads for workout plan: {}", workoutPlanId);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByWorkoutPlan_Id(workoutPlanId);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file uploads by nutrition plan
    public List<FileUploadDTO> getFileUploadsByNutritionPlan(Long nutritionPlanId) {
        logger.info("Fetching file uploads for nutrition plan: {}", nutritionPlanId);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByNutritionPlan_Id(nutritionPlanId);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file uploads by file type
    public List<FileUploadDTO> getFileUploadsByFileType(FileUpload.FileType fileType) {
        logger.info("Fetching file uploads by file type: {}", fileType);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByFileType(fileType);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file uploads by category
    public List<FileUploadDTO> getFileUploadsByCategory(FileUpload.FileCategory category) {
        logger.info("Fetching file uploads by category: {}", category);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByCategory(category);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get public file uploads
    public List<FileUploadDTO> getPublicFileUploads() {
        logger.info("Fetching public file uploads");
        
        List<FileUpload> fileUploads = fileUploadRepository.findByIsPublicTrue();
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get private file uploads
    public List<FileUploadDTO> getPrivateFileUploads() {
        logger.info("Fetching private file uploads");
        
        List<FileUpload> fileUploads = fileUploadRepository.findByIsPublicFalse();
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent file uploads
    public List<FileUploadDTO> getRecentFileUploads(int days) {
        logger.info("Fetching recent file uploads from last {} days", days);
        
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<FileUpload> fileUploads = fileUploadRepository.findByUploadedAtAfter(since);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get today's file uploads
    public List<FileUploadDTO> getTodaysFileUploads() {
        logger.info("Fetching today's file uploads");
        
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        List<FileUpload> fileUploads = fileUploadRepository.findByUploadedAtBetween(startOfDay, endOfDay);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this week's file uploads
    public List<FileUploadDTO> getThisWeeksFileUploads() {
        logger.info("Fetching this week's file uploads");
        
        LocalDateTime startOfWeek = LocalDateTime.now().minusDays(LocalDateTime.now().getDayOfWeek().getValue() - 1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfWeek = startOfWeek.plusDays(7);
        List<FileUpload> fileUploads = fileUploadRepository.findByUploadedAtBetween(startOfWeek, endOfWeek);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this month's file uploads
    public List<FileUploadDTO> getThisMonthsFileUploads() {
        logger.info("Fetching this month's file uploads");
        
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
        List<FileUpload> fileUploads = fileUploadRepository.findByUploadedAtBetween(startOfMonth, endOfMonth);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file uploads by date range
    public List<FileUploadDTO> getFileUploadsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Fetching file uploads from {} to {}", startDate, endDate);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByUploadedAtBetween(startDate, endDate);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file uploads by file size range
    public List<FileUploadDTO> getFileUploadsByFileSizeRange(Long minSize, Long maxSize) {
        logger.info("Fetching file uploads by file size range: {} to {}", minSize, maxSize);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByFileSizeBetween(minSize, maxSize);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get large file uploads
    public List<FileUploadDTO> getLargeFileUploads() {
        logger.info("Fetching large file uploads");
        
        Long minSize = 10L * 1024 * 1024; // 10MB
        List<FileUpload> fileUploads = fileUploadRepository.findByFileSizeGreaterThan(minSize);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get small file uploads
    public List<FileUploadDTO> getSmallFileUploads() {
        logger.info("Fetching small file uploads");
        
        Long maxSize = 1024L * 1024; // 1MB
        List<FileUpload> fileUploads = fileUploadRepository.findByFileSizeLessThan(maxSize);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file uploads by MIME type
    public List<FileUploadDTO> getFileUploadsByMimeType(String mimeType) {
        logger.info("Fetching file uploads by MIME type: {}", mimeType);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByMimeType(mimeType);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get file uploads by tags
    public List<FileUploadDTO> getFileUploadsByTags(String tags) {
        logger.info("Fetching file uploads by tags: {}", tags);
        
        List<FileUpload> fileUploads = fileUploadRepository.findByTagsContaining(tags);
        return fileUploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Update file access
    public FileUploadDTO updateFileAccess(Long id) {
        logger.info("Updating file access for file upload: {}", id);
        
        Optional<FileUpload> fileUploadOpt = fileUploadRepository.findById(id);
        if (fileUploadOpt.isEmpty()) {
            throw new RuntimeException("File upload not found with ID: " + id);
        }
        
        FileUpload fileUpload = fileUploadOpt.get();
        fileUpload.setLastAccessedAt(LocalDateTime.now());
        fileUpload.setAccessCount(fileUpload.getAccessCount() + 1);
        
        FileUpload savedFileUpload = fileUploadRepository.save(fileUpload);
        logger.info("File access updated successfully for ID: {}", savedFileUpload.getId());
        
        return convertToDTO(savedFileUpload);
    }

    // Get most accessed files
    public List<FileUploadDTO> getMostAccessedFiles(int limit) {
        logger.info("Fetching most accessed files (limit: {})", limit);
        
        Pageable pageable = PageRequest.of(0, limit, Sort.by("accessCount").descending());
        Page<FileUpload> fileUploads = fileUploadRepository.findAll(pageable);
        
        return fileUploads.getContent().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get total file uploads count
    public Long getTotalFileUploadsCount() {
        logger.info("Getting total file uploads count");
        return fileUploadRepository.count();
    }

    // Get total file size
    public Long getTotalFileSize() {
        logger.info("Getting total file size");
        return fileUploadRepository.getTotalFileSize();
    }

    // Convert entity to DTO
    private FileUploadDTO convertToDTO(FileUpload fileUpload) {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setId(fileUpload.getId());
        dto.setFileName(fileUpload.getFileName());
        dto.setOriginalFileName(fileUpload.getOriginalFileName());
        dto.setFilePath(fileUpload.getFilePath());
        dto.setFileUrl(fileUpload.getFileUrl());
        dto.setMimeType(fileUpload.getMimeType());
        dto.setFileSize(fileUpload.getFileSize());
        dto.setFileType(fileUpload.getFileType());
        dto.setCategory(fileUpload.getCategory());
        dto.setDescription(fileUpload.getDescription());
        dto.setUploadedById(fileUpload.getUploadedById());
        dto.setClientId(fileUpload.getClientId());
        dto.setWorkoutPlanId(fileUpload.getWorkoutPlanId());
        dto.setNutritionPlanId(fileUpload.getNutritionPlanId());
        dto.setIsPublic(fileUpload.getIsPublic());
        dto.setTags(fileUpload.getTags());
        dto.setUploadedAt(fileUpload.getUploadedAt());
        dto.setLastAccessedAt(fileUpload.getLastAccessedAt());
        dto.setAccessCount(fileUpload.getAccessCount());
        
        // Set uploaded by name if available
        if (fileUpload.getUploadedById() != null) {
            Optional<User> uploadedByOpt = userRepository.findById(fileUpload.getUploadedById());
            if (uploadedByOpt.isPresent()) {
                dto.setUploadedByName(uploadedByOpt.get().getFirstName() + " " + uploadedByOpt.get().getLastName());
            }
        }
        
        // Set client name if available
        if (fileUpload.getClientId() != null) {
            Optional<User> clientOpt = userRepository.findById(fileUpload.getClientId());
            if (clientOpt.isPresent()) {
                dto.setClientName(clientOpt.get().getFirstName() + " " + clientOpt.get().getLastName());
            }
        }
        
        // Set workout plan name if available
        if (fileUpload.getWorkoutPlanId() != null) {
            Optional<WorkoutPlan> workoutPlanOpt = workoutPlanRepository.findById(fileUpload.getWorkoutPlanId());
            if (workoutPlanOpt.isPresent()) {
                dto.setWorkoutPlanName(workoutPlanOpt.get().getName());
            }
        }
        
        // Set nutrition plan name if available
        if (fileUpload.getNutritionPlanId() != null) {
            Optional<NutritionPlan> nutritionPlanOpt = nutritionPlanRepository.findById(fileUpload.getNutritionPlanId());
            if (nutritionPlanOpt.isPresent()) {
                dto.setNutritionPlanName(nutritionPlanOpt.get().getName());
            }
        }
        
        return dto;
    }

    // Additional methods needed by FileUploadController
    public FileUploadDTO uploadFile(org.springframework.web.multipart.MultipartFile file, Long userId, String fileType, Long entityId) {
        logger.info("Uploading file for user: {}, type: {}, entityId: {}", userId, fileType, entityId);
        
        try {
            // This is a simplified implementation - you might want to add actual file handling logic
            FileUploadRequestDTO request = new FileUploadRequestDTO();
            request.setUserId(userId);
            request.setFileName(file.getOriginalFilename());
            request.setFileType(FileUpload.FileType.valueOf(fileType.toUpperCase()));
            request.setFileSize(file.getSize());
            
            return createFileUpload(request);
        } catch (Exception e) {
            logger.error("Error uploading file: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading file: " + e.getMessage());
        }
    }

    public List<FileUploadDTO> getFileUploadsByUser(Long userId) {
        logger.info("Fetching file uploads for user: {}", userId);
        
        List<FileUpload> uploads = fileUploadRepository.findByUploadedBy_Id(userId);
        return uploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FileUploadDTO> getFileUploadsByEntity(String entityType, Long entityId) {
        logger.info("Fetching file uploads for entity: {} with ID: {}", entityType, entityId);
        
        List<FileUpload> uploads = fileUploadRepository.findByEntityTypeAndEntityId(entityType, entityId);
        return uploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FileUploadDTO> getFileUploadsByType(String fileType) {
        logger.info("Fetching file uploads by type: {}", fileType);
        
        List<FileUpload> uploads = fileUploadRepository.findByFileType(fileType);
        return uploads.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Additional method needed by FileUploadController
    public byte[] downloadFile(Long fileId) {
        logger.info("Downloading file with ID: {}", fileId);
        
        Optional<FileUpload> fileOpt = fileUploadRepository.findById(fileId);
        if (fileOpt.isPresent()) {
            FileUpload file = fileOpt.get();
            // Update access count and last accessed time
            file.setLastAccessedAt(LocalDateTime.now());
            file.setAccessCount(file.getAccessCount() + 1);
            fileUploadRepository.save(file);
            
            // This is a simplified implementation - you might want to add actual file reading logic
            // For now, return empty byte array
            return new byte[0];
        }
        
        throw new RuntimeException("File not found with ID: " + fileId);
    }
}
