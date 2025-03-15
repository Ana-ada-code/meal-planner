package pl.adamik.mealplanner.domain.dish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DishSaveDto {
    private Long id;
    @NotBlank(message = "Nazwa dania nie może być pusta")
    @Size(min = 2, max = 100, message = "Nazwa dania musi mieć od 2 do 100 znaków")
    private String name;
    @NotBlank(message = "Składniki nie mogą być puste")
    private String ingredients;
    @NotBlank(message = "Przepis nie może być pusty")
    private String recipe;
    @NotNull(message = "Kategorie jest nie mogą być puste")
    private String category;
    private MultipartFile image;
}
