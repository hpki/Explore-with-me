package ru.practicum.explorewithme.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.compilation.service.CompilationService;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    public static final String ID_PATH_VARIABLE_KEY = "id";
    public static final String EVENT_ID_PATH_VARIABLE_KEY = "eventId";

    private final CompilationService compilationsService;
    private final EventService eventService;

    // Метод для добавления подборки событий админом
    @PostMapping
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Getting request POST for path /admin/compilations: {}", newCompilationDto);
        List<Event> listEvents = eventService.getEventsCompilation(newCompilationDto);
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, listEvents);
        return compilationsService.addCompilation(compilation);
    }

    // Метод для обновления подборки (Добавление нового события) админом
    @PatchMapping("/{id}/events/{eventId}")
    public CompilationDto addEvent(@PathVariable(ID_PATH_VARIABLE_KEY) Long compilationId,
                                   @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Getting request PATCH for path /admin/compilations (id: {}, eventId: {})", compilationId, eventId);
        Event eventForAdd = eventService.getEventById(eventId);
        return compilationsService.updateCompilation(compilationId, eventId, eventForAdd);
    }

    // Метод для закрепление подборки с на странице
    @PatchMapping("/{id}/pin")
    public CompilationDto pin(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        // log.info("Получен запрос PATCH для прикрепления подборки с id: {}", id);
        log.info("Getting request PATCH for pinned compilation with id: {}", id);
        return compilationsService.pinCompilation(id);
    }

    // Метод для удаления подборки
    @DeleteMapping("/{id}")
    public void deleteCompilation(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Getting request DELETE for path /admin/compilations/ по id {}", id);
        compilationsService.deleteCompilation(id);
    }

    // Метод для удаления события
    @DeleteMapping("/{id}/events/{eventId}")
    public void deleteEvent(@PathVariable(ID_PATH_VARIABLE_KEY) Long compilationId,
                            @PathVariable(EVENT_ID_PATH_VARIABLE_KEY) Long eventId) {
        log.info("Getting request DELETE for path /admin/compilations (id: {}, eventId: {})", compilationId, eventId);
        Event deletedEvent = eventService.getEventById(eventId);
        compilationsService.deleteEvent(compilationId, eventId, deletedEvent);
    }

    // Метод для открепления подборки со страницы
    @DeleteMapping("/{id}/pin")
    public CompilationDto unpin(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Getting request PATCH for unpinning compilation with id: {}", id);
        return compilationsService.unpin(id);
    }
}
