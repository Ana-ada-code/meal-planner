package pl.adamik.mealplanner.domain.dish;

import org.springframework.stereotype.Service;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<DishDto> findAllDishes() {
        return dishRepository.findAll().stream()
                .map(DishDtoMapper::map)
                .toList();
    }
}
