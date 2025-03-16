package pl.adamik.mealplanner.domain.dishselection;

import org.springframework.stereotype.Component;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.category.dto.CategoryDto;
import pl.adamik.mealplanner.domain.category.dto.CategorySelectionDto;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DishSelectionMapper {

    private DishSelectionMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public List<DishSelectionDto> map(List<DishSelection> dishSelections, List<Category> categories) {
        Map<LocalDate, List<DishSelection>> groupedByDate = groupByDate(dishSelections);

        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> createDishSelectionDto(entry.getKey(), entry.getValue(), categories))
                .collect(Collectors.toList());
    }

    private Map<LocalDate, List<DishSelection>> groupByDate(List<DishSelection> dishSelections) {
        return dishSelections.stream()
                .collect(Collectors.groupingBy(DishSelection::getDate));
    }

    private DishSelectionDto createDishSelectionDto(LocalDate date, List<DishSelection> dishSelections, List<Category> categories) {
        List<CategorySelectionDto> categoryDtos = mapCategories(dishSelections, categories);
        return new DishSelectionDto(date, categoryDtos);
    }

    private List<CategorySelectionDto> mapCategories(List<DishSelection> dishSelections, List<Category> categories) {
        Map<Long, List<CategorySelectionDto.DishDto>> groupedByCategory = groupByCategory(dishSelections);

        return categories.stream()
                .filter(category -> groupedByCategory.containsKey(category.getId()))
                .map(category -> createCategoryDto(category, groupedByCategory))
                .collect(Collectors.toList());
    }

    private Map<Long, List<CategorySelectionDto.DishDto>> groupByCategory(List<DishSelection> dishSelections) {
        return dishSelections.stream()
                .collect(Collectors.groupingBy(
                        ds -> ds.getDish().getCategory().getId(),
                        Collectors.mapping(this::createDishDto, Collectors.toList())
                ));
    }

    private CategorySelectionDto createCategoryDto(Category category, Map<Long, List<CategorySelectionDto.DishDto>> groupedByCategory) {
        return new CategorySelectionDto(new CategoryDto(category.getId(), category.getName()), groupedByCategory.get(category.getId()));
    }

    private CategorySelectionDto.DishDto createDishDto(DishSelection dishSelection) {
        return new CategorySelectionDto.DishDto(
                dishSelection.getDish().getId(),
                dishSelection.getDish().getName(),
                dishSelection.getId()
        );
    }
}
