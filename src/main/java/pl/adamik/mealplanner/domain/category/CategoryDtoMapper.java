package pl.adamik.mealplanner.domain.category;

import pl.adamik.mealplanner.domain.category.dto.CategoryDto;

class CategoryDtoMapper {
    static CategoryDto map(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
