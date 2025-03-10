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

@Controller
public class CategoryManagementController {
    private final CategoryService categoryService;

    public CategoryManagementController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/dodaj-kategorie")
    public String addCategoryForm(Model model) {
        CategoryDto category = new CategoryDto();
        model.addAttribute("category", category);
        return "admin/category-form";
    }

    @PostMapping("/admin/dodaj-kategorie")
    public String addCategory(CategoryDto category, RedirectAttributes redirectAttributes) {
        categoryService.addCategory(category);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Kategoria %s zastała zapisana".formatted(category.getName()));
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/usun-kategorie")
    public String removeCategory(@RequestParam String categoryName, RedirectAttributes redirectAttributes) {
        categoryService.removeCategory(categoryName);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Kategoria %s zastała usunięta".formatted(categoryName));
        return "redirect:/admin";
    }
}
