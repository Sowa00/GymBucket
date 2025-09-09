package gym.backend.service;

import gym.backend.dto.ClientProgressMeasurementDTO;
import gym.backend.dto.ClientProgressMeasurementRequestDTO;
import gym.backend.model.ClientProgressMeasurement;
import gym.backend.model.User;
import gym.backend.repository.ClientProgressMeasurementRepository;
import gym.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientProgressMeasurementService {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientProgressMeasurementService.class);
    
    @Autowired
    private ClientProgressMeasurementRepository progressMeasurementRepository;
    
    @Autowired
    private UserRepository userRepository;

    // Get all progress measurements with pagination
    public Page<ClientProgressMeasurementDTO> getAllProgressMeasurements(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all progress measurements with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}", 
                   page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClientProgressMeasurement> measurements = progressMeasurementRepository.findAll(pageable);
        
        return measurements.map(this::convertToDTO);
    }

    // Get all progress measurements as list
    public List<ClientProgressMeasurementDTO> getAllProgressMeasurementsList() {
        logger.info("Fetching all progress measurements as list");
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findAll();
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get progress measurement by ID
    public Optional<ClientProgressMeasurementDTO> getProgressMeasurementById(Long id) {
        logger.info("Fetching progress measurement by ID: {}", id);
        return progressMeasurementRepository.findById(id).map(this::convertToDTO);
    }

    // Create new progress measurement
    public ClientProgressMeasurementDTO createProgressMeasurement(ClientProgressMeasurementRequestDTO request) {
        logger.info("Creating new progress measurement for client: {} on date: {}", 
                   request.getClientId(), request.getMeasurementDate());
        
        Optional<User> clientOpt = userRepository.findById(request.getClientId());
        if (clientOpt.isEmpty()) {
            throw new RuntimeException("Client not found with ID: " + request.getClientId());
        }
        
        Optional<User> measuredByOpt = userRepository.findById(request.getMeasuredById());
        if (measuredByOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + request.getMeasuredById());
        }
        
        ClientProgressMeasurement measurement = new ClientProgressMeasurement();
        measurement.setClientId(request.getClientId());
        measurement.setMeasurementDate(request.getMeasurementDate());
        measurement.setWeight(request.getWeight());
        measurement.setBodyFatPercentage(request.getBodyFatPercentage());
        measurement.setMuscleMass(request.getMuscleMass());
        measurement.setMeasurements(request.getMeasurements());
        measurement.setPhotos(request.getPhotos());
        measurement.setNotes(request.getNotes());
        measurement.setMeasuredById(request.getMeasuredById());
        measurement.setCreatedAt(LocalDateTime.now());
        measurement.setUpdatedAt(LocalDateTime.now());
        
        ClientProgressMeasurement savedMeasurement = progressMeasurementRepository.save(measurement);
        logger.info("Progress measurement created successfully with ID: {}", savedMeasurement.getId());
        
        return convertToDTO(savedMeasurement);
    }

    // Update progress measurement
    public ClientProgressMeasurementDTO updateProgressMeasurement(Long id, ClientProgressMeasurementRequestDTO request) {
        logger.info("Updating progress measurement: {}", id);
        
        Optional<ClientProgressMeasurement> measurementOpt = progressMeasurementRepository.findById(id);
        if (measurementOpt.isEmpty()) {
            throw new RuntimeException("Progress measurement not found with ID: " + id);
        }
        
        ClientProgressMeasurement measurement = measurementOpt.get();
        measurement.setMeasurementDate(request.getMeasurementDate());
        measurement.setWeight(request.getWeight());
        measurement.setBodyFatPercentage(request.getBodyFatPercentage());
        measurement.setMuscleMass(request.getMuscleMass());
        measurement.setMeasurements(request.getMeasurements());
        measurement.setPhotos(request.getPhotos());
        measurement.setNotes(request.getNotes());
        measurement.setMeasuredById(request.getMeasuredById());
        measurement.setUpdatedAt(LocalDateTime.now());
        
        ClientProgressMeasurement savedMeasurement = progressMeasurementRepository.save(measurement);
        logger.info("Progress measurement updated successfully with ID: {}", savedMeasurement.getId());
        
        return convertToDTO(savedMeasurement);
    }

    // Delete progress measurement
    public void deleteProgressMeasurement(Long id) {
        logger.info("Deleting progress measurement: {}", id);
        
        Optional<ClientProgressMeasurement> measurementOpt = progressMeasurementRepository.findById(id);
        if (measurementOpt.isEmpty()) {
            throw new RuntimeException("Progress measurement not found with ID: " + id);
        }
        
        progressMeasurementRepository.deleteById(id);
        logger.info("Progress measurement deleted successfully with ID: {}", id);
    }

    // Get progress measurements by client
    public List<ClientProgressMeasurementDTO> getProgressMeasurementsByClient(Long clientId) {
        logger.info("Fetching progress measurements for client: {}", clientId);
        
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByClient_Id(clientId);
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get progress measurements by client with pagination
    public Page<ClientProgressMeasurementDTO> getProgressMeasurementsByClient(Long clientId, int page, int size) {
        logger.info("Fetching progress measurements for client: {} with pagination", clientId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("measurementDate").descending());
        Page<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByClient_Id(clientId, pageable);
        
        return measurements.map(this::convertToDTO);
    }

    // Get latest progress measurement for client
    public Optional<ClientProgressMeasurementDTO> getLatestProgressMeasurementByClient(Long clientId) {
        logger.info("Fetching latest progress measurement for client: {}", clientId);
        
        Optional<ClientProgressMeasurement> measurementOpt = progressMeasurementRepository.findTopByClient_IdOrderByMeasurementDateDesc(clientId);
        return measurementOpt.map(this::convertToDTO);
    }

    // Get progress measurements by date range
    public List<ClientProgressMeasurementDTO> getProgressMeasurementsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching progress measurements from {} to {}", startDate, endDate);
        
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByMeasurementDateBetween(startDate, endDate);
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get progress measurements by client and date range
    public List<ClientProgressMeasurementDTO> getProgressMeasurementsByClientAndDateRange(Long clientId, LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching progress measurements for client: {} from {} to {}", clientId, startDate, endDate);
        
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByClient_IdAndMeasurementDateBetween(clientId, startDate, endDate);
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get progress measurements by measured by user
    public List<ClientProgressMeasurementDTO> getProgressMeasurementsByMeasuredBy(Long measuredById) {
        logger.info("Fetching progress measurements measured by user: {}", measuredById);
        
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByMeasuredBy_Id(measuredById);
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get recent progress measurements
    public List<ClientProgressMeasurementDTO> getRecentProgressMeasurements(int days) {
        logger.info("Fetching recent progress measurements from last {} days", days);
        
        LocalDate since = LocalDate.now().minusDays(days);
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByMeasurementDateAfter(since);
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get today's progress measurements
    public List<ClientProgressMeasurementDTO> getTodaysProgressMeasurements() {
        logger.info("Fetching today's progress measurements");
        
        LocalDate today = LocalDate.now();
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByMeasurementDate(today);
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this week's progress measurements
    public List<ClientProgressMeasurementDTO> getThisWeeksProgressMeasurements() {
        logger.info("Fetching this week's progress measurements");
        
        LocalDate startOfWeek = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByMeasurementDateBetween(startOfWeek, endOfWeek);
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get this month's progress measurements
    public List<ClientProgressMeasurementDTO> getThisMonthsProgressMeasurements() {
        logger.info("Fetching this month's progress measurements");
        
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByMeasurementDateBetween(startOfMonth, endOfMonth);
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Convert entity to DTO
    private ClientProgressMeasurementDTO convertToDTO(ClientProgressMeasurement measurement) {
        ClientProgressMeasurementDTO dto = new ClientProgressMeasurementDTO();
        dto.setId(measurement.getId());
        dto.setClientId(measurement.getClientId());
        dto.setMeasurementDate(measurement.getMeasurementDate());
        dto.setWeight(measurement.getWeight());
        dto.setBodyFatPercentage(measurement.getBodyFatPercentage());
        dto.setMuscleMass(measurement.getMuscleMass());
        dto.setMeasurements(measurement.getMeasurements());
        dto.setPhotos(measurement.getPhotos());
        dto.setNotes(measurement.getNotes());
        dto.setMeasuredById(measurement.getMeasuredById());
        dto.setCreatedAt(measurement.getCreatedAt());
        dto.setUpdatedAt(measurement.getUpdatedAt());
        
        // Set client name if available
        if (measurement.getClientId() != null) {
            Optional<User> clientOpt = userRepository.findById(measurement.getClientId());
            if (clientOpt.isPresent()) {
                dto.setClientName(clientOpt.get().getFirstName() + " " + clientOpt.get().getLastName());
            }
        }
        
        // Set measured by name if available
        if (measurement.getMeasuredById() != null) {
            Optional<User> measuredByOpt = userRepository.findById(measurement.getMeasuredById());
            if (measuredByOpt.isPresent()) {
                dto.setMeasuredByName(measuredByOpt.get().getFirstName() + " " + measuredByOpt.get().getLastName());
            }
        }
        
        return dto;
    }

    // Get progress measurements by client and type
    public List<ClientProgressMeasurementDTO> getProgressMeasurementsByClientAndType(Long clientId, String type) {
        logger.info("Fetching progress measurements for client: {} and type: {}", clientId, type);
        
        List<ClientProgressMeasurement> measurements = progressMeasurementRepository.findByClient_Id(clientId);
        
        // Filter by type if specified
        if (type != null && !type.isEmpty()) {
            measurements = measurements.stream()
                    .filter(m -> m.getMeasurements() != null && m.getMeasurements().contains(type))
                    .collect(Collectors.toList());
        }
        
        return measurements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
