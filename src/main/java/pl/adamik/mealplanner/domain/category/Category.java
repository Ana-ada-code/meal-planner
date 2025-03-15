package pl.adamik.mealplanner.domain.category;

import jakarta.persistence.*;
import lombok.*;
import pl.adamik.mealplanner.domain.dish.Dish;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<Dish> dishes = new ArrayList<>();
}
