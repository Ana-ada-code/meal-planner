package pl.adamik.mealplanner.domain.dishselection;

import org.springframework.stereotype.Service;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.category.CategoryRepository;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionDto;
import pl.adamik.mealplanner.domain.user.User;
import pl.adamik.mealplanner.domain.user.UserRepository;

import java.util.List;

@Service
public class DishSelectionService {
    private final DishSelectionRepository dishSelectionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DishSelectionMapper dishSelectionMapper;

    public DishSelectionService(DishSelectionRepository dishSelectionRepository, UserRepository userRepository,
                                DishSelectionMapper dishSelectionMapper, CategoryRepository categoryRepository) {
        this.dishSelectionRepository = dishSelectionRepository;
        this.userRepository = userRepository;
        this.dishSelectionMapper = dishSelectionMapper;
        this.categoryRepository = categoryRepository;
    }

    public List<DishSelectionDto> getSelectionsGroupedByDateAndCategory(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<DishSelection> dishSelections = dishSelectionRepository.findDishSelectionByUser_IdForNext7Days(user.getId());

        List<Category> categories = categoryRepository.findAllByOrderByIdAsc();

        return dishSelectionMapper.map(dishSelections, categories);
    }
}
