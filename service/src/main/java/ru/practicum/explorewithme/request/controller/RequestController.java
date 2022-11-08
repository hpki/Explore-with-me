package ru.practicum.explorewithme.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.service.EventService;
import ru.practicum.explorewithme.request.dto.RequestDto;
import ru.practicum.explorewithme.request.service.RequestService;

import java.util.List;

import static ru.practicum.explorewithme.EwmMainService.ID_PATH_VARIABLE_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{id}/requests")
public class RequestController {

    private final RequestService requestService;
    private final EventService eventService;

    // Получение запросов
    @GetMapping
    public List<RequestDto> getAll(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Getting request GET for path /users/{}/requests", id);
        return requestService.getAll(id);
    }

    // Добавление запроса на событие
    @PostMapping
    public RequestDto addRequest(@PathVariable(ID_PATH_VARIABLE_KEY) Long userId, @RequestParam Long eventId) {
        log.info("Getting request POST for path /users/{}/requests", userId);
        Event event = eventService.getEventById(eventId);
        return requestService.addRequest(userId, event);
    }

    // Обновление запроса
    @PatchMapping("/{requestId}/cancel")
    public RequestDto update(@PathVariable(ID_PATH_VARIABLE_KEY) Long userId, @PathVariable Long requestId) {
        log.info("Getting request PATCH for path /users/{}/requests", userId);
        return requestService.updateRequest(userId, requestId);
    }
}
