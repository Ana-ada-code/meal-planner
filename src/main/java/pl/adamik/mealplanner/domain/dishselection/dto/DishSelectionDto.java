package pl.adamik.mealplanner.domain.dishselection.dto;

import pl.adamik.mealplanner.domain.category.dto.CategorySelectionDto;

import java.time.LocalDate;
import java.util.List;

public class DishSelectionDto {
    private LocalDate date;
    private List<CategorySelectionDto> categories;

    public DishSelectionDto(LocalDate date, List<CategorySelectionDto> categories) {
        this.date = date;
        this.categories = categories;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<CategorySelectionDto> getCategories() {
        return categories;
    }
}
