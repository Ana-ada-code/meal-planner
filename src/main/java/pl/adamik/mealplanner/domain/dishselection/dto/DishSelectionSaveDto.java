package pl.adamik.mealplanner.domain.dishselection.dto;

import java.time.LocalDate;

public class DishSelectionSaveDto {
    private Long dishId;
    private LocalDate selectedDate;

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }
}