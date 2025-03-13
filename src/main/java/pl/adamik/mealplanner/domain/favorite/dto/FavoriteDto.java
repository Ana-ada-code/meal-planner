package pl.adamik.mealplanner.domain.favorite.dto;

public class FavoriteDto {
    private Long dishId;

    public FavoriteDto(Long dishId) {
        this.dishId = dishId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }
}
