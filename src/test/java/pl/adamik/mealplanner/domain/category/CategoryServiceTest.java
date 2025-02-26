package pl.adamik.mealplanner.domain.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldReturnCategory_whenCategoryExists() {
        // Given
        Category category = new Category(1L, "Italian");
        when(categoryRepository.findByNameIgnoreCase("Italian")).thenReturn(Optional.of(category));

        // When
        Optional<CategoryDto> result = categoryService.findCategoryByName("Italian");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Italian");
        verify(categoryRepository, times(1)).findByNameIgnoreCase("Italian");
    }

    @Test
    void shouldReturnEmptyOptional_whenCategoryDoesNotExist() {
        // Given
        when(categoryRepository.findByNameIgnoreCase("Italian")).thenReturn(Optional.empty());

        // When
        Optional<CategoryDto> result = categoryService.findCategoryByName("Italian");

        // Then
        assertThat(result).isEmpty();
        verify(categoryRepository, times(1)).findByNameIgnoreCase("Italian");
    }

    @Test
    void shouldReturnAllCategories_whenCategoriesExist() {
        // Given
        Category category1 = new Category(1L, "Italian");
        Category category2 = new Category(2L, "Japanese");
        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        // When
        List<CategoryDto> result = categoryService.findAllCategories();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Italian");
        assertThat(result.get(1).getName()).isEqualTo("Japanese");
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyList_whenNoCategoriesExist() {
        // Given
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<CategoryDto> result = categoryService.findAllCategories();

        // Then
        assertThat(result).isEmpty();
        verify(categoryRepository, times(1)).findAll();
    }
}
