package pl.adamik.mealplanner.domain.rating;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.dish.DishRepository;
import pl.adamik.mealplanner.domain.user.User;
import pl.adamik.mealplanner.domain.user.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void shouldAddNewRatingWhenNoExistingRating() {
        // Given
        String userEmail = "test@example.com";
        long dishId = 1L;
        int ratingValue = 5;

        User user = new User();
        user.setEmail(userEmail);

        Dish dish = new Dish();
        dish.setId(dishId);

        when(ratingRepository.findByUser_EmailAndDish_Id(userEmail, dishId)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        // When
        ratingService.addOrUpdateRating(userEmail, dishId, ratingValue);

        // Then
        verify(ratingRepository, times(1)).save(argThat(r ->
                r.getUser().equals(user) &&
                        r.getDish().equals(dish) &&
                        r.getRating() == ratingValue));
    }

    @Test
    void shouldUpdateExistingRating() {
        // Given
        String userEmail = "test@example.com";
        long dishId = 1L;
        int newRatingValue = 4;

        User user = new User();
        user.setEmail(userEmail);

        Dish dish = new Dish();
        dish.setId(dishId);

        Rating existingRating = new Rating();
        existingRating.setUser(user);
        existingRating.setDish(dish);
        existingRating.setRating(3);

        when(ratingRepository.findByUser_EmailAndDish_Id(userEmail, dishId)).thenReturn(Optional.of(existingRating));
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));

        // When
        ratingService.addOrUpdateRating(userEmail, dishId, newRatingValue);

        // Then
        assertThat(existingRating.getRating()).isEqualTo(newRatingValue);
        verify(ratingRepository, times(1)).save(existingRating);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        String userEmail = "nonexistent@example.com";
        long dishId = 1L;
        int ratingValue = 5;

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> ratingService.addOrUpdateRating(userEmail, dishId, ratingValue))
                .isInstanceOf(RuntimeException.class);
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDishNotFound() {
        // Given
        String userEmail = "test@example.com";
        long dishId = 1L;
        int ratingValue = 5;

        User user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(dishRepository.findById(dishId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> ratingService.addOrUpdateRating(userEmail, dishId, ratingValue))
                .isInstanceOf(RuntimeException.class);
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void shouldReturnUserRatingForDishIfExists() {
        // Given
        String userEmail = "test@example.com";
        long dishId = 1L;
        int ratingValue = 4;

        Rating rating = new Rating();
        rating.setRating(ratingValue);

        when(ratingRepository.findByUser_EmailAndDish_Id(userEmail, dishId)).thenReturn(Optional.of(rating));

        // When
        Optional<Integer> result = ratingService.getUserRatingForDish(userEmail, dishId);

        // Then
        assertThat(result).isPresent().contains(ratingValue);
    }

    @Test
    void shouldReturnEmptyWhenUserHasNotRatedDish() {
        // Given
        String userEmail = "test@example.com";
        long dishId = 1L;

        when(ratingRepository.findByUser_EmailAndDish_Id(userEmail, dishId)).thenReturn(Optional.empty());

        // When
        Optional<Integer> result = ratingService.getUserRatingForDish(userEmail, dishId);

        // Then
        assertThat(result).isEmpty();
    }
}
