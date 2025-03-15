package pl.adamik.mealplanner.domain.dish;

import jakarta.persistence.*;
import lombok.*;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.dishselection.DishSelection;
import pl.adamik.mealplanner.domain.favorite.Favorite;
import pl.adamik.mealplanner.domain.rating.Rating;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String ingredients;
    private String recipe;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    @Builder.Default
    @OneToMany(mappedBy = "dish", cascade = CascadeType.REMOVE)
    private Set<Rating> ratings = new HashSet<>();
    private String image;
    @Builder.Default
    @OneToMany(mappedBy = "dish", cascade = CascadeType.REMOVE)
    private Set<DishSelection> dishSelections = new HashSet<>();
    @Builder.Default
    @OneToMany(mappedBy = "dish", cascade = CascadeType.REMOVE)
    private Set<Favorite> favorites = new HashSet<>();
}
