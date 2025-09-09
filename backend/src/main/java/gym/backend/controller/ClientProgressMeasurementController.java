package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.ClientProgressMeasurementDTO;
import gym.backend.dto.ClientProgressMeasurementRequestDTO;
import gym.backend.service.ClientProgressMeasurementService;
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
@RequestMapping("/api/client-progress")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientProgressMeasurementController {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientProgressMeasurementController.class);
    
    @Autowired
    private ClientProgressMeasurementService progressService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllProgressMeasurements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Page<ClientProgressMeasurementDTO> measurements = progressService.getAllProgressMeasurements(page, size, sortBy, sortDir);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Progress measurements fetched successfully", measurements));
        } catch (Exception e) {
            logger.error("Error fetching progress measurements", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching progress measurements: " + e.getMessage(), null));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO> getAllProgressMeasurementsList() {
        try {
            List<ClientProgressMeasurementDTO> measurements = progressService.getAllProgressMeasurementsList();
            return ResponseEntity.ok(new ApiResponseDTO(true, "Progress measurements fetched successfully", measurements));
        } catch (Exception e) {
            logger.error("Error fetching progress measurements list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching progress measurements: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getProgressMeasurementById(@PathVariable Long id) {
        try {
            Optional<ClientProgressMeasurementDTO> measurement = progressService.getProgressMeasurementById(id);
            if (measurement.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Progress measurement fetched successfully", measurement.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "Progress measurement not found", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching progress measurement by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching progress measurement: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createProgressMeasurement(@RequestBody ClientProgressMeasurementRequestDTO request) {
        try {
            ClientProgressMeasurementDTO measurement = progressService.createProgressMeasurement(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDTO(true, "Progress measurement created successfully", measurement));
        } catch (Exception e) {
            logger.error("Error creating progress measurement", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error creating progress measurement: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateProgressMeasurement(
            @PathVariable Long id,
            @RequestBody ClientProgressMeasurementRequestDTO request) {
        
        try {
            ClientProgressMeasurementDTO measurement = progressService.updateProgressMeasurement(id, request);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Progress measurement updated successfully", measurement));
        } catch (Exception e) {
            logger.error("Error updating progress measurement: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error updating progress measurement: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteProgressMeasurement(@PathVariable Long id) {
        try {
            progressService.deleteProgressMeasurement(id);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Progress measurement deleted successfully", null));
        } catch (Exception e) {
            logger.error("Error deleting progress measurement: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error deleting progress measurement: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponseDTO> getProgressMeasurementsByClient(@PathVariable Long clientId) {
        try {
            List<ClientProgressMeasurementDTO> measurements = progressService.getProgressMeasurementsByClient(clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Progress measurements fetched successfully", measurements));
        } catch (Exception e) {
            logger.error("Error fetching progress measurements for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching progress measurements: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}/latest")
    public ResponseEntity<ApiResponseDTO> getLatestProgressMeasurementByClient(@PathVariable Long clientId) {
        try {
            Optional<ClientProgressMeasurementDTO> measurement = progressService.getLatestProgressMeasurementByClient(clientId);
            if (measurement.isPresent()) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Latest progress measurement fetched successfully", measurement.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponseDTO(false, "No progress measurements found for client", null));
            }
        } catch (Exception e) {
            logger.error("Error fetching latest progress measurement for client: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching latest progress measurement: " + e.getMessage(), null));
        }
    }

    @GetMapping("/client/{clientId}/type/{type}")
    public ResponseEntity<ApiResponseDTO> getProgressMeasurementsByClientAndType(
            @PathVariable Long clientId,
            @PathVariable String type) {
        
        try {
            List<ClientProgressMeasurementDTO> measurements = progressService.getProgressMeasurementsByClientAndType(clientId, type);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Progress measurements fetched successfully", measurements));
        } catch (Exception e) {
            logger.error("Error fetching progress measurements for client: {} and type: {}", clientId, type, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDTO(false, "Error fetching progress measurements: " + e.getMessage(), null));
        }
    }
}
