package pl.adamik.mealplanner.web.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.adamik.mealplanner.domain.category.CategoryService;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;

@Controller
@RequestMapping("/admin")
public class CategoryManagementController {
    private final CategoryService categoryService;

    public CategoryManagementController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/dodaj-kategorie")
    public String addCategoryForm(Model model) {
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("categoryDto", categoryDto);
        return "admin/category-form";
    }

    @PostMapping("/dodaj-kategorie")
    public String addCategory(@Valid @ModelAttribute CategoryDto categoryDto, BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes) {

        if (categoryService.findCategoryByName(categoryDto.getName()).isPresent()) {
            FieldError error = new FieldError("categoryDto", "name", "Kategoria o podanej nazwie już istnieje");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryDto", categoryDto);
            return "admin/category-form";
        }

        categoryService.addCategory(categoryDto);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Kategoria %s zastała zapisana".formatted(categoryDto.getName()));
        return "redirect:/admin";
    }

    @DeleteMapping("/usun-kategorie")
    public String removeCategory(@RequestParam String categoryName, RedirectAttributes redirectAttributes) {
        categoryService.removeCategory(categoryName);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Kategoria %s zastała usunięta".formatted(categoryName));
        return "redirect:/admin";
    }

    @PatchMapping("/aktualizuj-kategorie")
    public String updateCategory(@RequestParam String categoryName, @RequestParam Long categoryId,
                                 RedirectAttributes redirectAttributes) {
        categoryService.update(categoryId, categoryName);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Nazwa kategorii została zaktualizowana na %s".formatted(categoryName));
        return "redirect:/admin";
    }
}
