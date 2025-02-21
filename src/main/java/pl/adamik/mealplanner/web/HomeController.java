package pl.adamik.mealplanner.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

import java.util.List;

@Controller
public class HomeController {
    private final DishService dishService;

    public HomeController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<DishDto> dishes = dishService.findAllDishes();
        model.addAttribute("heading", "Lista dań");
        model.addAttribute("dishes", dishes);
        return "dish-listing";
    }
}

