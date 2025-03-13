package pl.adamik.mealplanner.domain.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.dish.DishRepository;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.favorite.dto.FavoriteDto;
import pl.adamik.mealplanner.domain.user.User;
import pl.adamik.mealplanner.domain.user.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private User mockUser;
    private Dish mockDish;
    private FavoriteDto favoriteDto;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        mockDish = new Dish();
        mockDish.setId(10L);

        favoriteDto = new FavoriteDto(10L);
    }

    @Test
    void testFindFavorite() {
        // GIVEN
        Pageable pageable = mock(Pageable.class);
        Favorite favorite = new Favorite();
        favorite.setUser(mockUser);
        favorite.setDish(mockDish);

        Category mockCategory = new Category();
        mockCategory.setName("Obiady");
        mockDish.setCategory(mockCategory);

        Page<Favorite> favoritePage = new PageImpl<>(Collections.singletonList(favorite));

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(favoriteRepository.findByUser(mockUser, pageable)).thenReturn(favoritePage);

        // WHEN
        Page<DishDto> result = favoriteService.findFavorite("test@example.com", pageable);

        // THEN
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(userRepository).findByEmail("test@example.com");
        verify(favoriteRepository).findByUser(mockUser, pageable);
    }

    @Test
    void testGetUserFavoriteForDish_Exists() {
        // GIVEN
        when(favoriteRepository.findByUser_EmailAndDish_Id("test@example.com", 10L)).thenReturn(new Favorite());

        // WHEN
        boolean result = favoriteService.getUserFavoriteForDish("test@example.com", 10L);

        // THEN
        assertTrue(result);
        verify(favoriteRepository).findByUser_EmailAndDish_Id("test@example.com", 10L);
    }


    @Test
    void testGetUserFavoriteForDish_NotExists() {
        // GIVEN
        when(favoriteRepository.findByUser_EmailAndDish_Id("test@example.com", 10L)).thenReturn(null);

        // WHEN
        boolean result = favoriteService.getUserFavoriteForDish("test@example.com", 10L);

        // THEN
        assertFalse(result);
        verify(favoriteRepository).findByUser_EmailAndDish_Id("test@example.com", 10L);
    }

    @Test
    void testAddFavoriteToDish_Success() {
        // GIVEN
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(dishRepository.findById(10L)).thenReturn(Optional.of(mockDish));

        // WHEN
        boolean result = favoriteService.addFavoriteToDish(favoriteDto, "test@example.com");

        // THEN
        assertTrue(result);
        verify(userRepository).findByEmail("test@example.com");
        verify(dishRepository).findById(10L);
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void testAddFavoriteToDish_UserNotFound() {
        // GIVEN
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> favoriteService.addFavoriteToDish(favoriteDto, "test@example.com"));

        verify(userRepository).findByEmail("test@example.com");
        verifyNoInteractions(dishRepository);
        verifyNoInteractions(favoriteRepository);
    }

    @Test
    void testAddFavoriteToDish_DishNotFound() {
        // GIVEN
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(dishRepository.findById(10L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> favoriteService.addFavoriteToDish(favoriteDto, "test@example.com"));

        verify(userRepository).findByEmail("test@example.com");
        verify(dishRepository).findById(10L);
        verifyNoInteractions(favoriteRepository);
    }

    @Test
    void testRemoveFavoriteFromDish() {
        // WHEN
        boolean result = favoriteService.removeFavoriteFromDish(favoriteDto, "test@example.com");

        // THEN
        assertTrue(result);
        verify(favoriteRepository).removeFavoriteByDish_IdAndUser_Email(10L, "test@example.com");
    }
}
