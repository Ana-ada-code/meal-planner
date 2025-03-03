package pl.adamik.mealplanner.domain.dishselection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DishSelectionRepository extends CrudRepository<DishSelection, Long> {
    @Query("""
    SELECT ds FROM DishSelection ds
    WHERE ds.user.id = :userId
    AND ds.date >= CURRENT_DATE
""")
    List<DishSelection> findDishSelectionByUser_IdForNext7Days(
            @Param("userId") Long userId
    );
}





