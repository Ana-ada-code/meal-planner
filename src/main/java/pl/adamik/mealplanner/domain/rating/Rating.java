package pl.adamik.mealplanner.domain.rating;

import jakarta.persistence.*;
import lombok.*;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dish_rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "dish_id")
    private Dish dish;
    private Integer rating;
}
