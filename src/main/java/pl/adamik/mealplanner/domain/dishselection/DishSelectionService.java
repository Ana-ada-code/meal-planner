package pl.adamik.mealplanner.domain.dishselection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.category.CategoryRepository;
import pl.adamik.mealplanner.domain.dish.Dish;
import pl.adamik.mealplanner.domain.dish.DishRepository;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionDto;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionSaveDto;
import pl.adamik.mealplanner.domain.user.User;
import pl.adamik.mealplanner.domain.user.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DishSelectionService {
    private final DishSelectionRepository dishSelectionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DishSelectionMapper dishSelectionMapper;
    private final DishRepository dishRepository;

    public DishSelectionService(DishSelectionRepository dishSelectionRepository, UserRepository userRepository,
                                DishSelectionMapper dishSelectionMapper, CategoryRepository categoryRepository,
                                DishRepository dishRepository) {
        this.dishSelectionRepository = dishSelectionRepository;
        this.userRepository = userRepository;
        this.dishSelectionMapper = dishSelectionMapper;
        this.categoryRepository = categoryRepository;
        this.dishRepository = dishRepository;
    }

    public List<DishSelectionDto> getSelectionsGroupedByDateAndCategory(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        List<DishSelection> dishSelections = dishSelectionRepository.findDishSelectionByUser_IdFromToday(user.getId());

        List<Category> categories = categoryRepository.findAllByOrderByIdAsc();

        return dishSelectionMapper.map(dishSelections, categories);
    }

    @Transactional
    public boolean addDishToPlanner(DishSelectionSaveDto dishSelectionSaveDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Dish dish = dishRepository.findById(dishSelectionSaveDto.getDishId()).orElseThrow();

        DishSelection dishSelection = new DishSelection();
        dishSelection.setUser(user);
        dishSelection.setDish(dish);
        dishSelection.setDate(dishSelectionSaveDto.getSelectedDate());

        dishSelectionRepository.save(dishSelection);
        return true;
    }

    @Transactional
    public boolean removeAll() {
        dishSelectionRepository.deleteAll();
        return true;
    }


    @Transactional
    public boolean removeDay(LocalDate date) {
        dishSelectionRepository.deleteAllByDate(date);
        return true;
    }

    @Transactional
    public boolean removeDish(Long id) {
        dishSelectionRepository.deleteById(id);
        return true;
    }

    @Transactional
    public boolean update(DishSelectionSaveDto dishSelectionDto) {
        DishSelection dishSelection = dishSelectionRepository.findById(dishSelectionDto.getDishId()).orElseThrow();

        if (dishSelectionDto.getSelectedDate() != null) {
            dishSelection.setDate(dishSelectionDto.getSelectedDate());
        }

        dishSelectionRepository.save(dishSelection);
        return true;
    }
}
