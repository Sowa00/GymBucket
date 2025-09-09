package gym.backend.controller;

import gym.backend.dto.ApiResponseDTO;
import gym.backend.dto.ClientDTO;
import gym.backend.dto.ClientRequestDTO;
import gym.backend.service.ClientService;
import gym.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = {"http://localhost:4200", "https://gymbucket.com"})
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;
    private final UserRepository userRepository;

    @Autowired
    public ClientController(ClientService clientService, UserRepository userRepository) {
        this.clientService = clientService;
        this.userRepository = userRepository;
        logger.info("ClientController initialized");
    }

    // Create a new client
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientRequestDTO requestDTO) {
        try {
            Long trainerId = getCurrentUserId();
            logger.info("Creating new client for trainer: {}", trainerId);
            ClientDTO client = clientService.createClient(requestDTO, trainerId);
            logger.info("Client created successfully with id: {}", client.getId());
            return ResponseEntity.ok(client);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating client: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error creating client: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas tworzenia klienta"));
        }
    }

    // Get client by ID
    @GetMapping("/{clientId}")
    public ResponseEntity<?> getClientById(@PathVariable Long clientId) {
        try {
            Long trainerId = getCurrentUserId();
            logger.debug("Getting client by id: {} for trainer: {}", clientId, trainerId);
            ClientDTO client = clientService.getClientById(clientId, trainerId);
            return ResponseEntity.ok(client);
        } catch (IllegalArgumentException e) {
            logger.error("Error getting client: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error getting client: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas pobierania klienta"));
        }
    }

    // Get all clients for a trainer
    @GetMapping
    public ResponseEntity<?> getAllClients(@RequestParam(required = false) Boolean active,
                                         @RequestParam(required = false) String search,
                                         Pageable pageable) {
        try {
            Long trainerId = getCurrentUserId();
            logger.debug("Getting clients for trainer: {}", trainerId);

            if (active != null && active) {
                // Return only active clients
                List<ClientDTO> clients = clientService.getActiveClientsByTrainer(trainerId);
                return ResponseEntity.ok(clients);
            } else if (search != null && !search.trim().isEmpty()) {
                // Search clients by name
                List<ClientDTO> clients = clientService.searchClientsByName(trainerId, search.trim());
                return ResponseEntity.ok(clients);
            } else if (pageable.getPageSize() > 0) {
                // Return paginated results
                Page<ClientDTO> clients = clientService.getClientsByTrainer(trainerId, pageable);
                return ResponseEntity.ok(clients);
            } else {
                // Return all clients
                List<ClientDTO> clients = clientService.getAllClientsByTrainer(trainerId);
                return ResponseEntity.ok(clients);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error getting clients: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error getting clients: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas pobierania klientów"));
        }
    }

    // Update client
    @PutMapping("/{clientId}")
    public ResponseEntity<?> updateClient(@PathVariable Long clientId,
                                        @RequestBody ClientRequestDTO requestDTO) {
        try {
            Long trainerId = getCurrentUserId();
            logger.info("Updating client: {} for trainer: {}", clientId, trainerId);
            ClientDTO client = clientService.updateClient(clientId, requestDTO, trainerId);
            logger.info("Client updated successfully with id: {}", client.getId());
            return ResponseEntity.ok(client);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating client: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error updating client: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas aktualizacji klienta"));
        }
    }

    // Delete client (soft delete)
    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deleteClient(@PathVariable Long clientId) {
        try {
            Long trainerId = getCurrentUserId();
            logger.info("Deleting client: {} for trainer: {}", clientId, trainerId);
            clientService.deleteClient(clientId, trainerId);
            logger.info("Client deleted successfully with id: {}", clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Klient został usunięty pomyślnie"));
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting client: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error deleting client: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas usuwania klienta"));
        }
    }

    // Permanently delete client
    @DeleteMapping("/{clientId}/permanent")
    public ResponseEntity<?> permanentlyDeleteClient(@PathVariable Long clientId) {
        try {
            Long trainerId = getCurrentUserId();
            logger.info("Permanently deleting client: {} for trainer: {}", clientId, trainerId);
            clientService.permanentlyDeleteClient(clientId, trainerId);
            logger.info("Client permanently deleted with id: {}", clientId);
            return ResponseEntity.ok(new ApiResponseDTO(true, "Klient został trwale usunięty"));
        } catch (IllegalArgumentException e) {
            logger.error("Error permanently deleting client: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error permanently deleting client: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas trwałego usuwania klienta"));
        }
    }

    // Get client statistics
    @GetMapping("/stats")
    public ResponseEntity<?> getClientStats() {
        try {
            Long trainerId = getCurrentUserId();
            logger.debug("Getting client statistics for trainer: {}", trainerId);
            ClientService.ClientStatsDTO stats = clientService.getClientStats(trainerId);
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            logger.error("Error getting client stats: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDTO(false, e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error getting client stats: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ApiResponseDTO(false, "Wystąpił nieoczekiwany błąd podczas pobierania statystyk"));
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(new ApiResponseDTO(true, "Client service is running"));
    }

    // Helper method to get current user ID from security context
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            String email = (String) authentication.getPrincipal();
            return userRepository.findByEmail(email)
                    .map(user -> user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        }
        throw new RuntimeException("User not authenticated");
    }
}
