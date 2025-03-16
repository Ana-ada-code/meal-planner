package pl.adamik.mealplanner.domain.dishselection.dto;

import lombok.Getter;
import pl.adamik.mealplanner.domain.category.dto.CategorySelectionDto;

import java.time.LocalDate;
import java.util.List;

public final class DishSelectionDto {
    @Getter
    private final LocalDate date;
    private final List<CategorySelectionDto> categories;

    public DishSelectionDto(LocalDate date, List<CategorySelectionDto> categories) {
        this.date = date;
        this.categories = List.copyOf(categories);
    }

    public List<CategorySelectionDto> getCategories() {
        return List.copyOf(categories);
    }
}
