package pl.adamik.mealplanner.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.adamik.mealplanner.domain.dish.dto.DishDto;
import pl.adamik.mealplanner.domain.favorite.FavoriteService;
import pl.adamik.mealplanner.domain.favorite.dto.FavoriteDto;

@Controller
@RequestMapping("/ulubione")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public String findFavorite(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model,
                               Authentication authentication) {
        String currentUserEmail = authentication.getName();
        Pageable pageable = PageRequest.of(page, size);
        Page<DishDto> favorite = favoriteService.findFavorite(currentUserEmail, pageable);

        model.addAttribute("heading", "Ulubione");
        model.addAttribute("dishes", favorite);
        model.addAttribute("url", "/ulubione");
        return "dish-listing";
    }

    @PostMapping
    public String addFavorite(FavoriteDto favoriteDto,
                              @RequestHeader String referer,
                              Authentication authentication) {
        String currentUserEmail = authentication.getName();
        boolean isAdded = favoriteService.addFavoriteToDish(favoriteDto, currentUserEmail);
        Long id = favoriteDto.getDishId();
        return "redirect:" + referer;
    }

    @DeleteMapping
    public String removeFavorite(FavoriteDto favoriteDto,
                                 @RequestHeader String referer,
                                 Authentication authentication) {
        String currentUserEmail = authentication.getName();
        boolean isRemoved = favoriteService.removeFavoriteFromDish(favoriteDto, currentUserEmail);
        Long id = favoriteDto.getDishId();
        return "redirect:" + referer;
    }
}
