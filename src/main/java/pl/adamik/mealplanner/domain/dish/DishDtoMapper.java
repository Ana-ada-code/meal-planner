package pl.adamik.mealplanner.domain.dish;

import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.rating.Rating;

class DishDtoMapper {
    static DishDto map(Dish dish) {
        double avgRating = dish.getRatings().stream()
                .map(Rating::getRating)
                .mapToDouble(val -> val)
                .average().orElse(0);
        int ratingCount = dish.getRatings().size();
        return new DishDto(
                dish.getId(),
                dish.getName(),
                dish.getIngredients(),
                dish.getRecipe(),
                dish.getCategory().getName(),
                dish.getImage(),
                avgRating,
                ratingCount
        );
    }
}
