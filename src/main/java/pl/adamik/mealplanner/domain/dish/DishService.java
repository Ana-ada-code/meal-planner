package pl.adamik.mealplanner.domain.dish;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
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

    public Page<DishDto> findAllDishes(Pageable pageable) {
        return dishRepository.findAll(pageable)
                .map(DishDtoMapper::map);
    }

    public List<DishDto> findAllDishes() {
        return StreamSupport.stream(dishRepository.findAll().spliterator(), false)
                .map(DishDtoMapper::map)
                .toList();
    }

    public Optional<DishDto> findDishById(Long id) {
        return dishRepository.findById(id).map(DishDtoMapper::map);
    }

    public Page<DishDto> findDishesByCategoryName(String category, Pageable pageable) {
        return dishRepository.findAllByCategory_NameIgnoreCase(category, pageable)
                .map(DishDtoMapper::map);
    }

    public List<DishDto> findDishesByCategoryName(String category) {
        return dishRepository.findAllByCategory_NameIgnoreCase(category).stream()
                .map(DishDtoMapper::map)
                .toList();
    }

    @Transactional
    public void addDish(DishSaveDto dishToSave) {
        Dish dish = new Dish();
        updateAndSaveDish(dishToSave, dish);
    }

    public Page<DishDto> findTopDishes(int size, Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), size);
        return dishRepository.findTopByRating(pageRequest)
                .map(DishDtoMapper::map);
    }

    public Page<DishDto> searchDishes(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return findAllDishes(pageable);
        }
        return dishRepository.findByNameContainingIgnoreCase(keyword, pageable)
                .map(DishDtoMapper::map);
    }

    @Transactional
    public boolean deleteDish(Long id) {
        dishRepository.deleteById(id);
        return true;
    }

    @Transactional
    public void updateDish(DishSaveDto dishToSave, Long id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        updateAndSaveDish(dishToSave, dish);
    }

    private void updateAndSaveDish(DishSaveDto dishToSave, Dish dish) {
        dish.setName(dishToSave.getName());
        dish.setIngredients(dishToSave.getIngredients());
        dish.setRecipe(dishToSave.getRecipe());

        Category category = categoryRepository.findByNameIgnoreCase(dishToSave.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("Category not found" + dishToSave.getCategory()));
        dish.setCategory(category);

        if (dishToSave.getImage() != null && !dishToSave.getImage().isEmpty()) {
            String image = fileStorageService.saveFile(dishToSave.getImage());
            dish.setImage(image);
        }
        dishRepository.save(dish);
    }
}

