package pl.adamik.mealplanner.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

import java.util.Optional;

@Controller
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/danie/{id}")
    public String getDish(@PathVariable long id, Model model) {
        Optional<DishDto> optionalDishDto = dishService.findDishById(id);
        optionalDishDto.ifPresent(dish -> model.addAttribute("dish", dish));
        return "dish";
    }
}
