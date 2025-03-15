package pl.adamik.mealplanner.domain.user;

import jakarta.persistence.*;
import lombok.*;
import pl.adamik.mealplanner.domain.dishselection.DishSelection;
import pl.adamik.mealplanner.domain.favorite.Favorite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<UserRole> roles = new HashSet<>();
    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<DishSelection> dishSelections = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites = new ArrayList<>();
}