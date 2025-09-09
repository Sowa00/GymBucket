package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.AuditLogDTO;
import gym.backend.dto.AuditLogRequestDTO;
import gym.backend.model.AuditLog;
import gym.backend.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "http://localhost:4200")
public class AuditLogController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditLogController.class);
    
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Page<AuditLogDTO> auditLogs = auditLogService.getAllAuditLogs(page, size, sortBy, sortDir);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Audit logs fetched successfully", auditLogs));
        } catch (Exception e) {
            logger.error("Error fetching audit logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching audit logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllAuditLogsList() {
        try {
            List<AuditLogDTO> auditLogs = auditLogService.getAllAuditLogsList();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Audit logs fetched successfully", auditLogs));
        } catch (Exception e) {
            logger.error("Error fetching audit logs list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching audit logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getAuditLogById(@PathVariable Long id) {
        try {
            Optional<AuditLogDTO> auditLog = auditLogService.getAuditLogById(id);
            if (auditLog.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Audit log fetched successfully", auditLog.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Audit log not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching audit log by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching audit log: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createAuditLog(@RequestBody AuditLogRequestDTO request) {
        try {
            AuditLogDTO auditLog = auditLogService.createAuditLog(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Audit log created successfully", auditLog));
        } catch (Exception e) {
            logger.error("Error creating audit log", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating audit log: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateAuditLog(
            @PathVariable Long id,
            @RequestBody AuditLogRequestDTO request) {
        
        try {
            AuditLogDTO auditLog = auditLogService.updateAuditLog(id, request);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Audit log updated successfully", auditLog));
        } catch (Exception e) {
            logger.error("Error updating audit log: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating audit log: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteAuditLog(@PathVariable Long id) {
        try {
            auditLogService.deleteAuditLog(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Audit log deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting audit log: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting audit log: " + e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO> getAuditLogsByUser(@PathVariable Long userId) {
        try {
            List<AuditLogDTO> auditLogs = auditLogService.getAuditLogsByUser(userId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Audit logs fetched successfully", auditLogs));
        } catch (Exception e) {
            logger.error("Error fetching audit logs for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching audit logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/action/{actionType}")
    public ResponseEntity<ApiResponseDTO> getAuditLogsByActionType(@PathVariable String actionType) {
        try {
            AuditLog.ActionType actionTypeEnum = AuditLog.ActionType.valueOf(actionType.toUpperCase());
            List<AuditLogDTO> auditLogs = auditLogService.getAuditLogsByActionType(actionTypeEnum);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Audit logs fetched successfully", auditLogs));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid action type: {}", actionType, e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponseDTO(false, "Invalid action type: " + actionType, null));
        } catch (Exception e) {
            logger.error("Error fetching audit logs for action type: {}", actionType, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching audit logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/entity/{entityType}")
    public ResponseEntity<ApiResponseDTO> getAuditLogsByEntityType(@PathVariable String entityType) {
        try {
            List<AuditLogDTO> auditLogs = auditLogService.getAuditLogsByEntityType(entityType);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Audit logs fetched successfully", auditLogs));
        } catch (Exception e) {
            logger.error("Error fetching audit logs for entity type: {}", entityType, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching audit logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponseDTO> getRecentAuditLogs(
            @RequestParam(defaultValue = "24") int hours) {
        
        try {
            List<AuditLogDTO> auditLogs = auditLogService.getRecentAuditLogs(hours);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Recent audit logs fetched successfully", auditLogs));
        } catch (Exception e) {
            logger.error("Error fetching recent audit logs", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching recent audit logs: " + e.getMessage(), null));
        }
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<ApiResponseDTO> getAuditLogsByEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        
        try {
            List<AuditLogDTO> auditLogs = auditLogService.getAuditLogsByEntity(entityType, entityId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Audit logs fetched successfully", auditLogs));
        } catch (Exception e) {
            logger.error("Error fetching audit logs for entity: {} with ID: {}", entityType, entityId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching audit logs: " + e.getMessage(), null));
        }
    }
}
