package pl.adamik.mealplanner.domain.dish;

import pl.adamik.mealplanner.domain.dish.dto.DishDto;

class DishDtoMapper {
    static DishDto map(Dish dish) {
        return new DishDto(
                dish.getId(),
                dish.getName(),
                dish.getIngredients(),
                dish.getRecipe(),
                dish.getCategory().getName()
        );
    }
}
