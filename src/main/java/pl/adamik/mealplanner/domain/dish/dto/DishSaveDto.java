package pl.adamik.mealplanner.domain.dish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class DishSaveDto {
    private Long id;
    @NotBlank(message = "Nazwa dania nie może być pusta")
    @Size(min = 2, max = 100, message = "Nazwa dania musi mieć od 2 do 100 znaków")
    private String name;
    @NotBlank(message = "Składniki nie mogą być puste")
    private String ingredients;
    @NotBlank(message = "Przepis nie może być pusty")
    private String recipe;
    @NotNull(message = "Kategorie jest nie mogą być puste")
    private String category;
    private MultipartFile image;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
