package gym.backend.service;

import gym.backend.dto.ClientDTO;
import gym.backend.dto.ClientRequestDTO;
import gym.backend.model.Client;
import gym.backend.model.User;
import gym.backend.repository.ClientRepository;
import gym.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    // Create a new client
    public ClientDTO createClient(ClientRequestDTO requestDTO, Long trainerId) {
        logger.info("Creating new client for trainer: {}", trainerId);

        // Validate input
        if (requestDTO.getFirstName() == null || requestDTO.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (requestDTO.getLastName() == null || requestDTO.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (requestDTO.getEmail() == null || requestDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!isValidEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        // Check if email already exists among active clients
        if (clientRepository.existsByEmailAndIsActive(requestDTO.getEmail(), true)) {
            throw new IllegalArgumentException("Client with email " + requestDTO.getEmail() + " already exists");
        }
        
        // Check if email exists among inactive clients (soft deleted)
        Optional<Client> existingInactiveClient = clientRepository.findByEmailAndIsActive(requestDTO.getEmail(), false);
        if (existingInactiveClient.isPresent()) {
            // Reactivate the existing client instead of creating a new one
            Client client = existingInactiveClient.get();
            client.setFirstName(requestDTO.getFirstName());
            client.setLastName(requestDTO.getLastName());
            client.setPhone(requestDTO.getPhone());
            client.setDateOfBirth(requestDTO.getDateOfBirth());
            client.setGender(requestDTO.getGender());
            client.setHeight(requestDTO.getHeight());
            client.setWeight(requestDTO.getWeight());
            client.setMedicalConditions(requestDTO.getMedicalConditions());
            client.setFitnessGoals(requestDTO.getFitnessGoals());
            client.setNotes(requestDTO.getNotes());
            client.setMonthlyFee(requestDTO.getMonthlyFee());
            client.setPaymentStatus(requestDTO.getPaymentStatus() != null ? requestDTO.getPaymentStatus() : "PENDING");
            client.setIsActive(true);
            client.setTrainer(trainer);
            client.setStartDate(LocalDate.now());
            
            // Convert List to Set for emergency contacts
            if (requestDTO.getEmergencyContacts() != null && !requestDTO.getEmergencyContacts().isEmpty()) {
                client.setEmergencyContacts(new HashSet<>(requestDTO.getEmergencyContacts()));
            } else {
                client.setEmergencyContacts(new HashSet<>());
            }
            
            Client savedClient = clientRepository.save(client);
            logger.info("Client reactivated successfully with id: {}", savedClient.getId());
            return convertToDTO(savedClient);
        }

        Client client = new Client();
        client.setFirstName(requestDTO.getFirstName());
        client.setLastName(requestDTO.getLastName());
        client.setEmail(requestDTO.getEmail());
        client.setPhone(requestDTO.getPhone());
        client.setDateOfBirth(requestDTO.getDateOfBirth());
        client.setGender(requestDTO.getGender());
        client.setHeight(requestDTO.getHeight());
        client.setWeight(requestDTO.getWeight());
        client.setMedicalConditions(requestDTO.getMedicalConditions());
        client.setFitnessGoals(requestDTO.getFitnessGoals());
        client.setNotes(requestDTO.getNotes());
        client.setMonthlyFee(requestDTO.getMonthlyFee());
        client.setPaymentStatus(requestDTO.getPaymentStatus() != null ? requestDTO.getPaymentStatus() : "PENDING");
        // Convert List to Set for emergency contacts
        if (requestDTO.getEmergencyContacts() != null && !requestDTO.getEmergencyContacts().isEmpty()) {
            client.setEmergencyContacts(new HashSet<>(requestDTO.getEmergencyContacts()));
        } else {
            client.setEmergencyContacts(new HashSet<>());
        }
        client.setTrainer(trainer);
        client.setStartDate(LocalDate.now());

        Client savedClient = clientRepository.save(client);
        logger.info("Client created successfully with id: {}", savedClient.getId());

        return convertToDTO(savedClient);
    }

    // Get client by ID
    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long clientId, Long trainerId) {
        logger.debug("Getting client by id: {} for trainer: {}", clientId, trainerId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));

        // Verify trainer owns this client
        if (!client.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Client does not belong to trainer");
        }

        return convertToDTO(client);
    }

    // Get all clients for a trainer
    @Transactional(readOnly = true)
    public List<ClientDTO> getAllClientsByTrainer(Long trainerId) {
        logger.debug("Getting all clients for trainer: {}", trainerId);

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        List<Client> clients = clientRepository.findByTrainer(trainer);
        return clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get clients with pagination
    @Transactional(readOnly = true)
    public Page<ClientDTO> getClientsByTrainer(Long trainerId, Pageable pageable) {
        logger.debug("Getting clients for trainer: {} with pagination", trainerId);

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        Page<Client> clients = clientRepository.findByTrainer(trainer, pageable);
        return clients.map(this::convertToDTO);
    }

    // Get active clients only
    @Transactional(readOnly = true)
    public List<ClientDTO> getActiveClientsByTrainer(Long trainerId) {
        logger.debug("Getting active clients for trainer: {}", trainerId);

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        List<Client> clients = clientRepository.findByTrainerAndIsActive(trainer, true);
        return clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Search clients by name
    @Transactional(readOnly = true)
    public List<ClientDTO> searchClientsByName(Long trainerId, String searchTerm) {
        logger.debug("Searching clients by name: {} for trainer: {}", searchTerm, trainerId);

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        List<Client> clients = clientRepository.findByTrainerAndNameContaining(trainer, searchTerm);
        return clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Update client
    public ClientDTO updateClient(Long clientId, ClientRequestDTO requestDTO, Long trainerId) {
        logger.info("Updating client: {} for trainer: {}", clientId, trainerId);

        // Validate request DTO
        if (requestDTO == null) {
            throw new IllegalArgumentException("Request DTO cannot be null");
        }

        // Validate input
        if (requestDTO.getFirstName() == null || requestDTO.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (requestDTO.getLastName() == null || requestDTO.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (requestDTO.getEmail() == null || requestDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!isValidEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        Client client = clientRepository.findByIdWithTrainer(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));

        // Verify trainer owns this client
        if (!client.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Client does not belong to trainer");
        }

        // Check if email is being changed and if it already exists among active clients
        if (!client.getEmail().equals(requestDTO.getEmail()) && 
            clientRepository.existsByEmailAndIsActive(requestDTO.getEmail(), true)) {
            throw new IllegalArgumentException("Client with email " + requestDTO.getEmail() + " already exists");
        }

        // Update fields
        logger.debug("Setting client fields - firstName: {}", requestDTO.getFirstName());
        client.setFirstName(requestDTO.getFirstName());
        logger.debug("Setting client fields - lastName: {}", requestDTO.getLastName());
        client.setLastName(requestDTO.getLastName());
        logger.debug("Setting client fields - email: {}", requestDTO.getEmail());
        client.setEmail(requestDTO.getEmail());
        logger.debug("Setting client fields - phone: {}", requestDTO.getPhone());
        client.setPhone(requestDTO.getPhone());
        logger.debug("Setting client fields - dateOfBirth: {}", requestDTO.getDateOfBirth());
        client.setDateOfBirth(requestDTO.getDateOfBirth());
        logger.debug("Setting client fields - gender: {}", requestDTO.getGender());
        client.setGender(requestDTO.getGender());
        logger.debug("Setting client fields - height: {}", requestDTO.getHeight());
        client.setHeight(requestDTO.getHeight());
        logger.debug("Setting client fields - weight: {}", requestDTO.getWeight());
        client.setWeight(requestDTO.getWeight());
        logger.debug("Setting client fields - medicalConditions: {}", requestDTO.getMedicalConditions());
        client.setMedicalConditions(requestDTO.getMedicalConditions());
        logger.debug("Setting client fields - fitnessGoals: {}", requestDTO.getFitnessGoals());
        client.setFitnessGoals(requestDTO.getFitnessGoals());
        logger.debug("Setting client fields - notes: {}", requestDTO.getNotes());
        client.setNotes(requestDTO.getNotes());
        logger.debug("Setting client fields - monthlyFee: {}", requestDTO.getMonthlyFee());
        client.setMonthlyFee(requestDTO.getMonthlyFee());
        if (requestDTO.getPaymentStatus() != null) {
            client.setPaymentStatus(requestDTO.getPaymentStatus());
        }
        // Convert List to Set for emergency contacts
        if (requestDTO.getEmergencyContacts() != null && !requestDTO.getEmergencyContacts().isEmpty()) {
            client.setEmergencyContacts(new HashSet<>(requestDTO.getEmergencyContacts()));
        } else {
            client.setEmergencyContacts(new HashSet<>());
        }

        logger.debug("About to save client with id: {}", client.getId());
        logger.debug("Client trainer: {}", client.getTrainer() != null ? client.getTrainer().getId() : "null");
        logger.debug("Client isActive: {}", client.getIsActive());
        logger.debug("Client updatedAt: {}", client.getUpdatedAt());
        
        Client updatedClient;
        try {
            updatedClient = clientRepository.save(client);
            logger.info("Client updated successfully with id: {}", updatedClient.getId());
        } catch (Exception e) {
            logger.error("Error saving client: {}", e.getMessage(), e);
            throw e;
        }

        // Fetch the updated client with trainer relationship for DTO conversion
        Client clientWithTrainer = clientRepository.findByIdWithTrainer(updatedClient.getId())
                .orElse(updatedClient);
        
        return convertToDTO(clientWithTrainer);
    }

    // Delete client (soft delete by setting isActive to false)
    public void deleteClient(Long clientId, Long trainerId) {
        logger.info("Deleting client: {} for trainer: {}", clientId, trainerId);

        Client client = clientRepository.findByIdWithTrainer(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));

        // Verify trainer owns this client
        if (!client.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Client does not belong to trainer");
        }

        client.setIsActive(false);
        clientRepository.save(client);
        logger.info("Client deactivated successfully with id: {}", clientId);
    }

    // Permanently delete client
    public void permanentlyDeleteClient(Long clientId, Long trainerId) {
        logger.info("Permanently deleting client: {} for trainer: {}", clientId, trainerId);

        Client client = clientRepository.findByIdWithTrainer(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));

        // Verify trainer owns this client
        if (!client.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Client does not belong to trainer");
        }

        clientRepository.delete(client);
        logger.info("Client permanently deleted with id: {}", clientId);
    }

    // Get client statistics
    @Transactional(readOnly = true)
    public ClientStatsDTO getClientStats(Long trainerId) {
        logger.debug("Getting client statistics for trainer: {}", trainerId);

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        long totalClients = clientRepository.countByTrainer(trainer);
        long activeClients = clientRepository.countByTrainerAndIsActive(trainer, true);
        Double averageSessions = clientRepository.getAverageSessionsByTrainer(trainer);
        Double totalMonthlyRevenue = clientRepository.getTotalMonthlyRevenueByTrainer(trainer);
        Long paidClients = clientRepository.countPaidClientsByTrainer(trainer);
        Long overdueClients = clientRepository.countOverdueClientsByTrainer(trainer);

        return new ClientStatsDTO(
                totalClients,
                activeClients,
                averageSessions != null ? averageSessions : 0.0,
                totalMonthlyRevenue != null ? totalMonthlyRevenue : 0.0,
                paidClients,
                overdueClients
        );
    }

    // Convert Client entity to DTO
    private ClientDTO convertToDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        dto.setDateOfBirth(client.getDateOfBirth());
        dto.setGender(client.getGender());
        dto.setHeight(client.getHeight());
        dto.setWeight(client.getWeight());
        dto.setMedicalConditions(client.getMedicalConditions());
        dto.setFitnessGoals(client.getFitnessGoals());
        dto.setNotes(client.getNotes());
        dto.setIsActive(client.getIsActive());
        if (client.getTrainer() != null) {
            dto.setTrainerId(client.getTrainer().getId());
            dto.setTrainerName(client.getTrainer().getFullName());
        }
        dto.setStartDate(client.getStartDate());
        dto.setLastSessionDate(client.getLastSessionDate());
        dto.setTotalSessions(client.getTotalSessions());
        dto.setMonthlyFee(client.getMonthlyFee());
        dto.setPaymentStatus(client.getPaymentStatus());
        // Convert Set to List for DTO
        if (client.getEmergencyContacts() != null) {
            dto.setEmergencyContacts(client.getEmergencyContacts());
        } else {
            dto.setEmergencyContacts(new HashSet<>());
        }
        dto.setCreatedAt(client.getCreatedAt());
        dto.setUpdatedAt(client.getUpdatedAt());

        // Set computed fields
        dto.setFullName(client.getFullName());
        dto.setAge(client.getAge());
        dto.setBmi(client.getBMI());

        return dto;
    }

    // Inner class for client statistics
    public static class ClientStatsDTO {
        private long totalClients;
        private long activeClients;
        private double averageSessions;
        private double totalMonthlyRevenue;
        private long paidClients;
        private long overdueClients;

        public ClientStatsDTO(long totalClients, long activeClients, double averageSessions, 
                             double totalMonthlyRevenue, long paidClients, long overdueClients) {
            this.totalClients = totalClients;
            this.activeClients = activeClients;
            this.averageSessions = averageSessions;
            this.totalMonthlyRevenue = totalMonthlyRevenue;
            this.paidClients = paidClients;
            this.overdueClients = overdueClients;
        }

        // Getters
        public long getTotalClients() { return totalClients; }
        public long getActiveClients() { return activeClients; }
        public double getAverageSessions() { return averageSessions; }
        public double getTotalMonthlyRevenue() { return totalMonthlyRevenue; }
        public long getPaidClients() { return paidClients; }
        public long getOverdueClients() { return overdueClients; }
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
