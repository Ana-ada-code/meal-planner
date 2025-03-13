package pl.adamik.mealplanner.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String getCategory(@PathVariable String name,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model) {
        CategoryDto category = categoryService.findCategoryByName(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<DishDto> dishes = dishService.findDishesByCategoryName(name, pageable);
        String url = "/kategoria/" + name;
        model.addAttribute("heading", category.getName());
        model.addAttribute("dishes", dishes);
        model.addAttribute("url", url);
        return "dish-listing";
    }

    @GetMapping("/kategorie-dan")
    public String getCategoryList(Model model) {
        List<CategoryDto> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "category-listing";
    }
}
