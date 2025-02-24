package pl.adamik.mealplanner.domain.dish;

import org.junit.jupiter.api.Test;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.category.Category;

import static org.assertj.core.api.Assertions.*;

class DishDtoMapperTest {

    @Test
    void shouldMapDishToDishDto() {
        // Given
        Category italianCategory = new Category(1L, "Italian");
        Dish dish = new Dish(1L, "Pizza", italianCategory);

        // When
        DishDto dishDto = DishDtoMapper.map(dish);

        // Then
        assertThat(dishDto).isNotNull();
        assertThat(dishDto.getId()).isEqualTo(dish.getId());
        assertThat(dishDto.getName()).isEqualTo(dish.getName());
        assertThat(dishDto.getCategory()).isEqualTo(dish.getCategory().getName());
    }

}