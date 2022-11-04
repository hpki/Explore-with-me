package ru.practicum.explorewithme.compilation.service;

import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

public interface CompilationService {

    // Получение списка всех подборок
    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    // Получение dto подборки по id
    CompilationDto getDtoCompilationById(Long id);

    // Получение подборки по id
    Compilation getCompilationById(Long id);

    // Добавление новой подборки
    CompilationDto addCompilation(Compilation compilation);

    // Обновление подборки (Добавление нового события)
    CompilationDto updateCompilation(Long compilationId, Long eventId, Event event);

    // Удаление подборки по id
    void deleteCompilation(Long id);

    // Закрепление подборки с на странице
    CompilationDto pinCompilation(Long id);

    // Открепление подборки с на странице
    CompilationDto unpin(Long id);

    // Удаление события
    void deleteEvent(Long compilationId, Long eventId, Event event);
}
