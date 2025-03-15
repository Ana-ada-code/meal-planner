package pl.adamik.mealplanner.domain.favorite;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.user.User;

@Getter
@Setter
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    private Dish dish;
}


