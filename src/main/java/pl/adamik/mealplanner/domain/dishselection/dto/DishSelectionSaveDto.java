package pl.adamik.mealplanner.domain.dishselection.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DishSelectionSaveDto {
    private Long dishId;
    private LocalDate selectedDate;
}