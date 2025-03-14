package pl.adamik.mealplanner.domain.category.dto;

import lombok.Getter;

import java.util.List;

public final class CategorySelectionDto {
    @Getter
    private final CategoryDto categoryDto;
    private final List<DishDto> dishes;

    public CategorySelectionDto(CategoryDto categoryDto, List<DishDto> dishes) {
        this.categoryDto = categoryDto;
        this.dishes = List.copyOf(dishes);
    }

    public List<DishDto> getDishes() {
        return List.copyOf(dishes);
    }

    public record DishDto(Long id, String name, Long selectionId) {
    }
}
