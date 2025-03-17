package pl.adamik.mealplanner.domain.user.dto;

import lombok.Getter;

import java.util.Set;

public final class UserCredentialsDto {
    @Getter
    private final UserDto userDto;
    private final Set<String> roles;

    public UserCredentialsDto(UserDto userDto, Set<String> roles) {
        this.userDto = userDto;
        this.roles = Set.copyOf(roles);
    }

    public Set<String> getRoles() {
        return Set.copyOf(roles);
    }
}