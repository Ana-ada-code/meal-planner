package pl.adamik.mealplanner.domain.dish;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
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
import static org.junit.jupiter.api.Assertions.*;
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
        assertThat(result.get(0).name()).isEqualTo("Pizza");
        assertThat(result.get(1).name()).isEqualTo("Sushi");
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
        assertThat(result.get().name()).isEqualTo("Pizza");
        assertThat(result.get().category()).isEqualTo("Italian");
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
        Category category = Category.builder()
                .id(1L)
                .name("Italian")
                .build();

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
        Category category = Category.builder()
                .id(1L)
                .name("Italian")
                .build();

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

        Pageable pageable = PageRequest.of(0, 2);
        Page<Dish> dishPage = new PageImpl<>(List.of(dish1, dish2));

        when(dishRepository.findTopByRating(any(Pageable.class))).thenReturn(dishPage);

        // When
        Page<DishDto> result = dishService.findTopDishes(2, pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).name()).isEqualTo("Pizza");
        assertThat(result.getContent().get(1).name()).isEqualTo("Pasta");
        verify(dishRepository, times(1)).findTopByRating(any(Pageable.class));
    }

    @Test
    void shouldReturnEmptyPage_whenNoTopDishesExist() {
        // Given
        Pageable pageable = PageRequest.of(0, 3);
        Page<Dish> emptyPage = Page.empty(pageable);

        when(dishRepository.findTopByRating(pageable)).thenReturn(emptyPage);

        // When
        Page<DishDto> result = dishService.findTopDishes(3, pageable);

        // Then
        assertThat(result).isEmpty();
        verify(dishRepository, times(1)).findTopByRating(pageable);
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

        Pageable pageable = PageRequest.of(0, 1);
        Page<Dish> dishPage = new org.springframework.data.domain.PageImpl<>(List.of(dish1), pageable, 1);

        when(dishRepository.findTopByRating(pageable)).thenReturn(dishPage);

        // When
        Page<DishDto> result = dishService.findTopDishes(1, pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Pizza");
        verify(dishRepository, times(1)).findTopByRating(pageable);
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

        Pageable pageable = PageRequest.of(0, 10);
        Page<Dish> dishPage = new org.springframework.data.domain.PageImpl<>(List.of(dish1, dish2), pageable, 2);

        when(dishRepository.findAll(pageable)).thenReturn(dishPage);

        // When
        Page<DishDto> result = dishService.searchDishes(keyword.equals("null") ? null : keyword, pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent().get(0).name()).isEqualTo("Pizza");
        assertThat(result.getContent().get(1).name()).isEqualTo("Pasta");

        verify(dishRepository, times(1)).findAll(pageable);
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

        Pageable pageable = PageRequest.of(0, 10);
        Page<Dish> dishPage = new org.springframework.data.domain.PageImpl<>(List.of(dish1), pageable, 1);

        when(dishRepository.findByNameContainingIgnoreCase("Pi", pageable)).thenReturn(dishPage);

        // When
        Page<DishDto> result = dishService.searchDishes("Pi", pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Pizza");

        verify(dishRepository, times(1)).findByNameContainingIgnoreCase("Pi", pageable);
    }

    @Test
    void shouldReturnEmptyList_whenNoDishesMatchKeyword() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        when(dishRepository.findByNameContainingIgnoreCase("NonExistentDish", pageable))
                .thenReturn(Page.empty());

        // When
        Page<DishDto> result = dishService.searchDishes("NonExistentDish", pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getContent()).isEmpty();

        verify(dishRepository, times(1)).findByNameContainingIgnoreCase("NonExistentDish", pageable);
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

        Pageable pageable = PageRequest.of(0, 10);

        when(dishRepository.findByNameContainingIgnoreCase("Pasta C", pageable))
                .thenReturn(new PageImpl<>(List.of(dish2), pageable, 1));

        // When
        Page<DishDto> result = dishService.searchDishes("Pasta C", pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Pasta Carbonara");

        verify(dishRepository, times(1)).findByNameContainingIgnoreCase("Pasta C", pageable);
    }

    @Test
    void shouldReturnDishesByCategory_whenCategoryExists() {
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

        when(dishRepository.findAllByCategory_NameIgnoreCase("Italian")).thenReturn(List.of(dish1));

        // When
        List<DishDto> result = dishService.findDishesByCategoryName("Italian");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Pizza");
        verify(dishRepository, times(1)).findAllByCategory_NameIgnoreCase("Italian");
    }

    @Test
    void shouldReturnEmptyList_whenCategoryHasNoDishes() {
        // Given
        when(dishRepository.findAllByCategory_NameIgnoreCase("NonExistentCategory")).thenReturn(Collections.emptyList());

        // When
        List<DishDto> result = dishService.findDishesByCategoryName("NonExistentCategory");

        // Then
        assertThat(result).isEmpty();
        verify(dishRepository, times(1)).findAllByCategory_NameIgnoreCase("NonExistentCategory");
    }

    @Test
    void shouldDeleteDish_whenDishExists() {
        // Given
        Long dishId = 1L;
        doNothing().when(dishRepository).deleteById(dishId);

        // When
        boolean result = dishService.deleteDish(dishId);

        // Then
        assertThat(result).isTrue();
        verify(dishRepository, times(1)).deleteById(dishId);
    }

    @Test
    void shouldHandleDeleteDish_whenDishDoesNotExist() {
        // Given
        Long dishId = 1L;
        doNothing().when(dishRepository).deleteById(dishId);

        // When
        boolean result = dishService.deleteDish(dishId);

        // Then
        assertThat(result).isTrue();
        verify(dishRepository, times(1)).deleteById(dishId);
    }

    @Test
    void shouldUpdateDish_whenDishExists() {
        // Given
        Long dishId = 1L;
        Dish existingDish = new Dish();
        existingDish.setId(dishId);
        existingDish.setName("Old Name");
        existingDish.setIngredients("Old Ingredients");
        existingDish.setRecipe("Old Recipe");
        existingDish.setCategory(Category.builder().id(1L).name("Italian").build());

        DishSaveDto dishSaveDto = new DishSaveDto();
        dishSaveDto.setName("New Name");
        dishSaveDto.setIngredients("New Ingredients");
        dishSaveDto.setRecipe("New Recipe");
        dishSaveDto.setCategory("Italian");
        dishSaveDto.setImage(null);

        when(dishRepository.findById(dishId)).thenReturn(Optional.of(existingDish));
        when(categoryRepository.findByNameIgnoreCase("Italian")).thenReturn(Optional.of(Category.builder().id(1L).name("Italian").build()));

        // When
        dishService.updateDish(dishSaveDto, dishId);

        // Then
        assertThat(existingDish.getName()).isEqualTo("New Name");
        assertThat(existingDish.getIngredients()).isEqualTo("New Ingredients");
        assertThat(existingDish.getRecipe()).isEqualTo("New Recipe");
        assertThat(existingDish.getCategory().getName()).isEqualTo("Italian");
        verify(dishRepository, times(1)).save(existingDish);
    }

    @Test
    void shouldThrowException_whenDishDoesNotExist() {
        // Given
        Long nonExistentDishId = 999L;
        DishSaveDto dishToUpdate = new DishSaveDto();
        dishToUpdate.setName("Updated Dish");
        dishToUpdate.setIngredients("Updated Ingredients");
        dishToUpdate.setRecipe("Updated Recipe");
        dishToUpdate.setCategory("Category");
        dishToUpdate.setImage(null);

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            dishService.updateDish(dishToUpdate, nonExistentDishId);
        });
    }
}
