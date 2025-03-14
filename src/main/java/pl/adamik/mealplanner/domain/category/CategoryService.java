package pl.adamik.mealplanner.domain.category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<CategoryDto> findCategoryByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .map(CategoryDtoMapper::map);
    }

    public List<CategoryDto> findAllCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
                .map(CategoryDtoMapper::map)
                .toList();
    }

    @Transactional
    public void addCategory(CategoryDto category) {
        Category categoryToSave = new Category();
        categoryToSave.setName(category.getName());
        categoryRepository.save(categoryToSave);
    }

    @Transactional
    public boolean removeCategory(String name) {
        Optional<Category> categoryOpt = categoryRepository.findByNameIgnoreCase(name);

        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();

            categoryRepository.delete(category);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean update(Long id, String name) {
        Optional<Category> category = categoryRepository.findById(id);

        if (name != null && category.isPresent()) {
            category.get().setName(name);
            categoryRepository.save(category.get());
            return true;
        }

        return false;
    }
}
