package pl.adamik.mealplanner.domain.dishselection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DishSelectionRepository extends CrudRepository<DishSelection, Long> {
    @Query("""
                SELECT ds FROM DishSelection ds
                WHERE ds.user.id = :userId
                AND ds.date >= CURRENT_DATE
            """)
    List<DishSelection> findDishSelectionByUser_IdFromToday(@Param("userId") Long userId);

    void deleteAllByDate(LocalDate date);
}





