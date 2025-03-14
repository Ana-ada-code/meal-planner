package pl.adamik.mealplanner.domain.dish;

import org.junit.jupiter.api.Test;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

import static org.assertj.core.api.Assertions.assertThat;

class DishDtoMapperTest {

    @Test
    void shouldMapDishToDishDto() {
        // Given
        Category italianCategory = new Category();
        italianCategory.setId(1L);
        italianCategory.setName("Italian");

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setIngredients("mąka, oliwa");
        dish.setRecipe("połącz składniaki");
        dish.setCategory(italianCategory);
        dish.setImage("https://example.com/image.jpg");

        // When
        DishDto dishDto = DishDtoMapper.map(dish);

        // Then
        assertThat(dishDto).isNotNull();
        assertThat(dishDto.getId()).isEqualTo(dish.getId());
        assertThat(dishDto.getName()).isEqualTo(dish.getName());
        assertThat(dishDto.getIngredients()).isEqualTo(dish.getIngredients());
        assertThat(dishDto.getRecipe()).isEqualTo(dish.getRecipe());
        assertThat(dishDto.getCategory()).isEqualTo(dish.getCategory().getName());
        assertThat(dishDto.getImage()).isEqualTo(dish.getImage());
    }
}