package pl.adamik.mealplanner.domain.dishselection.dto;

import pl.adamik.mealplanner.domain.category.dto.CategorySelectionDto;

import java.time.LocalDate;
import java.util.List;

public record DishSelectionDto(
        LocalDate date,
        List<CategorySelectionDto> categories
) {
}
