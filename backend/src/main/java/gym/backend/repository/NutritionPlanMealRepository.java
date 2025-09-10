package gym.backend.repository;

import gym.backend.model.NutritionPlanMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionPlanMealRepository extends JpaRepository<NutritionPlanMeal, Long> {
}
