package gym.backend.repository;

import gym.backend.model.Client;
import gym.backend.model.ClientProgressMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientProgressMeasurementRepository extends JpaRepository<ClientProgressMeasurement, Long> {

    // Find measurements by client
    List<ClientProgressMeasurement> findByClient(Client client);
    
    // Find measurements by client with pagination
    Page<ClientProgressMeasurement> findByClient(Client client, Pageable pageable);
    
    // Find measurements by client ordered by date
    List<ClientProgressMeasurement> findByClientOrderByMeasurementDateDesc(Client client);
    
    // Find measurements by date range
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client AND " +
           "cpm.measurementDate BETWEEN :startDate AND :endDate ORDER BY cpm.measurementDate DESC")
    List<ClientProgressMeasurement> findByClientAndDateRange(@Param("client") Client client, 
                                                          @Param("startDate") LocalDate startDate, 
                                                          @Param("endDate") LocalDate endDate);
    
    // Find latest measurement for client
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client " +
           "ORDER BY cpm.measurementDate DESC LIMIT 1")
    Optional<ClientProgressMeasurement> findLatestByClient(@Param("client") Client client);
    
    // Find first measurement for client
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client " +
           "ORDER BY cpm.measurementDate ASC LIMIT 1")
    Optional<ClientProgressMeasurement> findFirstByClient(@Param("client") Client client);
    
    // Find measurements by date
    List<ClientProgressMeasurement> findByClientAndMeasurementDate(Client client, LocalDate measurementDate);
    
    // Find measurements with weight data
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client AND " +
           "cpm.weight IS NOT NULL ORDER BY cpm.measurementDate DESC")
    List<ClientProgressMeasurement> findWithWeightData(@Param("client") Client client);
    
    // Find measurements with body fat data
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client AND " +
           "cpm.bodyFatPercentage IS NOT NULL ORDER BY cpm.measurementDate DESC")
    List<ClientProgressMeasurement> findWithBodyFatData(@Param("client") Client client);
    
    // Find measurements with muscle mass data
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client AND " +
           "cpm.muscleMass IS NOT NULL ORDER BY cpm.measurementDate DESC")
    List<ClientProgressMeasurement> findWithMuscleMassData(@Param("client") Client client);
    
    // Count measurements by client
    Long countByClient(Client client);
    
    // Find measurements by date range for all clients
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE " +
           "cpm.measurementDate BETWEEN :startDate AND :endDate ORDER BY cpm.measurementDate DESC")
    List<ClientProgressMeasurement> findByDateRange(@Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);
    
    // Find recent measurements (last 30 days)
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client AND " +
           "cpm.measurementDate >= :date ORDER BY cpm.measurementDate DESC")
    List<ClientProgressMeasurement> findRecentMeasurements(@Param("client") Client client, 
                                                         @Param("date") LocalDate date);
    
    // Find measurements with photos
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client AND " +
           "cpm.photos IS NOT NULL AND cpm.photos != '' ORDER BY cpm.measurementDate DESC")
    List<ClientProgressMeasurement> findWithPhotos(@Param("client") Client client);
    
    // Find measurements with notes
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client AND " +
           "cpm.notes IS NOT NULL AND cpm.notes != '' ORDER BY cpm.measurementDate DESC")
    List<ClientProgressMeasurement> findWithNotes(@Param("client") Client client);
    
    
    // Find measurements for specific month
    @Query("SELECT cpm FROM ClientProgressMeasurement cpm WHERE cpm.client = :client AND " +
           "YEAR(cpm.measurementDate) = :year AND MONTH(cpm.measurementDate) = :month " +
           "ORDER BY cpm.measurementDate DESC")
    List<ClientProgressMeasurement> findByClientAndMonth(@Param("client") Client client, 
                                                        @Param("year") int year, 
                                                        @Param("month") int month);

    // Additional methods needed by ClientProgressMeasurementService
    List<ClientProgressMeasurement> findByClient_Id(Long clientId);
    Page<ClientProgressMeasurement> findByClient_Id(Long clientId, Pageable pageable);
    Optional<ClientProgressMeasurement> findTopByClient_IdOrderByMeasurementDateDesc(Long clientId);
    List<ClientProgressMeasurement> findByMeasurementDateBetween(LocalDate startDate, LocalDate endDate);
    List<ClientProgressMeasurement> findByClient_IdAndMeasurementDateBetween(Long clientId, LocalDate startDate, LocalDate endDate);
    List<ClientProgressMeasurement> findByMeasuredBy_Id(Long measuredById);
    List<ClientProgressMeasurement> findByMeasurementDateAfter(LocalDate date);
    List<ClientProgressMeasurement> findByMeasurementDate(LocalDate date);
}
