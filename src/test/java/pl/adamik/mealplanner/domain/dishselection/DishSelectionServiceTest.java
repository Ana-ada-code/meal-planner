package pl.adamik.mealplanner.domain.dishselection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.mealplanner.domain.category.CategoryRepository;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.dish.DishRepository;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionDto;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionSaveDto;
import pl.adamik.mealplanner.domain.user.User;
import pl.adamik.mealplanner.domain.user.UserRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DishSelectionServiceTest {

    @Mock
    private DishSelectionRepository dishSelectionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private DishSelectionMapper dishSelectionMapper;

    @InjectMocks
    private DishSelectionService dishSelectionService;

    private User mockUser;
    private Dish mockDish;
    private DishSelectionSaveDto dishSelectionSaveDto;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        mockDish = new Dish();
        mockDish.setId(10L);

        dishSelectionSaveDto = new DishSelectionSaveDto();
        dishSelectionSaveDto.setDishId(10L);
        dishSelectionSaveDto.setSelectedDate(LocalDate.of(2025, 3, 19));
    }

    @Test
    void testGetSelectionsGroupedByDateAndCategory() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(dishSelectionRepository.findDishSelectionByUser_IdFromToday(mockUser.getId())).thenReturn(Collections.emptyList());
        when(categoryRepository.findAllByOrderByIdAsc()).thenReturn(Collections.emptyList());
        when(dishSelectionMapper.map(Collections.emptyList(), Collections.emptyList())).thenReturn(Collections.emptyList());

        List<DishSelectionDto> result = dishSelectionService.getSelectionsGroupedByDateAndCategory("test@example.com");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail("test@example.com");
        verify(dishSelectionRepository).findDishSelectionByUser_IdFromToday(mockUser.getId());
        verify(categoryRepository).findAllByOrderByIdAsc();
        verify(dishSelectionMapper).map(Collections.emptyList(), Collections.emptyList());
    }

    @Test
    void testAddDishToPlanner_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(dishRepository.findById(10L)).thenReturn(Optional.of(mockDish));

        boolean result = dishSelectionService.addDishToPlanner(dishSelectionSaveDto, "test@example.com");

        assertTrue(result);
        verify(userRepository).findByEmail("test@example.com");
        verify(dishRepository).findById(10L);
        verify(dishSelectionRepository).save(any(DishSelection.class));
    }

    @Test
    void testAddDishToPlanner_UserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> dishSelectionService.addDishToPlanner(dishSelectionSaveDto, "test@example.com"));

        verify(userRepository).findByEmail("test@example.com");
        verifyNoInteractions(dishRepository);
        verifyNoInteractions(dishSelectionRepository);
    }

    @Test
    void testAddDishToPlanner_DishNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(dishRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> dishSelectionService.addDishToPlanner(dishSelectionSaveDto, "test@example.com"));

        verify(userRepository).findByEmail("test@example.com");
        verify(dishRepository).findById(10L);
        verifyNoInteractions(dishSelectionRepository);
    }
}
