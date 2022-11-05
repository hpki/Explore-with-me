package ru.practicum.explorewithme.category.service;

import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    // получения списка всех категорий
    List<CategoryDto> getAll();

    // получение dto категории с id
    CategoryDto getCategoryDto(Long id);

    // получение категории с id
    Category getCategory(Long id);

    // добавление категории
    CategoryDto addCategory(Category category);

    // обновление категории
    CategoryDto updateCategory(CategoryDto category);

    // удаление категории
    void deleteCategory(Long id);
}
