package gym.backend.service;

import gym.backend.dto.TrainingSessionDTO;
import gym.backend.model.Client;
import gym.backend.model.TrainingSession;
import gym.backend.model.User;
import gym.backend.repository.ClientRepository;
import gym.backend.repository.TrainingSessionRepository;
import gym.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TrainingSessionService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingSessionService.class);

    private final TrainingSessionRepository trainingSessionRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Autowired
    public TrainingSessionService(TrainingSessionRepository trainingSessionRepository,
                                ClientRepository clientRepository,
                                UserRepository userRepository) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    // Create a new training session
    public TrainingSessionDTO createSession(TrainingSessionDTO sessionDTO, Long trainerId) {
        logger.info("Creating new training session for trainer: {}", trainerId);

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        Client client = clientRepository.findById(sessionDTO.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + sessionDTO.getClientId()));

        // Verify trainer owns this client
        if (!client.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Client does not belong to trainer");
        }

        TrainingSession session = new TrainingSession();
        session.setSessionDate(sessionDTO.getSessionDate());
        session.setStartTime(sessionDTO.getStartTime());
        session.setEndTime(sessionDTO.getEndTime());
        session.setSessionType(sessionDTO.getSessionType());
        session.setLocation(sessionDTO.getLocation());
        session.setNotes(sessionDTO.getNotes());
        session.setStatus(TrainingSession.SessionStatus.SCHEDULED);
        session.setClient(client);
        session.setTrainer(trainer);
        session.setPrice(sessionDTO.getPrice());
        session.setIsPaid(sessionDTO.getIsPaid() != null ? sessionDTO.getIsPaid() : false);

        TrainingSession savedSession = trainingSessionRepository.save(session);
        logger.info("Training session created successfully with id: {}", savedSession.getId());

        return convertToDTO(savedSession);
    }

    // Get sessions by trainer
    @Transactional(readOnly = true)
    public List<TrainingSessionDTO> getSessionsByTrainer(Long trainerId) {
        logger.debug("Getting sessions for trainer: {}", trainerId);

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        List<TrainingSession> sessions = trainingSessionRepository.findByTrainer(trainer);
        return sessions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get today's sessions
    @Transactional(readOnly = true)
    public List<TrainingSessionDTO> getTodaysSessions(Long trainerId) {
        logger.debug("Getting today's sessions for trainer: {}", trainerId);

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        List<TrainingSession> sessions = trainingSessionRepository.findTodaysSessionsByTrainer(trainer);
        return sessions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get upcoming sessions
    @Transactional(readOnly = true)
    public List<TrainingSessionDTO> getUpcomingSessions(Long trainerId) {
        logger.debug("Getting upcoming sessions for trainer: {}", trainerId);

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found with id: " + trainerId));

        List<TrainingSession> sessions = trainingSessionRepository.findUpcomingSessionsByTrainer(trainer);
        return sessions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Update session
    public TrainingSessionDTO updateSession(Long sessionId, TrainingSessionDTO sessionDTO, Long trainerId) {
        logger.info("Updating session: {} for trainer: {}", sessionId, trainerId);

        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with id: " + sessionId));

        // Verify trainer owns this session
        if (!session.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Session does not belong to trainer");
        }

        // Update fields
        session.setSessionDate(sessionDTO.getSessionDate());
        session.setStartTime(sessionDTO.getStartTime());
        session.setEndTime(sessionDTO.getEndTime());
        session.setSessionType(sessionDTO.getSessionType());
        session.setLocation(sessionDTO.getLocation());
        session.setNotes(sessionDTO.getNotes());
        if (sessionDTO.getStatus() != null) {
            session.setStatus(TrainingSession.SessionStatus.valueOf(sessionDTO.getStatus()));
        }
        session.setPrice(sessionDTO.getPrice());
        if (sessionDTO.getIsPaid() != null) {
            session.setIsPaid(sessionDTO.getIsPaid());
        }
        session.setFeedback(sessionDTO.getFeedback());
        session.setRating(sessionDTO.getRating());

        TrainingSession updatedSession = trainingSessionRepository.save(session);
        logger.info("Session updated successfully with id: {}", updatedSession.getId());

        return convertToDTO(updatedSession);
    }

    // Delete session
    public void deleteSession(Long sessionId, Long trainerId) {
        logger.info("Deleting session: {} for trainer: {}", sessionId, trainerId);

        TrainingSession session = trainingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with id: " + sessionId));

        // Verify trainer owns this session
        if (!session.getTrainer().getId().equals(trainerId)) {
            throw new IllegalArgumentException("Session does not belong to trainer");
        }

        trainingSessionRepository.delete(session);
        logger.info("Session deleted successfully with id: {}", sessionId);
    }

    // Convert TrainingSession entity to DTO
    private TrainingSessionDTO convertToDTO(TrainingSession session) {
        TrainingSessionDTO dto = new TrainingSessionDTO();
        dto.setId(session.getId());
        dto.setSessionDate(session.getSessionDate());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setSessionType(session.getSessionType());
        dto.setLocation(session.getLocation());
        dto.setNotes(session.getNotes());
        dto.setStatus(session.getStatus().name());
        dto.setClientId(session.getClient().getId());
        dto.setClientName(session.getClient().getFullName());
        dto.setTrainerId(session.getTrainer().getId());
        dto.setTrainerName(session.getTrainer().getFullName());
        dto.setPrice(session.getPrice());
        dto.setIsPaid(session.getIsPaid());
        dto.setFeedback(session.getFeedback());
        dto.setRating(session.getRating());
        dto.setCreatedAt(session.getCreatedAt());
        dto.setUpdatedAt(session.getUpdatedAt());

        return dto;
    }
}
