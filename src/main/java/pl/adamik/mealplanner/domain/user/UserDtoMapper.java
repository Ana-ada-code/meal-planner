package pl.adamik.mealplanner.domain.user;

import pl.adamik.mealplanner.domain.user.dto.UserCredentialsDto;
import pl.adamik.mealplanner.domain.user.dto.UserDto;

import java.util.Set;
import java.util.stream.Collectors;

class UserDtoMapper {

    private UserDtoMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static UserCredentialsDto mapCredentials(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        Set<String> roles = user.getRoles()
                .stream()
                .map(UserRole::getName)
                .collect(Collectors.toSet());
        return new UserCredentialsDto(new UserDto(email, password), roles);
    }
}