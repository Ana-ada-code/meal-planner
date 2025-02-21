package pl.adamik.mealplanner.domain.dish;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {

}
