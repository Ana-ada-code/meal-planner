package pl.adamik.mealplanner.domain.category.dto;

import java.util.List;

public class CategorySelectionDto {
    private Long id;
    private String name;
    private List<DishDto> dishes;

    public CategorySelectionDto(Long id, String name, List<DishDto> dishes) {
        this.id = id;
        this.name = name;
        this.dishes = dishes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DishDto> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishDto> dishes) {
        this.dishes = dishes;
    }

    public static class DishDto {
        private Long id;
        private String name;
        private Long selectionId;

        public DishDto(Long id, String name, Long selectionId) {
            this.id = id;
            this.name = name;
            this.selectionId = selectionId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getSelectionId() {
            return selectionId;
        }

        public void setSelectionId(Long selectionId) {
            this.selectionId = selectionId;
        }
    }
}
