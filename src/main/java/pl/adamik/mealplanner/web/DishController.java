package pl.adamik.mealplanner.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pl.adamik.mealplanner.domain.dish.DishService;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionSaveDto;
import pl.adamik.mealplanner.domain.favorite.FavoriteService;
import pl.adamik.mealplanner.domain.rating.RatingService;

@Controller
public class DishController {
    private final DishService dishService;
    private final RatingService ratingService;
    private final FavoriteService favoriteService;
    private final static int TOP_DISHES = 10;

    public DishController(DishService dishService, RatingService ratingService, FavoriteService favoriteService) {
        this.dishService = dishService;
        this.ratingService = ratingService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/danie/{id}")
    public String getDish(@PathVariable long id, Model model, Authentication authentication) {
        DishDto dish = dishService.findDishById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("dish", dish);
        if (authentication != null) {
            String currentUserEmail = authentication.getName();
            Integer rating = ratingService.getUserRatingForDish(currentUserEmail, id).orElse(0);
            boolean userFavoriteForDish = favoriteService.getUserFavoriteForDish(currentUserEmail, id);
            model.addAttribute("userRating", rating);
            model.addAttribute("userFavorite", userFavoriteForDish);
        }
        model.addAttribute("dishSelection", new DishSelectionSaveDto());

        return "dish";
    }

    @GetMapping("/top")
    public String findTopDishes(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DishDto> topDishes = dishService.findTopDishes(TOP_DISHES, pageable);
        model.addAttribute("heading", "Najlepsze 10 dań");
        model.addAttribute("dishes", topDishes);
        model.addAttribute("url", "/top");
        return "dish-listing";
    }

    @GetMapping("/szukaj")
    public String searchDishes(@RequestParam(required = false) String keyword,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DishDto> foundDishes = dishService.searchDishes(keyword, pageable);
        String heading = "Wyniki wyszukiwania dla:  " + keyword;
        model.addAttribute("dishes", foundDishes);
        model.addAttribute("heading", heading);
        model.addAttribute("url", "/szukaj");
        return "dish-listing";
    }
}
