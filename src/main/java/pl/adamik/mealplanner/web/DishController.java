package pl.adamik.mealplanner.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

@Controller
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/danie/{id}")
    public String getDish(@PathVariable long id, Model model) {
        DishDto dish = dishService.findDishById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("dish", dish);
        return "dish";
    }

}
