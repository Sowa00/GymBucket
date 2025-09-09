package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.FileUploadDTO;
import gym.backend.dto.FileUploadRequestDTO;
import gym.backend.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/file-uploads")
@CrossOrigin(origins = "http://localhost:4200")
public class FileUploadController {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    
    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllFileUploads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Page<FileUploadDTO> fileUploads = fileUploadService.getAllFileUploads(page, size, sortBy, sortDir);
            return ResponseEntity.ok(new ApiResponseDTO(true, "File uploads fetched successfully", fileUploads));
        } catch (Exception e) {
            logger.error("Error fetching file uploads", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching file uploads: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllFileUploadsList() {
        try {
            List<FileUploadDTO> fileUploads = fileUploadService.getAllFileUploadsList();
            return ResponseEntity.ok(new ApiResponseDTO(true, "File uploads fetched successfully", fileUploads));
        } catch (Exception e) {
            logger.error("Error fetching file uploads list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching file uploads: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getFileUploadById(@PathVariable Long id) {
        try {
            Optional<FileUploadDTO> fileUpload = fileUploadService.getFileUploadById(id);
            if (fileUpload.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "File upload fetched successfully", fileUpload.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "File upload not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching file upload by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching file upload: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createFileUpload(@RequestBody FileUploadRequestDTO request) {
        try {
            FileUploadDTO fileUpload = fileUploadService.createFileUpload(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "File upload created successfully", fileUpload));
        } catch (Exception e) {
            logger.error("Error creating file upload", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating file upload: " + e.getMessage(), null));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponseDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("entityType") String entityType,
            @RequestParam("entityId") Long entityId) {
        
        try {
            FileUploadDTO fileUpload = fileUploadService.uploadFile(file, userId, entityType, entityId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "File uploaded successfully", fileUpload));
        } catch (Exception e) {
            logger.error("Error uploading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error uploading file: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateFileUpload(
            @PathVariable Long id,
            @RequestBody FileUploadRequestDTO request) {
        
        try {
            FileUploadDTO fileUpload = fileUploadService.updateFileUpload(id, request);
            return ResponseEntity.ok(new ApiResponseDTO(true, "File upload updated successfully", fileUpload));
        } catch (Exception e) {
            logger.error("Error updating file upload: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating file upload: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteFileUpload(@PathVariable Long id) {
        try {
            fileUploadService.deleteFileUpload(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "File upload deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting file upload: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting file upload: " + e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO> getFileUploadsByUser(@PathVariable Long userId) {
        try {
            List<FileUploadDTO> fileUploads = fileUploadService.getFileUploadsByUser(userId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "File uploads fetched successfully", fileUploads));
        } catch (Exception e) {
            logger.error("Error fetching file uploads for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching file uploads: " + e.getMessage(), null));
        }
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<ApiResponseDTO> getFileUploadsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        
        try {
            List<FileUploadDTO> fileUploads = fileUploadService.getFileUploadsByEntity(entityType, entityId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "File uploads fetched successfully", fileUploads));
        } catch (Exception e) {
            logger.error("Error fetching file uploads for entity: {} with ID: {}", entityType, entityId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching file uploads: " + e.getMessage(), null));
        }
    }

    @GetMapping("/type/{fileType}")
    public ResponseEntity<ApiResponseDTO> getFileUploadsByType(@PathVariable String fileType) {
        try {
            List<FileUploadDTO> fileUploads = fileUploadService.getFileUploadsByType(fileType);
            return ResponseEntity.ok(new ApiResponseDTO(true, "File uploads fetched successfully", fileUploads));
        } catch (Exception e) {
            logger.error("Error fetching file uploads for type: {}", fileType, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching file uploads: " + e.getMessage(), null));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponseDTO> getRecentFileUploads(
            @RequestParam(defaultValue = "24") int hours) {
        
        try {
            List<FileUploadDTO> fileUploads = fileUploadService.getRecentFileUploads(hours);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Recent file uploads fetched successfully", fileUploads));
        } catch (Exception e) {
            logger.error("Error fetching recent file uploads", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching recent file uploads: " + e.getMessage(), null));
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        try {
            byte[] fileContent = fileUploadService.downloadFile(id);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"file\"")
                    .body(fileContent);
        } catch (Exception e) {
            logger.error("Error downloading file: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
