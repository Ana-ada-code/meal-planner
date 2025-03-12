package pl.adamik.mealplanner.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

@Controller
public class HomeController {
    private final DishService dishService;

    public HomeController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DishDto> dishes = dishService.findAllDishes(pageable);
        model.addAttribute("heading", "Lista dań");
        model.addAttribute("dishes", dishes);
        model.addAttribute("currentUrl", "/");
        return "dish-listing";
    }

//    @GetMapping("/page")
//    public String page(@RequestParam(defaultValue = "0") int page,
//                       @RequestParam(defaultValue = "5") int size,
//                       Model model) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<DishDto> dishes = dishService.findAllDishes(pageable);
//        model.addAttribute("heading", "Lista dań");
//        model.addAttribute("dishes", dishes);
//        model.addAttribute("currentUrl", "/page");  // Przykład: "/page"
//        return "dish-listing";
//    }

}

