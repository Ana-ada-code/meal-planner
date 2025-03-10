package pl.adamik.mealplanner.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.adamik.mealplanner.domain.category.CategoryService;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;

import java.util.List;

@Controller
class AdminController {
    public static final String NOTIFICATION_ATTRIBUTE = "notification";
    private final CategoryService categoryService;

    public AdminController(CategoryService categoryService) {

        this.categoryService = categoryService;
    }

    @GetMapping("/admin")
    public String getAdminPanel(Model model) {
        List<CategoryDto> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "admin/admin";
    }
}