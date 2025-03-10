package pl.adamik.mealplanner.domain.dish;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.category.CategoryRepository;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.dish.dto.DishSaveDto;
import pl.adamik.mealplanner.storage.FileStorageService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private DishService dishService;

    @Test
    void shouldReturnAllDishes_whenDishesExist() {
        // Given
        Category italianCategory = new Category();
        italianCategory.setId(1L);
        italianCategory.setName("Italian");

        Category japaneseCategory = new Category();
        japaneseCategory.setId(2L);
        japaneseCategory.setName("Japanese");

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Pizza");
        dish1.setIngredients("mąka, oliwa");
        dish1.setRecipe("połącz składniaki");
        dish1.setCategory(italianCategory);
        dish1.setImage("https://example.com/image.jpg");

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Sushi");
        dish2.setIngredients("ryż, ryba");
        dish2.setRecipe("połącz składniki");
        dish2.setCategory(japaneseCategory);
        dish2.setImage("https://example.com/image2.jpg");

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
        Category italianCategory = new Category();
        italianCategory.setId(1L);
        italianCategory.setName("Italian");

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setIngredients("mąka, oliwa");
        dish.setRecipe("połącz składniki");
        dish.setCategory(italianCategory);
        dish.setImage("https://example.com/image.jpg");

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


    @Test
    void shouldAddDish_whenDishIsValid() {
        // Given
        MultipartFile mockImage = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[10]);
        DishSaveDto dishSaveDto = new DishSaveDto();
        dishSaveDto.setName("Pizza");
        dishSaveDto.setIngredients("mąka, oliwa");
        dishSaveDto.setRecipe("połącz składniki");
        dishSaveDto.setCategory("Italian");
        dishSaveDto.setImage(mockImage);
        Category category = new Category(1L, "Italian");

        when(categoryRepository.findByNameIgnoreCase("Italian")).thenReturn(Optional.of(category));
        when(fileStorageService.saveImage(mockImage)).thenReturn("saved_image.jpg");

        // When
        dishService.addDish(dishSaveDto);

        // Then
        verify(categoryRepository, times(1)).findByNameIgnoreCase("Italian");
        verify(fileStorageService, times(1)).saveImage(mockImage);
        verify(dishRepository, times(1)).save(argThat(dish ->
                dish.getName().equals("Pizza") &&
                        dish.getIngredients().equals("mąka, oliwa") &&
                        dish.getRecipe().equals("połącz składniki") &&
                        dish.getCategory().getName().equals("Italian") &&
                        dish.getImage().equals("saved_image.jpg")
        ));
    }

    @Test
    void shouldThrowException_whenCategoryDoesNotExist() {
        // Given
        DishSaveDto dishSaveDto = new DishSaveDto();
        dishSaveDto.setName("Pizza");
        dishSaveDto.setIngredients("mąka, oliwa");
        dishSaveDto.setRecipe("połącz składniki");
        dishSaveDto.setCategory("NonExistentCategory");

        when(categoryRepository.findByNameIgnoreCase("NonExistentCategory")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> dishService.addDish(dishSaveDto))
                .isInstanceOf(RuntimeException.class);

        verify(categoryRepository, times(1)).findByNameIgnoreCase("NonExistentCategory");
        verify(dishRepository, never()).save(any());
    }

    @Test
    void shouldSaveDishWithoutImage_whenNoImageProvided() {
        // Given
        DishSaveDto dishSaveDto = new DishSaveDto();
        dishSaveDto.setName("Pizza");
        dishSaveDto.setIngredients("mąka, oliwa");
        dishSaveDto.setRecipe("połącz składniki");
        dishSaveDto.setCategory("Italian");
        dishSaveDto.setImage(null);
        Category category = new Category(1L, "Italian");

        when(categoryRepository.findByNameIgnoreCase("Italian")).thenReturn(Optional.of(category));

        // When
        dishService.addDish(dishSaveDto);

        // Then
        verify(categoryRepository, times(1)).findByNameIgnoreCase("Italian");
        verify(fileStorageService, never()).saveImage(any());
        verify(dishRepository, times(1)).save(argThat(dish ->
                dish.getName().equals("Pizza") &&
                        dish.getIngredients().equals("mąka, oliwa") &&
                        dish.getRecipe().equals("połącz składniki") &&
                        dish.getCategory().getName().equals("Italian") &&
                        dish.getImage() == null
        ));
    }

    @Test
    void shouldReturnTopDishes_whenDishesExist() {
        // Given
        Category italianCategory = new Category();
        italianCategory.setId(1L);
        italianCategory.setName("Italian");

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Pizza");
        dish1.setIngredients("mąka, oliwa");
        dish1.setRecipe("połącz składniki");
        dish1.setCategory(italianCategory);
        dish1.setImage("https://example.com/image.jpg");

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Pasta");
        dish2.setIngredients("mąka, woda");
        dish2.setRecipe("ugotuj makaron");
        dish2.setCategory(italianCategory);
        dish2.setImage("https://example.com/image2.jpg");

        when(dishRepository.findTopByRating(Pageable.ofSize(2))).thenReturn(List.of(dish1, dish2));

        // When
        List<DishDto> result = dishService.findTopDishes(2);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Pizza");
        assertThat(result.get(1).getName()).isEqualTo("Pasta");
        verify(dishRepository, times(1)).findTopByRating(Pageable.ofSize(2));
    }

    @Test
    void shouldReturnEmptyList_whenNoTopDishesExist() {
        // Given
        when(dishRepository.findTopByRating(Pageable.ofSize(3))).thenReturn(Collections.emptyList());

        // When
        List<DishDto> result = dishService.findTopDishes(3);

        // Then
        assertThat(result).isEmpty();
        verify(dishRepository, times(1)).findTopByRating(Pageable.ofSize(3));
    }

    @Test
    void shouldReturnCorrectNumberOfTopDishes() {
        // Given
        Category italianCategory = new Category();
        italianCategory.setId(1L);
        italianCategory.setName("Italian");

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Pizza");
        dish1.setIngredients("mąka, oliwa");
        dish1.setRecipe("połącz składniki");
        dish1.setCategory(italianCategory);
        dish1.setImage("https://example.com/image.jpg");

        when(dishRepository.findTopByRating(Pageable.ofSize(1))).thenReturn(List.of(dish1));

        // When
        List<DishDto> result = dishService.findTopDishes(1);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Pizza");
        verify(dishRepository, times(1)).findTopByRating(Pageable.ofSize(1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "null"})
    void shouldReturnAllDishes_whenKeywordIsNullOrEmpty(String keyword) {
        // Given
        Category italianCategory = new Category();
        italianCategory.setId(1L);
        italianCategory.setName("Italian");

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Pizza");
        dish1.setIngredients("mąka, oliwa");
        dish1.setRecipe("połącz składniki");
        dish1.setCategory(italianCategory);
        dish1.setImage("https://example.com/image.jpg");

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Pasta");
        dish2.setIngredients("mąka, woda");
        dish2.setRecipe("ugotuj makaron");
        dish2.setCategory(italianCategory);
        dish2.setImage("https://example.com/image2.jpg");

        when(dishRepository.findAll()).thenReturn(List.of(dish1, dish2));

        // When
        List<DishDto> result = dishService.searchDishes(keyword.equals("null") ? null : keyword);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Pizza");
        assertThat(result.get(1).getName()).isEqualTo("Pasta");

        verify(dishRepository, times(1)).findAll();
    }


    @Test
    void shouldReturnFilteredDishes_whenKeywordMatchesPartOfName() {
        // Given
        Category italianCategory = new Category();
        italianCategory.setId(1L);
        italianCategory.setName("Italian");

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Pizza");
        dish1.setIngredients("mąka, oliwa");
        dish1.setRecipe("połącz składniki");
        dish1.setCategory(italianCategory);
        dish1.setImage("https://example.com/image.jpg");

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Pasta");
        dish2.setIngredients("mąka, woda");
        dish2.setRecipe("ugotuj makaron");
        dish2.setCategory(italianCategory);
        dish2.setImage("https://example.com/image2.jpg");

        when(dishRepository.findByNameContainingIgnoreCase("Pi")).thenReturn(List.of(dish1));

        // When
        List<DishDto> result = dishService.searchDishes("Pi");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Pizza");

        verify(dishRepository, times(1)).findByNameContainingIgnoreCase("Pi");
    }

    @Test
    void shouldReturnEmptyList_whenNoDishesMatchKeyword() {
        // Given
        when(dishRepository.findByNameContainingIgnoreCase("NonExistentDish")).thenReturn(Collections.emptyList());

        // When
        List<DishDto> result = dishService.searchDishes("NonExistentDish");

        // Then
        assertThat(result).isEmpty();
        verify(dishRepository, times(1)).findByNameContainingIgnoreCase("NonExistentDish");
    }

    @Test
    void shouldReturnFilteredDishes_whenKeywordContainsSpecialCharacters() {
        // Given
        Category italianCategory = new Category();
        italianCategory.setId(1L);
        italianCategory.setName("Italian");

        Dish dish1 = new Dish();
        dish1.setId(1L);
        dish1.setName("Pizza Margherita");
        dish1.setIngredients("mąka, oliwa");
        dish1.setRecipe("połącz składniki");
        dish1.setCategory(italianCategory);
        dish1.setImage("https://example.com/image.jpg");

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Pasta Carbonara");
        dish2.setIngredients("mąka, jajka");
        dish2.setRecipe("ugotuj makaron");
        dish2.setCategory(italianCategory);
        dish2.setImage("https://example.com/image2.jpg");

        when(dishRepository.findByNameContainingIgnoreCase("Pasta C")).thenReturn(List.of(dish2));

        // When
        List<DishDto> result = dishService.searchDishes("Pasta C");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Pasta Carbonara");

        verify(dishRepository, times(1)).findByNameContainingIgnoreCase("Pasta C");
    }



}
