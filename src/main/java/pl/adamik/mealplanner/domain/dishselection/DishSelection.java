package pl.adamik.mealplanner.domain.dishselection;

import jakarta.persistence.*;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.user.User;

import java.time.LocalDate;

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

    public DishSelection() {
    }

    public DishSelection(final User user, final Dish dish, final LocalDate date) {
        this.user = user;
        this.dish = dish;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
