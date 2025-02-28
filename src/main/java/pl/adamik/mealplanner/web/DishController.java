package pl.adamik.mealplanner.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.rating.RatingService;

@Controller
public class DishController {
    private final DishService dishService;
    private final RatingService ratingService;

    public DishController(DishService dishService, RatingService ratingService) {
        this.dishService = dishService;
        this.ratingService = ratingService;
    }

    @GetMapping("/danie/{id}")
    public String getDish(@PathVariable long id, Model model, Authentication authentication) {
        DishDto dish = dishService.findDishById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("dish", dish);
        if (authentication != null) {
            String currentUserEmail = authentication.getName();
            Integer rating = ratingService.getUserRatingForDish(currentUserEmail,id).orElse(0);
            model.addAttribute("userRating", rating);
        }
        return "dish";
    }
}
