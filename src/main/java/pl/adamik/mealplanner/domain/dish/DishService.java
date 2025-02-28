package pl.adamik.mealplanner.domain.dish;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.category.CategoryRepository;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.dish.dto.DishSaveDto;
import pl.adamik.mealplanner.storage.FileStorageService;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    public DishService(DishRepository dishRepository, CategoryRepository categoryRepository, FileStorageService fileStorageService) {
        this.dishRepository = dishRepository;
        this.categoryRepository = categoryRepository;
        this.fileStorageService = fileStorageService;
    }


    public List<DishDto> findAllDishes() {
        return StreamSupport.stream(dishRepository.findAll().spliterator(), false)
                .map(DishDtoMapper::map)
                .toList();
    }

    public Optional<DishDto> findDishById(Long id) {
        return dishRepository.findById(id).map(DishDtoMapper::map);
    }

    public List<DishDto> findDishesByCategoryName(String category) {
        return dishRepository.findAllByCategory_NameIgnoreCase(category).stream()
                .map(DishDtoMapper::map)
                .toList();
    }

    public void addDish(DishSaveDto dishToSave) {
        Dish dish = new Dish();
        dish.setName(dishToSave.getName());
        dish.setIngredients(dishToSave.getIngredients());
        dish.setRecipe(dishToSave.getRecipe());
        Category category = categoryRepository.findByNameIgnoreCase(dishToSave.getCategory()).orElseThrow();
        dish.setCategory(category);
        if (dishToSave.getImage() != null && !dishToSave.getImage().isEmpty()) {
            String savedFileName = fileStorageService.saveImage(dishToSave.getImage());
            dish.setImage(savedFileName);
        }
        dishRepository.save(dish);
    }

    public List<DishDto> findTopDishes(int size) {
        Pageable page = Pageable.ofSize(size);
        return dishRepository.findTopByRating(page).stream()
                .map(DishDtoMapper::map)
                .toList();
    }
}
