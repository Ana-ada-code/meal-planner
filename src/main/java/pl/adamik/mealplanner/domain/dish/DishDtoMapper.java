package pl.adamik.mealplanner.domain.dish;

import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.dish.dto.DishSaveDto;
import pl.adamik.mealplanner.domain.rating.Rating;

public class DishDtoMapper {
    public static DishDto map(Dish dish) {
        double avgRating = dish.getRatings().stream()
                .map(Rating::getRating)
                .mapToDouble(val -> val)
                .average().orElse(0);
        int ratingCount = dish.getRatings().size();
        boolean favorite = !dish.getFavorites().isEmpty();
        return new DishDto(
                dish.getId(),
                dish.getName(),
                dish.getIngredients(),
                dish.getRecipe(),
                dish.getCategory().getName(),
                dish.getImage(),
                avgRating,
                ratingCount,
                favorite
        );
    }

    public static DishSaveDto map(DishDto dishDto) {
        DishSaveDto dishSaveDto = new DishSaveDto();
        dishSaveDto.setId(dishDto.getId());
        dishSaveDto.setName(dishDto.getName());
        dishSaveDto.setIngredients(dishDto.getIngredients());
        dishSaveDto.setRecipe(dishDto.getRecipe());
        dishSaveDto.setCategory(dishDto.getCategory());
        dishSaveDto.setImage(dishSaveDto.getImage());
        return dishSaveDto;
    }
}
