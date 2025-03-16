package pl.adamik.mealplanner.domain.user.dto;

import lombok.Getter;

import java.util.Set;

public final class UserCredentialsDto {
    @Getter
    private final UserRegistrationDto registrationDto;
    private final Set<String> roles;

    public UserCredentialsDto(UserRegistrationDto registrationDto, Set<String> roles) {
        this.registrationDto = registrationDto;
        this.roles = Set.copyOf(roles);
    }

    public Set<String> getRoles() {
        return Set.copyOf(roles);
    }
}