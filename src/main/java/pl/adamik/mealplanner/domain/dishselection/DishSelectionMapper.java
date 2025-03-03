package pl.adamik.mealplanner.domain.dishselection;

import org.springframework.stereotype.Component;
import pl.adamik.mealplanner.domain.category.Category;
import pl.adamik.mealplanner.domain.category.dto.CategorySelectionDto;
import pl.adamik.mealplanner.domain.dishselection.dto.DishSelectionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DishSelectionMapper {
    public List<DishSelectionDto> map(List<DishSelection> dishSelections, List<Category> categories) {

        Map<LocalDate, List<DishSelection>> groupedByDate = dishSelections.stream()
                .collect(Collectors.groupingBy(DishSelection::getDate));

        return groupedByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new DishSelectionDto(
                        entry.getKey(), // Data
                        mapCategories(entry.getValue(), categories)
                ))
                .collect(Collectors.toList());
    }


    private List<CategorySelectionDto> mapCategories(List<DishSelection> dishSelections, List<Category> categories) {

        Map<Long, List<CategorySelectionDto.DishDto>> categoriesMap = dishSelections.stream()
                .collect(Collectors.groupingBy(ds -> ds.getDish().getCategory().getId(),
                        Collectors.mapping(ds -> new CategorySelectionDto.DishDto(
                                ds.getDish().getId(), ds.getDish().getName()), Collectors.toList())));

        return categories.stream()
                .filter(category -> categoriesMap.containsKey(category.getId()))
                .map(category -> new CategorySelectionDto(category.getId(), category.getName(), categoriesMap.get(category.getId())))
                .collect(Collectors.toList());
    }
}
