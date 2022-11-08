package ru.practicum.explorewithme.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.category.repository.CategoryRepository;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.exception.ForbiddenException;
import ru.practicum.explorewithme.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getAll() {
        log.info("All category list is recived");
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryDto(Long id) {
        log.info("Category dto with id {} is recived", id);
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Category with id %s not found", id)));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public Category getCategory(Long id) {
        log.info("Category with id {} is recived", id);
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Category with id %s is not found", id)));
    }

    @Override
    @Transactional
    public CategoryDto addCategory(Category category) {
        log.info("Category {} added", category);
        Category newCategory = categoryRepository.save(category);
        return CategoryMapper.toCategoryDto(newCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        log.info("Category with id {} updated", categoryDto.getId());
        Category updatingCategory = getCategory(categoryDto.getId());
        updatingCategory.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(updatingCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        log.info("Category with id {} is deleted", id);
        Category category = getCategory(id);
        List<Event> eventsByCategory = eventRepository.findAllByCategoryId(id);
        if (eventsByCategory.isEmpty()) {
            categoryRepository.delete(category);
        } else {
            throw new ForbiddenException(String.format("Category with id %s is not empty", id));
        }
    }
}
