package gym.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "workout_plan_exercises")
public class WorkoutPlanExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private Integer orderIndex;

    @Column
    private Integer sets;

    @Column(length = 50)
    private String reps; // e.g., "8-10", "12", "max", "30s"

    @Column
    private Double weight; // in kg

    @Column
    private Integer duration; // in seconds for time-based exercises

    @Column
    private Integer restTime; // in seconds

    @Column(length = 500)
    private String notes;

    @Column
    private Boolean isOptional = false;

    // Constructors
    public WorkoutPlanExercise() {}

    public WorkoutPlanExercise(WorkoutPlan workoutPlan, Exercise exercise, Integer orderIndex) {
        this.workoutPlan = workoutPlan;
        this.exercise = exercise;
        this.orderIndex = orderIndex;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getRestTime() {
        return restTime;
    }

    public void setRestTime(Integer restTime) {
        this.restTime = restTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(Boolean isOptional) {
        this.isOptional = isOptional;
    }

    // Helper methods
    public String getFormattedReps() {
        if (reps == null || reps.isEmpty()) {
            return "N/A";
        }
        return reps;
    }

    public String getFormattedWeight() {
        if (weight == null || weight <= 0) {
            return "Bodyweight";
        }
        return weight + " kg";
    }

    public String getFormattedDuration() {
        if (duration == null || duration <= 0) {
            return "N/A";
        }
        if (duration < 60) {
            return duration + "s";
        } else {
            int minutes = duration / 60;
            int seconds = duration % 60;
            if (seconds == 0) {
                return minutes + "min";
            } else {
                return minutes + "min " + seconds + "s";
            }
        }
    }

    public String getFormattedRestTime() {
        if (restTime == null || restTime <= 0) {
            return "No rest";
        }
        if (restTime < 60) {
            return restTime + "s";
        } else {
            int minutes = restTime / 60;
            int seconds = restTime % 60;
            if (seconds == 0) {
                return minutes + "min";
            } else {
                return minutes + "min " + seconds + "s";
            }
        }
    }

    @Override
    public String toString() {
        return "WorkoutPlanExercise{" +
                "id=" + id +
                ", exercise=" + (exercise != null ? exercise.getName() : "null") +
                ", orderIndex=" + orderIndex +
                ", sets=" + sets +
                ", reps='" + reps + '\'' +
                ", weight=" + weight +
                ", duration=" + duration +
                ", restTime=" + restTime +
                '}';
    }
}
