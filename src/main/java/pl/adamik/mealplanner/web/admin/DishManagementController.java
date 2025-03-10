package pl.adamik.mealplanner.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.adamik.mealplanner.domain.category.CategoryService;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishSaveDto;

import java.util.List;

@Controller
public class DishManagementController {
    private final DishService dishService;
    private final CategoryService categoryService;

    public DishManagementController(DishService dishService, CategoryService categoryService) {
        this.dishService = dishService;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/dodaj-danie")
    public String addDishForm(Model model) {
        List<CategoryDto> allCategories = categoryService.findAllCategories();
        model.addAttribute("categories", allCategories);
        DishSaveDto dish = new DishSaveDto();
        model.addAttribute("dish", dish);
        return "admin/dish-form";
    }

    @PostMapping("/admin/dodaj-danie")
    public String addDish(DishSaveDto dish, RedirectAttributes redirectAttributes) {
        dishService.addDish(dish);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Danie %s zostało zapisane".formatted(dish.getName()));
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/usun-danie")
    public String removeCategory(@RequestParam Long dishId, RedirectAttributes redirectAttributes) {
        dishService.deleteDish(dishId);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Danie zastała usunięta");
        return "redirect:/admin";
    }
}
