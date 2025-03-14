package pl.adamik.mealplanner.domain.dishselection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionDto;
import pl.adamik.mealplanner.domain.user.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DishSelectionMapperTest {
    private DishSelectionMapper dishSelectionMapper;

    @BeforeEach
    void setUp() {
        dishSelectionMapper = new DishSelectionMapper();
    }

    @Test
    void shouldMapDishSelectionsToDishSelectionDto() {
        // Given
        LocalDate date1 = LocalDate.of(2025, 3, 5);
        LocalDate date2 = LocalDate.of(2025, 3, 6);

        Category category1 = new Category(1L, "Śniadanie");
        Category category2 = new Category(2L, "Obiad");

        Dish dish1 = new Dish();
        dish1.setId(101L);
        dish1.setName("Jajecznica");
        dish1.setCategory(category1);

        Dish dish2 = new Dish();
        dish2.setId(102L);
        dish2.setName("Spaghetti");
        dish2.setCategory(category2);

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        DishSelection dishSelection1 = new DishSelection(user, dish1, date1);
        DishSelection dishSelection2 = new DishSelection(user, dish2, date2);

        List<DishSelection> dishSelections = List.of(dishSelection1, dishSelection2);
        List<Category> categories = List.of(category1, category2);

        // When
        List<DishSelectionDto> result = dishSelectionMapper.map(dishSelections, categories);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        DishSelectionDto dto1 = result.get(0);
        assertEquals(date1, dto1.date());
        assertEquals(1, dto1.categories().size());
        assertEquals("Śniadanie", dto1.categories().get(0).getCategoryDto().getName());
        assertEquals(1, dto1.categories().get(0).getDishes().size());
        assertEquals("Jajecznica", dto1.categories().get(0).getDishes().get(0).name());

        DishSelectionDto dto2 = result.get(1);
        assertEquals(date2, dto2.date());
        assertEquals(1, dto2.categories().size());
        assertEquals("Obiad", dto2.categories().get(0).getCategoryDto().getName());
        assertEquals(1, dto2.categories().get(0).getDishes().size());
        assertEquals("Spaghetti", dto2.categories().get(0).getDishes().get(0).name());
    }

    @Test
    void shouldReturnEmptyListWhenNoDishSelections() {
        // When
        List<DishSelectionDto> result = dishSelectionMapper.map(Collections.emptyList(), Collections.emptyList());

        // Then
        assertThat(result).isEmpty();
    }
}
