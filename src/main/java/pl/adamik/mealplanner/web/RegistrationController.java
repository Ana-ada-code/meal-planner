package pl.adamik.mealplanner.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.adamik.mealplanner.domain.user.UserService;
import pl.adamik.mealplanner.domain.user.dto.UserDto;


@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/rejestracja")
    public String registrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "registration-form";
    }

    @PostMapping("/rejestracja")
    public String register(@Valid @ModelAttribute UserDto userDto, BindingResult bindingResult, Model model) {

        if (userService.findCredentialsByEmail(userDto.getEmail()).isPresent()) {
            FieldError error = new FieldError("userDto", "email", "Podany email jest już zarejestrowany");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "registration-form";
        }

        userService.registerUserWithDefaultRole(userDto);
        return "redirect:/login?register=true";
    }
}
