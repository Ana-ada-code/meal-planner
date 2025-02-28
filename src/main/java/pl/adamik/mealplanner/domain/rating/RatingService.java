package pl.adamik.mealplanner.domain.rating;

import org.springframework.stereotype.Service;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.dish.DishRepository;
import pl.adamik.mealplanner.domain.user.User;
import pl.adamik.mealplanner.domain.user.UserRepository;

import java.util.Optional;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository, DishRepository dishRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
    }

    public void addOrUpdateRating(String userEmail, long dishId, int rating) {
        Rating ratingRoSaveOrUpdate = ratingRepository.findByUser_EmailAndDish_Id(userEmail,dishId)
                .orElseGet(Rating::new);
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Dish dish = dishRepository.findById(dishId).orElseThrow();
        ratingRoSaveOrUpdate.setUser(user);
        ratingRoSaveOrUpdate.setDish(dish);
        ratingRoSaveOrUpdate.setRating(rating);
        ratingRepository.save(ratingRoSaveOrUpdate);
    }

    public Optional<Integer> getUserRatingForDish(String userEmail, long dishId) {
        return ratingRepository.findByUser_EmailAndDish_Id(userEmail, dishId)
                .map(Rating::getRating);
    }
}
