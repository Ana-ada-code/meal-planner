package pl.adamik.mealplanner.domain.category;

import org.junit.jupiter.api.Test;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryDtoMapperTest {

    @Test
    void shouldMapCategoryToCategoryDto() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .name("Italian")
                .build();

        // When
        CategoryDto categoryDto = CategoryDtoMapper.map(category);

        // Then
        assertThat(categoryDto).isNotNull();
        assertThat(categoryDto.getId()).isEqualTo(category.getId());
        assertThat(categoryDto.getName()).isEqualTo(category.getName());
    }
}