package pl.adamik.mealplanner.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "Email nie może być pusty")
    private String email;
    @NotBlank(message = "Hasło nie może być puste")
    private String password;
}
