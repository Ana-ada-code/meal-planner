package pl.adamik.mealplanner.domain.user.dto;

import java.util.Set;

public record UserCredentialsDto(
        UserRegistrationDto registrationDto,
        Set<String> roles
) {
}