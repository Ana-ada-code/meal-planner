package pl.adamik.mealplanner.domain.user;

import pl.adamik.mealplanner.domain.user.dto.UserCredentialsDto;
import pl.adamik.mealplanner.domain.user.dto.UserRegistrationDto;

import java.util.Set;
import java.util.stream.Collectors;

class UserDtoMapper {
    static UserCredentialsDto mapCredentials(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        Set<String> roles = user.getRoles()
                .stream()
                .map(UserRole::getName)
                .collect(Collectors.toSet());
        return new UserCredentialsDto(new UserRegistrationDto(email, password), roles);
    }
}