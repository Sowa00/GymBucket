package gym.backend.repository;

import gym.backend.model.DashboardAnalytics;
import gym.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DashboardAnalyticsRepository extends JpaRepository<DashboardAnalytics, Long> {

    // Find analytics by user
    List<DashboardAnalytics> findByUser(User user);
    
    // Find analytics by user and date
    Optional<DashboardAnalytics> findByUserAndAnalyticsDate(User user, LocalDate analyticsDate);
    
    // Additional methods needed by DashboardAnalyticsService
    Optional<DashboardAnalytics> findByAnalyticsDate(LocalDate date);
    Optional<DashboardAnalytics> findTopByOrderByAnalyticsDateDesc();
    List<DashboardAnalytics> findByAnalyticsDateBetween(LocalDate startDate, LocalDate endDate);
    List<DashboardAnalytics> findByAnalyticsDateAfter(LocalDate date);
    List<DashboardAnalytics> findByAnalyticsDateAfterOrderByAnalyticsDateAsc(LocalDate date);
    
    // Find analytics by user ordered by date
    List<DashboardAnalytics> findByUserOrderByAnalyticsDateDesc(User user);
    
    // Find latest analytics for user
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user " +
           "ORDER BY da.analyticsDate DESC LIMIT 1")
    Optional<DashboardAnalytics> findLatestByUser(@Param("user") User user);
    
    // Find analytics by date range
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.analyticsDate BETWEEN :startDate AND :endDate ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findByUserAndDateRange(@Param("user") User user, 
                                                  @Param("startDate") LocalDate startDate, 
                                                  @Param("endDate") LocalDate endDate);
    
    // Find analytics for today
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND da.analyticsDate = :date")
    Optional<DashboardAnalytics> findTodayAnalytics(@Param("user") User user, @Param("date") LocalDate date);
    
    // Find analytics for this week
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.analyticsDate >= :startOfWeek ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findThisWeekAnalytics(@Param("user") User user, 
                                                 @Param("startOfWeek") LocalDate startOfWeek);
    
    // Find analytics for this month
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "YEAR(da.analyticsDate) = :year AND MONTH(da.analyticsDate) = :month " +
           "ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findThisMonthAnalytics(@Param("user") User user, 
                                                  @Param("year") int year, 
                                                  @Param("month") int month);
    
    // Find analytics by year
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "YEAR(da.analyticsDate) = :year ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findByUserAndYear(@Param("user") User user, @Param("year") int year);
    
    // Find analytics by month
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "YEAR(da.analyticsDate) = :year AND MONTH(da.analyticsDate) = :month " +
           "ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findByUserAndMonth(@Param("user") User user, 
                                              @Param("year") int year, 
                                              @Param("month") int month);
    
    // Find recent analytics (last 30 days)
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.analyticsDate >= :date ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findRecentAnalytics(@Param("user") User user, 
                                               @Param("date") LocalDate date);
    
    // Calculate average monthly revenue
    @Query("SELECT AVG(da.monthlyRevenue) FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.analyticsDate >= :startDate")
    Double calculateAverageMonthlyRevenue(@Param("user") User user, 
                                        @Param("startDate") LocalDate startDate);
    
    // Calculate average completion rate
    @Query("SELECT AVG(da.completionRate) FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.analyticsDate >= :startDate")
    Double calculateAverageCompletionRate(@Param("user") User user, 
                                        @Param("startDate") LocalDate startDate);
    
    // Calculate average session rating
    @Query("SELECT AVG(da.averageSessionRating) FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.analyticsDate >= :startDate")
    Double calculateAverageSessionRating(@Param("user") User user, 
                                       @Param("startDate") LocalDate startDate);
    
    // Find peak client count
    @Query("SELECT MAX(da.totalClients) FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.analyticsDate >= :startDate")
    Integer findPeakClientCount(@Param("user") User user, 
                              @Param("startDate") LocalDate startDate);
    
    // Find peak monthly revenue
    @Query("SELECT MAX(da.monthlyRevenue) FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.analyticsDate >= :startDate")
    Double findPeakMonthlyRevenue(@Param("user") User user, 
                                @Param("startDate") LocalDate startDate);
    
    // Find analytics with high completion rate
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.completionRate >= :minRate ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findWithHighCompletionRate(@Param("user") User user, 
                                                      @Param("minRate") Double minRate);
    
    // Find analytics with high revenue
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.monthlyRevenue >= :minRevenue ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findWithHighRevenue(@Param("user") User user, 
                                               @Param("minRevenue") Double minRevenue);
    
    // Find analytics with many clients
    @Query("SELECT da FROM DashboardAnalytics da WHERE da.user = :user AND " +
           "da.totalClients >= :minClients ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findWithManyClients(@Param("user") User user, 
                                               @Param("minClients") Integer minClients);
    
    // Count analytics records by user
    Long countByUser(User user);
    
    
    // Find analytics by date range (all users)
    @Query("SELECT da FROM DashboardAnalytics da WHERE " +
           "da.analyticsDate BETWEEN :startDate AND :endDate ORDER BY da.analyticsDate DESC")
    List<DashboardAnalytics> findByDateRange(@Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);
}
