package pl.adamik.mealplanner.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.adamik.mealplanner.domain.user.dto.UserCredentialsDto;
import pl.adamik.mealplanner.domain.user.dto.UserDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnCredentials_whenUserExists() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // When
        Optional<UserCredentialsDto> result = userService.findCredentialsByEmail("test@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUserDto().getEmail()).isEqualTo("test@example.com");
        assertThat(result.get().getUserDto().getPassword()).isEqualTo("encodedPassword");
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void shouldReturnEmptyOptional_whenUserDoesNotExist() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        Optional<UserCredentialsDto> result = userService.findCredentialsByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void shouldRegisterUserWithDefaultRole_whenRoleExists() {
        // Given
        UserDto userRegistration = new UserDto();
        userRegistration.setEmail("newuser@example.com");
        userRegistration.setPassword("plainPassword");

        UserRole defaultRole = new UserRole();
        defaultRole.setName("USER");

        when(userRoleRepository.findByName("USER")).thenReturn(Optional.of(defaultRole));
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        // When
        userService.registerUserWithDefaultRole(userRegistration);

        // Then
        verify(userRoleRepository, times(1)).findByName("USER");
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(userRepository, times(1)).save(argThat(user ->
                user.getEmail().equals("newuser@example.com") &&
                        user.getPassword().equals("encodedPassword") &&
                        user.getRoles().contains(defaultRole)
        ));
    }

    @Test
    void shouldThrowException_whenDefaultRoleDoesNotExist() {
        // Given
        UserDto userRegistration = new UserDto();
        userRegistration.setEmail("newuser@example.com");
        userRegistration.setPassword("plainPassword");

        when(userRoleRepository.findByName("USER")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.registerUserWithDefaultRole(userRegistration))
                .isInstanceOf(RuntimeException.class);

        verify(userRoleRepository, times(1)).findByName("USER");
        verify(userRepository, never()).save(any());
    }
}
