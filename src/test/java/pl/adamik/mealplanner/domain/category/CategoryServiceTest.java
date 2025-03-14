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

    @Test
    void shouldAddCategory_whenCategoryIsValid() {
        // Given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Mexican");

        // When
        categoryService.addCategory(categoryDto);

        // Then
        verify(categoryRepository, times(1)).save(argThat(category ->
                category.getName().equals("Mexican")
        ));
    }


    @Test
    void shouldAddCategory_whenCategoryHasLeadingOrTrailingSpaces() {
        // Given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("  Thai  ");

        // When
        categoryService.addCategory(categoryDto);

        // Then
        verify(categoryRepository, times(1)).save(argThat(category ->
                category.getName().trim().equals("Thai")
        ));
    }

    @Test
    void shouldRemoveCategory_whenCategoryExists() {
        // Given
        Category category = new Category(1L, "Italian");
        when(categoryRepository.findByNameIgnoreCase("Italian")).thenReturn(Optional.of(category));

        // When
        boolean result = categoryService.removeCategory("Italian");

        // Then
        assertThat(result).isTrue();
        verify(categoryRepository, times(1)).findByNameIgnoreCase("Italian");
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void shouldReturnFalse_whenCategoryDoesNotExist() {
        // Given
        when(categoryRepository.findByNameIgnoreCase("Italian")).thenReturn(Optional.empty());

        // When
        boolean result = categoryService.removeCategory("Italian");

        // Then
        assertThat(result).isFalse();
        verify(categoryRepository, times(1)).findByNameIgnoreCase("Italian");
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void shouldNotThrowException_whenCategoryIsNull() {
        // Given
        when(categoryRepository.findByNameIgnoreCase(null)).thenReturn(Optional.empty());

        // When
        boolean result = categoryService.removeCategory(null);

        // Then
        assertThat(result).isFalse();
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    void shouldUpdateCategory_whenCategoryExists() {
        // Given
        Long categoryId = 1L;
        String newName = "Updated Name";
        Category category = new Category(categoryId, "Old Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        boolean result = categoryService.update(categoryId, newName);

        // Then
        assertThat(result).isTrue();
        assertThat(category.getName()).isEqualTo(newName);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void shouldReturnFalse_whenCategoryDoesNotExist_UpdateCategory() {
        // Given
        Long categoryId = 1L;
        String newName = "Updated Name";

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        boolean result = categoryService.update(categoryId, newName);

        // Then
        assertThat(result).isFalse();
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void shouldReturnFalse_whenNewNameIsNull() {
        // Given
        Long categoryId = 1L;
        Category category = new Category(categoryId, "Old Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        boolean result = categoryService.update(categoryId, null);

        // Then
        assertThat(result).isFalse();
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).save(any());
    }
}
