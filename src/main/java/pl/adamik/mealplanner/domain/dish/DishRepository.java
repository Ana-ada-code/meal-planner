package pl.adamik.mealplanner.domain.dish;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    Page<Dish> findAllByCategory_NameIgnoreCase(String category, Pageable pageable);

    List<Dish> findAllByCategory_NameIgnoreCase(String category);

    @Query("select d from Dish d join d.ratings r group by d order by avg(r.rating) desc")
    Page<Dish> findTopByRating(Pageable pageable);

    Page<Dish> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
