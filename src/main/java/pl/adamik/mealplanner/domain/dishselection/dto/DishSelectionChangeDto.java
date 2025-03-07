package pl.adamik.mealplanner.domain.dishselection.dto;

import java.time.LocalDate;

public class DishSelectionChangeDto {
    private LocalDate selectedDate;
    private Long selectedDishId;

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Long getSelectedDishId() {
        return selectedDishId;
    }

    public void setSelectedDishId(Long selectedDishId) {
        this.selectedDishId = selectedDishId;
    }
}
