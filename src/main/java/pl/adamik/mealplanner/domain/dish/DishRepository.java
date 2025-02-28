package pl.adamik.mealplanner.domain.dish;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DishRepository extends CrudRepository<Dish, Long> {
    List<Dish> findAllByCategory_NameIgnoreCase(String category);
    @Query("select d from Dish d join d.ratings r group by d order by avg(r.rating) desc")
    List<Dish> findTopByRating(Pageable pageable);
}
