package pl.adamik.mealplanner.domain.favorite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.adamik.mealplanner.domain.dish.DishDtoMapper;
import pl.adamik.mealplanner.domain.dish.DishRepository;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.favorite.dto.FavoriteDto;
import pl.adamik.mealplanner.domain.user.User;
import pl.adamik.mealplanner.domain.user.UserRepository;

@Service
public class FavoriteService {
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final DishRepository dishRepository;

    public FavoriteService(UserRepository userRepository, FavoriteRepository favoriteRepository, DishRepository dishRepository) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.dishRepository = dishRepository;
    }

    public Page<DishDto> findFavorite(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        return favoriteRepository.findByUser(user, pageable)
                .map(Favorite::getDish)
                .map(DishDtoMapper::map);
    }

    public boolean getUserFavoriteForDish(String userEmail, long dishId) {
        return favoriteRepository.findByUser_EmailAndDish_Id(userEmail, dishId) != null;

    }

    @Transactional
    public boolean addFavoriteToDish(FavoriteDto favoriteDto, String currentUserEmail) {
        Favorite favorite = new Favorite();
        favorite.setUser(userRepository.findByEmail(currentUserEmail).orElseThrow());
        favorite.setDish(dishRepository.findById(favoriteDto.dishId()).orElseThrow());
        favoriteRepository.save(favorite);
        return true;
    }

    @Transactional
    public boolean removeFavoriteFromDish(FavoriteDto favoriteDto, String currentUserEmail) {
        favoriteRepository.removeFavoriteByDish_IdAndUser_Email(favoriteDto.dishId(), currentUserEmail);
        return true;
    }
}
