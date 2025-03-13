package pl.adamik.mealplanner.domain.favorite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import pl.adamik.mealplanner.domain.user.User;

interface FavoriteRepository extends CrudRepository<Favorite, Long> {
    Page<Favorite> findByUser(User user, Pageable pageable);
    Favorite findByUser_EmailAndDish_Id(String userEmail, Long dishId);
    void removeFavoriteByDish_IdAndUser_Email(Long dishId, String currentUserEmail);
}
