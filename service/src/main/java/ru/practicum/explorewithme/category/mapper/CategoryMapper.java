package ru.practicum.explorewithme.category.mapper;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {

   public static Category toCategory(NewCategoryDto newCategoryDto) {
       return Category.builder()
               .name(newCategoryDto.getName())
               .build();

   }

  public static CategoryDto toCategoryDto(Category category) {
      return CategoryDto.builder()
              .id(category.getId())
              .name(category.getName())
              .build();
  }

}
