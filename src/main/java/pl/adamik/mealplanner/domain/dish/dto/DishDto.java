package pl.adamik.mealplanner.domain.dish.dto;

public record DishDto(
        Long id,
        String name,
        String ingredients,
        String recipe,
        String category,
        String image,
        Double avgRating,
        Integer ratingCount,
        Boolean favorite
) {}