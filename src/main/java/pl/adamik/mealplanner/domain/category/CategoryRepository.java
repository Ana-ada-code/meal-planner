package pl.adamik.mealplanner.domain.category;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByNameIgnoreCase(String name);
}
