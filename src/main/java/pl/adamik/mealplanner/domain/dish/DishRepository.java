package pl.adamik.mealplanner.domain.dish;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishRepository extends CrudRepository<Dish, Long> {
    List<Dish> findAllByCategory_NameIgnoreCase(String category);
}
