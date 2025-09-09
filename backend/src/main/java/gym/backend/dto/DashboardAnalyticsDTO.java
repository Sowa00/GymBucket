package gym.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DashboardAnalyticsDTO {
    private Long id;
    private LocalDate date;
    private Integer totalClients;
    private Integer activeClients;
    private Integer newClients;
    private Integer totalWorkoutPlans;
    private Integer totalNutritionPlans;
    private Integer totalWorkoutSessions;
    private Integer totalMealLogs;
    private Double totalRevenue;
    private Double monthlyRevenue;
    private Integer totalNotifications;
    private Integer unreadNotifications;
    private Integer totalFileUploads;
    private Double averageWorkoutRating;
    private Double averageMealRating;
    private String topWorkoutPlan;
    private String topNutritionPlan;
    private String mostActiveClient;
    private String statistics; // JSON string for additional stats
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public DashboardAnalyticsDTO() {}

    public DashboardAnalyticsDTO(Long id, LocalDate date, Integer totalClients, 
                                Integer activeClients, Integer newClients) {
        this.id = id;
        this.date = date;
        this.totalClients = totalClients;
        this.activeClients = activeClients;
        this.newClients = newClients;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Integer getNewClients() {
        return newClients;
    }

    public void setNewClients(Integer newClients) {
        this.newClients = newClients;
    }

    public Integer getTotalWorkoutPlans() {
        return totalWorkoutPlans;
    }

    public void setTotalWorkoutPlans(Integer totalWorkoutPlans) {
        this.totalWorkoutPlans = totalWorkoutPlans;
    }

    public Integer getTotalNutritionPlans() {
        return totalNutritionPlans;
    }

    public void setTotalNutritionPlans(Integer totalNutritionPlans) {
        this.totalNutritionPlans = totalNutritionPlans;
    }

    public Integer getTotalWorkoutSessions() {
        return totalWorkoutSessions;
    }

    public void setTotalWorkoutSessions(Integer totalWorkoutSessions) {
        this.totalWorkoutSessions = totalWorkoutSessions;
    }

    public Integer getTotalMealLogs() {
        return totalMealLogs;
    }

    public void setTotalMealLogs(Integer totalMealLogs) {
        this.totalMealLogs = totalMealLogs;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(Double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public Integer getTotalNotifications() {
        return totalNotifications;
    }

    public void setTotalNotifications(Integer totalNotifications) {
        this.totalNotifications = totalNotifications;
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

    public Double getAverageWorkoutRating() {
        return averageWorkoutRating;
    }

    public void setAverageWorkoutRating(Double averageWorkoutRating) {
        this.averageWorkoutRating = averageWorkoutRating;
    }

    public Double getAverageMealRating() {
        return averageMealRating;
    }

    public void setAverageMealRating(Double averageMealRating) {
        this.averageMealRating = averageMealRating;
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
    public boolean hasRevenue() {
        return totalRevenue != null && totalRevenue > 0;
    }

    public boolean hasMonthlyRevenue() {
        return monthlyRevenue != null && monthlyRevenue > 0;
    }

    public boolean hasWorkoutRating() {
        return averageWorkoutRating != null && averageWorkoutRating > 0;
    }

    public boolean hasMealRating() {
        return averageMealRating != null && averageMealRating > 0;
    }

    public boolean hasTopWorkoutPlan() {
        return topWorkoutPlan != null && !topWorkoutPlan.trim().isEmpty();
    }

    public boolean hasTopNutritionPlan() {
        return topNutritionPlan != null && !topNutritionPlan.trim().isEmpty();
    }

    public boolean hasMostActiveClient() {
        return mostActiveClient != null && !mostActiveClient.trim().isEmpty();
    }

    public boolean hasStatistics() {
        return statistics != null && !statistics.trim().isEmpty();
    }

    public String getFormattedTotalRevenue() {
        if (totalRevenue == null) {
            return "0.00 zł";
        }
        return String.format("%.2f zł", totalRevenue);
    }

    public String getFormattedMonthlyRevenue() {
        if (monthlyRevenue == null) {
            return "0.00 zł";
        }
        return String.format("%.2f zł", monthlyRevenue);
    }

    public String getFormattedAverageWorkoutRating() {
        if (averageWorkoutRating == null) {
            return "N/A";
        }
        return String.format("%.1f/5", averageWorkoutRating);
    }

    public String getFormattedAverageMealRating() {
        if (averageMealRating == null) {
            return "N/A";
        }
        return String.format("%.1f/5", averageMealRating);
    }

    public String getFormattedDate() {
        if (date == null) {
            return "N/A";
        }
        return date.toString();
    }

    public boolean isToday() {
        return date != null && date.equals(LocalDate.now());
    }

    public boolean isThisWeek() {
        if (date == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        return !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek);
    }

    public boolean isThisMonth() {
        if (date == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return date.getYear() == now.getYear() && 
               date.getMonth() == now.getMonth();
    }

    public boolean isRecent() {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now().minusDays(7));
    }

    public Integer getClientGrowthRate() {
        if (totalClients == null || newClients == null || totalClients == 0) {
            return 0;
        }
        return (newClients * 100) / totalClients;
    }

    public Integer getActiveClientPercentage() {
        if (totalClients == null || activeClients == null || totalClients == 0) {
            return 0;
        }
        return (activeClients * 100) / totalClients;
    }

    public Integer getUnreadNotificationPercentage() {
        if (totalNotifications == null || unreadNotifications == null || totalNotifications == 0) {
            return 0;
        }
        return (unreadNotifications * 100) / totalNotifications;
    }

    @Override
    public String toString() {
        return "DashboardAnalyticsDTO{" +
                "id=" + id +
                ", date=" + date +
                ", totalClients=" + totalClients +
                ", activeClients=" + activeClients +
                ", newClients=" + newClients +
                ", totalRevenue=" + totalRevenue +
                ", monthlyRevenue=" + monthlyRevenue +
                '}';
    }
}
