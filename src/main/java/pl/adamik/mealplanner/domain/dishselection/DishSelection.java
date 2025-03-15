package pl.adamik.mealplanner.domain.dishselection;

import jakarta.persistence.*;
import lombok.*;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.user.User;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DishSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    private Dish dish;
    private LocalDate date;
}
