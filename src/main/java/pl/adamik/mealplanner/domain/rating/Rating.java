package pl.adamik.mealplanner.domain.rating;

import jakarta.persistence.*;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.user.User;

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

    public Rating() {
    }

    public Rating(User user, Dish dish, Integer rating) {
        this.user = user;
        this.dish = dish;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
