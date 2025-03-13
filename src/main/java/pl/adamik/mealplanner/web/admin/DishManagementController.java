package pl.adamik.mealplanner.web.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.adamik.mealplanner.domain.category.CategoryService;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;
import pl.adamik.mealplanner.domain.dish.DishDtoMapper;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.dish.dto.DishSaveDto;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class DishManagementController {
    private final DishService dishService;
    private final CategoryService categoryService;

    public DishManagementController(DishService dishService, CategoryService categoryService) {
        this.dishService = dishService;
        this.categoryService = categoryService;
    }

    @GetMapping("/dodaj-danie")
    public String addDishForm(Model model) {
        List<CategoryDto> allCategories = categoryService.findAllCategories();
        model.addAttribute("categories", allCategories);
        DishSaveDto dishSaveDto = new DishSaveDto();
        model.addAttribute("dishSaveDto", dishSaveDto);
        return "admin/dish-form";
    }

    @PostMapping("/dodaj-danie")
    public String addDish(@Valid @ModelAttribute DishSaveDto dishSaveDto, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            List<CategoryDto> allCategories = categoryService.findAllCategories();
            model.addAttribute("categories", allCategories);
            return "admin/dish-form";
        }

        dishService.addDish(dishSaveDto);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Danie %s zostało zapisane".formatted(dishSaveDto.getName()));
        return "redirect:/admin";
    }

    @DeleteMapping("/usun-danie")
    public String removeCategory(@RequestParam Long dishId, RedirectAttributes redirectAttributes) {
        dishService.deleteDish(dishId);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Wybrane danie zastało usunięte");
        return "redirect:/admin";
    }

    @GetMapping("/edytuj-danie")
    public String updateDishForm(@RequestParam Long dishId, Model model) {
        List<CategoryDto> allCategories = categoryService.findAllCategories();
        model.addAttribute("categories", allCategories);
        DishDto dishDto = dishService.findDishById(dishId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        DishSaveDto dishSaveDto = DishDtoMapper.map(dishDto);
        model.addAttribute("dishSaveDto", dishSaveDto);
        return "admin/dish-update-form";
    }

    @PutMapping("/edytuj-danie")
    public String updateDish(@Valid @ModelAttribute DishSaveDto dishSaveDto, BindingResult bindingResult,
                             @RequestParam Long dishId, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            List<CategoryDto> allCategories = categoryService.findAllCategories();
            model.addAttribute("categories", allCategories);
            dishSaveDto.setId(dishId);
            model.addAttribute("dishSaveDto", dishSaveDto);
            return "admin/dish-update-form";
        }

        dishService.updateDish(dishSaveDto, dishId);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Danie %s zostało zaktualizowane".formatted(dishSaveDto.getName()));
        return "redirect:/admin";
    }
}
