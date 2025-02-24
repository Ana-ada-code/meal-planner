package pl.adamik.mealplanner.domain.dish;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    @Test
    void shouldReturnAllDishes_whenDishesExist() {
        // Given
        Category italianCategory = new Category(1L, "Italian");
        Category japaneseCategory = new Category(2L, "Japanese");

        Dish dish1 = new Dish(1L, "Pizza", "mąka, oliwa", "połącz składniaki", italianCategory);
        Dish dish2 = new Dish(2L, "Sushi", "ryż, ryba", "połącz składniki",japaneseCategory);

        when(dishRepository.findAll()).thenReturn(List.of(dish1, dish2));

        // When
        List<DishDto> result = dishService.findAllDishes();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Pizza");
        assertThat(result.get(1).getName()).isEqualTo("Sushi");
        verify(dishRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyList_whenNoDishesExist() {
        // Given
        when(dishRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<DishDto> result = dishService.findAllDishes();

        // Then
        assertThat(result).isEmpty();
        verify(dishRepository, times(1)).findAll();
    }


    @Test
    void shouldReturnDish_whenDishExists() {
        // Given
        Category italianCategory = new Category(1L, "Italian");
        Dish dish = new Dish(1L, "Pizza", "mąka, oliwa", "połącz składniki", italianCategory);
        DishDto expectedDishDto = new DishDto(1L, "Pizza", "mąka, oliwa", "połącz składniki","Italian");

        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        // When
        Optional<DishDto> result = dishService.findDishById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Pizza");
        assertThat(result.get().getCategory()).isEqualTo("Italian");
        verify(dishRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmptyOptional_whenDishDoesNotExist() {
        // Given
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<DishDto> result = dishService.findDishById(1L);

        // Then
        assertThat(result).isEmpty();
        verify(dishRepository, times(1)).findById(1L);
    }
}
