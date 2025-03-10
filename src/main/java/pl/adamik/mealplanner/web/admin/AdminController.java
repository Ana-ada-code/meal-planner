package pl.adamik.mealplanner.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.adamik.mealplanner.domain.category.CategoryService;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

import java.util.ArrayList;
import java.util.List;

@Controller
class AdminController {
    public static final String NOTIFICATION_ATTRIBUTE = "notification";
    private final CategoryService categoryService;
    private final DishService dishService;

    public AdminController(CategoryService categoryService, DishService dishService) {

        this.categoryService = categoryService;
        this.dishService = dishService;
    }

    @GetMapping("/admin")
    public String getAdminPanel(Model model) {
        List<CategoryDto> categories = categoryService.findAllCategories();
        List<DishDto> dishes = dishService.findAllDishes();
        model.addAttribute("categories", categories);
        model.addAttribute("dishes", new ArrayList<>());
        return "admin/admin";
    }

    @GetMapping("/admin/dishes")
    @ResponseBody
    public List<DishDto> getDishesByCategory(@RequestParam("category") String categoryName) {
        return dishService.findDishesByCategoryName(categoryName);
    }
}