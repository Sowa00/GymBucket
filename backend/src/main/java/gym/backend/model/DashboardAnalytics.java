package gym.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard_analytics")
public class DashboardAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "analytics_date", nullable = false)
    private LocalDate analyticsDate;

    @Column(name = "total_clients")
    private Integer totalClients = 0;

    @Column(name = "active_clients")
    private Integer activeClients = 0;

    @Column(name = "total_sessions_today")
    private Integer totalSessionsToday = 0;

    @Column(name = "total_sessions_this_week")
    private Integer totalSessionsThisWeek = 0;

    @Column(name = "total_sessions_this_month")
    private Integer totalSessionsThisMonth = 0;

    @Column(name = "monthly_revenue", precision = 10, scale = 2)
    private BigDecimal monthlyRevenue = BigDecimal.ZERO;

    @Column(name = "completion_rate", precision = 5, scale = 2)
    private BigDecimal completionRate = BigDecimal.ZERO;

    @Column(name = "average_session_rating", precision = 3, scale = 2)
    private BigDecimal averageSessionRating = BigDecimal.ZERO;

    @Column(name = "new_clients_this_month")
    private Integer newClientsThisMonth = 0;

    @Column(name = "clients_with_overdue_payments")
    private Integer clientsWithOverduePayments = 0;

    @Column(name = "upcoming_sessions_today")
    private Integer upcomingSessionsToday = 0;

    // Additional fields needed by the service
    @Column(name = "total_workout_plans")
    private Integer totalWorkoutPlans = 0;

    @Column(name = "total_nutrition_plans")
    private Integer totalNutritionPlans = 0;

    @Column(name = "total_meal_logs")
    private Integer totalMealLogs = 0;

    @Column(name = "total_notifications")
    private Integer totalNotifications = 0;

    @Column(name = "unread_notifications")
    private Integer unreadNotifications = 0;

    @Column(name = "total_file_uploads")
    private Integer totalFileUploads = 0;

    @Column(name = "average_meal_rating", precision = 3, scale = 2)
    private BigDecimal averageMealRating = BigDecimal.ZERO;

    @Column(name = "top_workout_plan", length = 255)
    private String topWorkoutPlan;

    @Column(name = "top_nutrition_plan", length = 255)
    private String topNutritionPlan;

    @Column(name = "most_active_client", length = 255)
    private String mostActiveClient;

    @Column(name = "statistics", columnDefinition = "TEXT")
    private String statistics;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public DashboardAnalytics() {}

    public DashboardAnalytics(User user, LocalDate analyticsDate) {
        this.user = user;
        this.analyticsDate = analyticsDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getAnalyticsDate() {
        return analyticsDate;
    }

    public void setAnalyticsDate(LocalDate analyticsDate) {
        this.analyticsDate = analyticsDate;
    }

    public Integer getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(Integer totalClients) {
        this.totalClients = totalClients;
    }

    public Integer getActiveClients() {
        return activeClients;
    }

    public void setActiveClients(Integer activeClients) {
        this.activeClients = activeClients;
    }

    public Integer getTotalSessionsToday() {
        return totalSessionsToday;
    }

    public void setTotalSessionsToday(Integer totalSessionsToday) {
        this.totalSessionsToday = totalSessionsToday;
    }

    public Integer getTotalSessionsThisWeek() {
        return totalSessionsThisWeek;
    }

    public void setTotalSessionsThisWeek(Integer totalSessionsThisWeek) {
        this.totalSessionsThisWeek = totalSessionsThisWeek;
    }

    public Integer getTotalSessionsThisMonth() {
        return totalSessionsThisMonth;
    }

    public void setTotalSessionsThisMonth(Integer totalSessionsThisMonth) {
        this.totalSessionsThisMonth = totalSessionsThisMonth;
    }

    public BigDecimal getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public BigDecimal getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(BigDecimal completionRate) {
        this.completionRate = completionRate;
    }

    public BigDecimal getAverageSessionRating() {
        return averageSessionRating;
    }

    public void setAverageSessionRating(BigDecimal averageSessionRating) {
        this.averageSessionRating = averageSessionRating;
    }

    public Integer getNewClientsThisMonth() {
        return newClientsThisMonth;
    }

    public void setNewClientsThisMonth(Integer newClientsThisMonth) {
        this.newClientsThisMonth = newClientsThisMonth;
    }

    public Integer getClientsWithOverduePayments() {
        return clientsWithOverduePayments;
    }

    public void setClientsWithOverduePayments(Integer clientsWithOverduePayments) {
        this.clientsWithOverduePayments = clientsWithOverduePayments;
    }

    public Integer getUpcomingSessionsToday() {
        return upcomingSessionsToday;
    }

    public void setUpcomingSessionsToday(Integer upcomingSessionsToday) {
        this.upcomingSessionsToday = upcomingSessionsToday;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public String getFormattedMonthlyRevenue() {
        if (monthlyRevenue == null) {
            return "0,00 zł";
        }
        return String.format("%.2f zł", monthlyRevenue);
    }

    public String getFormattedCompletionRate() {
        if (completionRate == null) {
            return "0%";
        }
        return String.format("%.1f%%", completionRate);
    }

    public String getFormattedAverageRating() {
        if (averageSessionRating == null) {
            return "0.0";
        }
        return String.format("%.1f", averageSessionRating);
    }

    public Double getCompletionRateAsDouble() {
        return completionRate != null ? completionRate.doubleValue() : 0.0;
    }

    public Double getAverageRatingAsDouble() {
        return averageSessionRating != null ? averageSessionRating.doubleValue() : 0.0;
    }

    public boolean isToday() {
        return analyticsDate != null && analyticsDate.equals(LocalDate.now());
    }

    public boolean isThisWeek() {
        if (analyticsDate == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return !analyticsDate.isBefore(startOfWeek) && !analyticsDate.isAfter(endOfWeek);
    }

    public boolean isThisMonth() {
        if (analyticsDate == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return analyticsDate.getYear() == now.getYear() && 
               analyticsDate.getMonth() == now.getMonth();
    }

    // Additional methods needed by DashboardAnalyticsService
    public LocalDate getDate() {
        return analyticsDate;
    }

    public void setDate(LocalDate date) {
        this.analyticsDate = date;
    }

    public Integer getNewClients() {
        return newClientsThisMonth;
    }

    public void setNewClients(Integer newClients) {
        this.newClientsThisMonth = newClients;
    }

    public Integer getTotalWorkoutPlans() {
        return totalWorkoutPlans;
    }

    public void setTotalWorkoutPlans(Integer totalWorkoutPlans) {
        this.totalWorkoutPlans = totalWorkoutPlans;
    }

    public void setTotalWorkoutPlans(long totalWorkoutPlans) {
        this.totalWorkoutPlans = (int) totalWorkoutPlans;
    }

    public Integer getTotalNutritionPlans() {
        return totalNutritionPlans;
    }

    public void setTotalNutritionPlans(Integer totalNutritionPlans) {
        this.totalNutritionPlans = totalNutritionPlans;
    }

    public void setTotalNutritionPlans(long totalNutritionPlans) {
        this.totalNutritionPlans = (int) totalNutritionPlans;
    }

    public Integer getTotalWorkoutSessions() {
        return totalSessionsThisMonth;
    }

    public void setTotalWorkoutSessions(Integer totalWorkoutSessions) {
        this.totalSessionsThisMonth = totalWorkoutSessions;
    }

    public void setTotalWorkoutSessions(long totalWorkoutSessions) {
        this.totalSessionsThisMonth = (int) totalWorkoutSessions;
    }

    public Integer getTotalMealLogs() {
        return totalMealLogs;
    }

    public void setTotalMealLogs(Integer totalMealLogs) {
        this.totalMealLogs = totalMealLogs;
    }

    public void setTotalMealLogs(long totalMealLogs) {
        this.totalMealLogs = (int) totalMealLogs;
    }

    public Double getTotalRevenue() {
        return monthlyRevenue != null ? monthlyRevenue.doubleValue() : 0.0;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.monthlyRevenue = totalRevenue != null ? BigDecimal.valueOf(totalRevenue) : BigDecimal.ZERO;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.monthlyRevenue = BigDecimal.valueOf(totalRevenue);
    }

    public Integer getTotalNotifications() {
        return totalNotifications;
    }

    public void setTotalNotifications(Integer totalNotifications) {
        this.totalNotifications = totalNotifications;
    }

    public void setTotalNotifications(long totalNotifications) {
        this.totalNotifications = (int) totalNotifications;
    }

    public Integer getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(Integer unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }

    public Integer getTotalFileUploads() {
        return totalFileUploads;
    }

    public void setTotalFileUploads(Integer totalFileUploads) {
        this.totalFileUploads = totalFileUploads;
    }

    public void setTotalFileUploads(long totalFileUploads) {
        this.totalFileUploads = (int) totalFileUploads;
    }

    public Double getAverageWorkoutRating() {
        return averageSessionRating != null ? averageSessionRating.doubleValue() : 0.0;
    }

    public void setAverageWorkoutRating(Double averageWorkoutRating) {
        this.averageSessionRating = averageWorkoutRating != null ? BigDecimal.valueOf(averageWorkoutRating) : BigDecimal.ZERO;
    }

    public void setAverageWorkoutRating(double averageWorkoutRating) {
        this.averageSessionRating = BigDecimal.valueOf(averageWorkoutRating);
    }

    public BigDecimal getAverageMealRating() {
        return averageMealRating;
    }

    public void setAverageMealRating(BigDecimal averageMealRating) {
        this.averageMealRating = averageMealRating;
    }

    public Double getAverageMealRatingAsDouble() {
        return averageMealRating != null ? averageMealRating.doubleValue() : 0.0;
    }

    public void setAverageMealRatingAsDouble(Double averageMealRating) {
        this.averageMealRating = averageMealRating != null ? BigDecimal.valueOf(averageMealRating) : BigDecimal.ZERO;
    }

    public String getTopWorkoutPlan() {
        return topWorkoutPlan;
    }

    public void setTopWorkoutPlan(String topWorkoutPlan) {
        this.topWorkoutPlan = topWorkoutPlan;
    }

    public String getTopNutritionPlan() {
        return topNutritionPlan;
    }

    public void setTopNutritionPlan(String topNutritionPlan) {
        this.topNutritionPlan = topNutritionPlan;
    }

    public String getMostActiveClient() {
        return mostActiveClient;
    }

    public void setMostActiveClient(String mostActiveClient) {
        this.mostActiveClient = mostActiveClient;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    @Override
    public String toString() {
        return "DashboardAnalytics{" +
                "id=" + id +
                ", user=" + (user != null ? user.getFullName() : "null") +
                ", analyticsDate=" + analyticsDate +
                ", totalClients=" + totalClients +
                ", activeClients=" + activeClients +
                ", monthlyRevenue=" + monthlyRevenue +
                ", completionRate=" + completionRate +
                '}';
    }
}
