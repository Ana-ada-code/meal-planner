package pl.adamik.mealplanner.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.adamik.mealplanner.domain.dishselection.DishSelectionService;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionDto;

import java.util.List;

@Controller
public class DishSelectionController {
    private final DishSelectionService dishSelectionService;

    public DishSelectionController(DishSelectionService dishSelectionService) {
        this.dishSelectionService = dishSelectionService;
    }

    @GetMapping("/planer")
    public String findDishSelection (Model model,
                                     Authentication authentication) {
        String currentUserEmail = authentication.getName();
        List<DishSelectionDto> dishSelection = dishSelectionService.getSelectionsGroupedByDateAndCategory(currentUserEmail);

        model.addAttribute("heading", "Zaplanowane dania");
        model.addAttribute("dishSelection", dishSelection);
        return "planner";
    }

}
