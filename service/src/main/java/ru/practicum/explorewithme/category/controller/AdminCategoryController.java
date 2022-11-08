package ru.practicum.explorewithme.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.service.CategoryService;

import javax.validation.Valid;

import static ru.practicum.explorewithme.EwmMainService.ID_PATH_VARIABLE_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    // Добавление категории
    @PostMapping
    public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Getting request POST for path /admin/categories: {}", newCategoryDto);
        Category category = CategoryMapper.toCategory(newCategoryDto);
        return categoryService.addCategory(category);
    }

    // Обновление категории
    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Getting request PATCH for path /admin/categories: {}", categoryDto);
        return categoryService.updateCategory(categoryDto);
    }

    // Удаление категории
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Getting request DELETE for path /admin/categories/ по id {}", id);
        categoryService.deleteCategory(id);
    }
}
