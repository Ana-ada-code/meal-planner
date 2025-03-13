package pl.adamik.mealplanner.domain.dish;

import jakarta.persistence.*;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.dishselection.DishSelection;
import pl.adamik.mealplanner.domain.favorite.Favorite;
import pl.adamik.mealplanner.domain.rating.Rating;

import java.util.HashSet;
import java.util.Set;

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
    @OneToMany(mappedBy = "dish", cascade = CascadeType.REMOVE)
    private Set<Rating> ratings = new HashSet<>();
    private String image;
    @OneToMany(mappedBy = "dish", cascade = CascadeType.REMOVE)
    private Set<DishSelection> dishSelections = new HashSet<>();
    @OneToMany(mappedBy = "dish", cascade = CascadeType.REMOVE)
    private Set<Favorite> favorites = new HashSet<>();

    public Dish() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    public Set<DishSelection> getDishSelections() {
        return dishSelections;
    }

    public void setDishSelections(Set<DishSelection> dishSelections) {
        this.dishSelections = dishSelections;
    }

    public Set<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Favorite> favorites) {
        this.favorites = favorites;
    }
}
