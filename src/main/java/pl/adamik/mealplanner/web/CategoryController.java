package pl.adamik.mealplanner.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import pl.adamik.mealplanner.domain.category.CategoryService;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;

import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final DishService dishService;

    public CategoryController(CategoryService categoryService, DishService dishService) {
        this.categoryService = categoryService;
        this.dishService = dishService;
    }

    @GetMapping("/kategoria/{name}")
    public String getCategory(@PathVariable String name, Model model) {
        CategoryDto category = categoryService.findCategoryByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<DishDto> dishes = dishService.findDishesByCategoryName(name);
        model.addAttribute("heading", category.getName());
        model.addAttribute("dishes", dishes);
        return "dish-listing";
    }
}
