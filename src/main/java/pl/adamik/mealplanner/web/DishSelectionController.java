package pl.adamik.mealplanner.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.adamik.mealplanner.domain.dishselection.DishSelectionService;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionDto;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionSaveDto;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/planer")
public class DishSelectionController {
    public static final String NOTIFICATION_ATTRIBUTE = "notification";
    private final DishSelectionService dishSelectionService;


    public DishSelectionController(DishSelectionService dishSelectionService) {
        this.dishSelectionService = dishSelectionService;
    }

    @GetMapping
    public String findDishSelection (Model model,
                                     Authentication authentication) {
        String currentUserEmail = authentication.getName();
        List<DishSelectionDto> dishSelection = dishSelectionService.getSelectionsGroupedByDateAndCategory(currentUserEmail);

        model.addAttribute("heading", "Zaplanowane dania");
        model.addAttribute("dishSelection", dishSelection);
        return "planner";
    }

    @PostMapping
    public String addToPlanner(DishSelectionSaveDto dishSelection, Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        String currentUserEmail = authentication.getName();
        boolean isAdded = dishSelectionService.addDishToPlanner(dishSelection, currentUserEmail);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE, "Danie zostało dodane do planera");
        return "redirect:/planer";
    }

    @DeleteMapping
    public String removeAll(RedirectAttributes redirectAttributes) {
        dishSelectionService.removeAll();
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE, "Wszystkie zaplanowane dania zostały usunięte");
        return "redirect:/planer";
    }

    @DeleteMapping("/{date}")
    public String remove(@PathVariable String date, RedirectAttributes redirectAttributes) {
        LocalDate localDate = LocalDate.parse(date);
        dishSelectionService.removeDay(localDate);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE, "Wszystkie zaplanowane dania na dzień %s zostały usunięte".formatted(date));
        return "redirect:/planer";
    }

    @DeleteMapping("/by-id/{id}")
    public String remove(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        dishSelectionService.removeDish(id);
        redirectAttributes.addFlashAttribute(NOTIFICATION_ATTRIBUTE, "Wybrane danie zostało usunięte z planera");
        return "redirect:/planer";
    }




}
