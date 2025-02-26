package pl.adamik.mealplanner.domain.dish;

import org.springframework.stereotype.Service;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
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
}
