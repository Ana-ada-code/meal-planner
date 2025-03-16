package pl.adamik.mealplanner.domain.category;

import pl.adamik.mealplanner.domain.category.dto.CategoryDto;

class CategoryDtoMapper {

    private CategoryDtoMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static CategoryDto map(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
